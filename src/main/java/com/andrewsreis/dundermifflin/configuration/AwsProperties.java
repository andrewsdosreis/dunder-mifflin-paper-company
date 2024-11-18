package com.andrewsreis.dundermifflin.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws")
class AwsProperties {
    private String region;
    private Credentials credentials;
    private S3 s3;
    private Elasticache elasticache;
    private Sqs sqs;

    String getRegion() {
        return region;
    }

    void setRegion(String region) {
        this.region = region;
    }

    Credentials getCredentials() {
        return credentials;
    }

    void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    S3 getS3() {
        return s3;
    }

    void setS3(S3 s3) {
        this.s3 = s3;
    }

    Elasticache getElasticache() {
        return elasticache;
    }

    void setElasticache(Elasticache elasticache) {
        this.elasticache = elasticache;
    }

    Sqs getSqs() {
        return sqs;
    }

    void setSqs(Sqs sqs) {
        this.sqs = sqs;
    }

    static class Credentials {
        private String accessKey;
        private String secretKey;

        String getAccessKey() {
            return accessKey;
        }

        void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        String getSecretKey() {
            return secretKey;
        }

        void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
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