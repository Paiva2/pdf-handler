package com.root.signaturehandler.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {
    @Value("${aws.access.key.id}")
    private String accessKey;
    @Value("${aws.secret.key}")
    private String secretKey;

    public AWSCredentials setupCredentials() {
        AWSCredentials credentials = new BasicAWSCredentials(
                this.accessKey,
                this.secretKey
        );

        return credentials;
    }

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials credentials = this.setupCredentials();

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();

        return s3Client;
    }
}
