package com.andrewsreis.dundermifflin.app.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.*;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.CreateFunctionRequest;
import software.amazon.awssdk.services.lambda.model.FunctionCode;
import software.amazon.awssdk.services.lambda.model.Runtime;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;

@TestConfiguration
public class TestContainersConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestContainersConfiguration.class);

    private final AwsProperties awsProperties;
    private final AwsBasicCredentials credentials;

    private String triviaEndpoint;

    public TestContainersConfiguration(AwsProperties awsProperties) {
        this.awsProperties = awsProperties;
        this.credentials = AwsBasicCredentials.create(awsProperties.getCredentials().getAccessKeyId(), awsProperties.getCredentials().getSecretAccessKey());
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public PostgreSQLContainer<?> postgresSQLContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:15.3"))
                .withExposedPorts(5432)
                .withDatabaseName("dundermifflin")
                .withUsername(awsProperties.getRds().getUsername())
                .withPassword(awsProperties.getRds().getPassword())
                .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
                .withInitScript("static/test-db-init.sql")
                .withCreateContainerCmdModifier(
                        createContainerCmd -> createContainerCmd.withName("postgres-testcontainers")
                );
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public GenericContainer<?> redisContainer() {
        return new GenericContainer<>(DockerImageName.parse("redis:latest"))
                .withExposedPorts(awsProperties.getElasticache().getPort())
                .waitingFor(Wait.forListeningPort())
                .withCreateContainerCmdModifier(
                        createContainerCmd -> createContainerCmd.withName("redis-testcontainers")
                );
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer localStackContainer() {
        LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack-pro:latest"))
                .withServices(S3, SQS, LAMBDA, API_GATEWAY, CLOUDWATCHLOGS)
                .withEnv("LOCALSTACK_AUTH_TOKEN", System.getenv("LOCALSTACK_AUTH_TOKEN"))
                .withEnv("AWS_DEFAULT_REGION", "eu-west-1")
                .withEnv("AWS_ACCESS_KEY_ID", "test")
                .withEnv("AWS_SECRET_ACCESS_KEY", "test")
                .withEnv("FORCE_CLEANUP", "1")
                .withExposedPorts(4566)
                .withCreateContainerCmdModifier(
                        createContainerCmd -> createContainerCmd.withName("localstack-testcontainers")
                )
                .waitingFor(Wait.forListeningPort());

        localStackContainer.setPortBindings(listOf("4566:4566"));
        localStackContainer.start();
        initializeResources(localStackContainer);

        return localStackContainer;
    }

    private void initializeResources(LocalStackContainer localStackContainer) {
        // Initialize S3 Bucket
        S3Client s3Client = S3Client.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(S3))
                .region(Region.of("eu-west-1"))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        s3Client.createBucket(b -> b.bucket("dunder-mifflin-bucket"));
        LOGGER.info("S3 bucket 'dunder-mifflin-bucket' created.");

        // Initialize SQS Queue
        SqsClient sqsClient = SqsClient.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(SQS))
                .region(Region.of("eu-west-1"))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        sqsClient.createQueue(q -> q.queueName("quotes"));
        LOGGER.info("SQS queue 'quotes' created.");

        // Initialize API Gateway and Lambda Function for Trivia external API
        try {
            initializeLambdaAndApiGateway(localStackContainer);
            LOGGER.info("Lambda and API Gateway initialized.");
        } catch (Exception ignored) {
            LOGGER.error("Error initializing Lambda and API Gateway: ", ignored);
        }
    }

    private void initializeLambdaAndApiGateway(LocalStackContainer localStackContainer) throws IOException {
        LambdaClient lambdaClient = LambdaClient.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(LAMBDA))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.EU_WEST_1)
                .build();

        ApiGatewayClient apiGatewayClient = ApiGatewayClient.builder()
                .endpointOverride(localStackContainer.getEndpointOverride(API_GATEWAY))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.EU_WEST_1)
                .build();

        String functionName = "TheOfficeTriviaFunction";
        String roleArn = "arn:aws:iam::000000000000:role/lambda-role";

        createLambdaFunction(lambdaClient, functionName, roleArn);
        createApiGateway(apiGatewayClient, functionName);
    }

    private void createLambdaFunction(LambdaClient lambdaClient, String functionName, String roleArn) throws IOException {
        InputStream lambdaZipStream = getClass().getClassLoader().getResourceAsStream("static/trivia_api.zip");
        if (lambdaZipStream == null) {
            throw new IOException("Could not find trivia_api.zip in resources");
        }

        SdkBytes functionCode = SdkBytes.fromInputStream(lambdaZipStream);

        lambdaClient.createFunction(CreateFunctionRequest.builder()
                .functionName(functionName)
                .runtime(Runtime.PYTHON3_8)
                .handler("trivia_api.lambda_handler")
                .code(FunctionCode.builder().zipFile(functionCode).build())
                .role(roleArn)
                .build());

        LOGGER.info("Lambda function '{}' created.", functionName);
    }

    private void createApiGateway(ApiGatewayClient apiGatewayClient, String functionName) {
        String apiName = "TheOfficeTriviaAPI";
        String resourcePath = "trivia";
        String stageName = "test";
        String region = "eu-west-1";
        String accountId = "000000000000";

        CreateRestApiResponse apiResponse = apiGatewayClient.createRestApi(CreateRestApiRequest.builder().name(apiName).build());

        String apiId = apiResponse.id();

        String parentId = apiGatewayClient.getResources(GetResourcesRequest.builder().restApiId(apiId).build())
                .items().stream()
                .filter(resource -> resource.path().equals("/"))
                .map(Resource::id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Root resource not found"));

        CreateResourceResponse resourceResponse = apiGatewayClient.createResource(CreateResourceRequest.builder()
                .restApiId(apiId)
                .parentId(parentId)
                .pathPart(resourcePath)
                .build());

        String resourceId = resourceResponse.id();

        apiGatewayClient.putMethod(PutMethodRequest.builder()
                .restApiId(apiId)
                .resourceId(resourceId)
                .httpMethod("GET")
                .authorizationType("NONE")
                .build());

        String functionArn = "arn:aws:lambda:" + region + ":" + accountId + ":function:" + functionName;

        apiGatewayClient.putIntegration(PutIntegrationRequest.builder()
                .restApiId(apiId)
                .resourceId(resourceId)
                .httpMethod("GET")
                .type(IntegrationType.AWS_PROXY)
                .integrationHttpMethod("POST")
                .uri("arn:aws:apigateway:" + region + ":lambda:path/2015-03-31/functions/" + functionArn + "/invocations")
                .build());

        apiGatewayClient.createDeployment(CreateDeploymentRequest.builder()
                .restApiId(apiId)
                .stageName(stageName)
                .build());

        triviaEndpoint = "http://localhost:4566/restapis/" + apiId + "/" + stageName + "/_user_request_/" + resourcePath;
        LOGGER.info("API Gateway deployed at: {}", triviaEndpoint);
    }

    public String getTriviaApiEndpoint() {
        return triviaEndpoint;
    }
}
