package com.logtarget.webapp.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logtarget.webapp.service.LogPersistenceService;
import com.logtarget.webapp.service.S3Service;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * S3ServiceConfig injects an instance of S3Service as LogPersistenceService when
 * the required properties for S3 are present
 */
@Configuration
@Conditional(S3ServiceConfig.S3ServicePropertiesPresent.class)
public class S3ServiceConfig {
    private static final String LOG_BUCKET_NAME = "log.bucket.name";
    private static final String AWS_ACCESS_KEY = "cloud.aws.credentials.accessKey";
    private static final String AWS_SECRET_KEY = "cloud.aws.credentials.secretKey";
    private final S3Service s3Service;

    public S3ServiceConfig(
            final Environment environment,
            final ObjectMapper objectMapper
            ) {
        this.s3Service = new S3Service(
                AmazonS3ClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
                                environment.getRequiredProperty(AWS_SECRET_KEY),
                                environment.getRequiredProperty(AWS_SECRET_KEY)))).build(),
                objectMapper,
                environment.getRequiredProperty(LOG_BUCKET_NAME)
        );
    }

    /**
     * adds an instance of S3Service as a LogPersistenceService to the Spring context
     * @return
     */
    @Bean
    public LogPersistenceService logPersistenceService() {
        return s3Service;
    }

    /**
     * S3ServicePropertiesPresent extends AllNestedConditions to inject S3Service only when
     * all the required properties are present
     */
    public static class S3ServicePropertiesPresent extends AllNestedConditions {

        public S3ServicePropertiesPresent() {
            super(ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @ConditionalOnProperty(LOG_BUCKET_NAME)
        private class LogBucketName {}

        @ConditionalOnProperty(AWS_ACCESS_KEY)
        private class AwsAccessKey {}

        @ConditionalOnProperty(AWS_SECRET_KEY)
        private class AwsSecretKey {}

    }

}
