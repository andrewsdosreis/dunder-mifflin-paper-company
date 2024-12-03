package com.andrewsreis.dundermifflin.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties(prefix = "aws")
@Profile({"dev", "test"})
public class AwsProperties {
    private String region;
    private ApiGateway apiGateway;
    private Credentials credentials;
    private Elasticache elasticache;
    private Rds rds;
    private S3 s3;
    private Sqs sqs;

    String getRegion() {
        return region;
    }

    void setRegion(String region) {
        this.region = region;
    }

    ApiGateway getApiGateway() {
        return apiGateway;
    }

    void setApiGateway(ApiGateway apiGateway) {
        this.apiGateway = apiGateway;
    }

    Credentials getCredentials() {
        return credentials;
    }

    void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    Elasticache getElasticache() {
        return elasticache;
    }

    void setElasticache(Elasticache elasticache) {
        this.elasticache = elasticache;
    }

    Rds getRds() {
        return rds;
    }

    void setRds(Rds rds) {
        this.rds = rds;
    }

    S3 getS3() {
        return s3;
    }

    void setS3(S3 s3) {
        this.s3 = s3;
    }

    Sqs getSqs() {
        return sqs;
    }

    void setSqs(Sqs sqs) {
        this.sqs = sqs;
    }

    static class ApiGateway {
        private String endpoint;

        String getEndpoint() {
            return endpoint;
        }

        void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }

    static class Credentials {
        private String accessKeyId;
        private String secretAccessKey;

        String getAccessKeyId() {
            return accessKeyId;
        }

        void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        String getSecretAccessKey() {
            return secretAccessKey;
        }

        void setSecretAccessKey(String secretAccessKey) {
            this.secretAccessKey = secretAccessKey;
        }
    }

    static class Elasticache {
        private String host;
        private Integer port;

        String getHost() {
            return host;
        }

        void setHost(String host) {
            this.host = host;
        }

        Integer getPort() {
            return port;
        }

        void setPort(Integer port) {
            this.port = port;
        }
    }

    static class Rds {
        private String url;
        private String username;
        private String password;

        String getUrl() {
            return url;
        }

        void setUrl(String url) {
            this.url = url;
        }

        String getUsername() {
            return username;
        }

        void setUsername(String username) {
            this.username = username;
        }

        String getPassword() {
            return password;
        }

        void setPassword(String password) {
            this.password = password;
        }
    }

    static class S3 {
        private String bucket;
        private String endpoint;

        String getBucket() {
            return bucket;
        }

        void setBucket(String bucket) {
            this.bucket = bucket;
        }

        String getEndpoint() {
            return endpoint;
        }

        void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }

    static class Sqs {
        private String queue;
        private String endpoint;

        String getQueue() {
            return queue;
        }

        void setQueue(String queue) {
            this.queue = queue;
        }

        String getEndpoint() {
            return endpoint;
        }

        void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }
    }
}