package br.com.fiap.utils;

import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3Manager {

	public static final AmazonS3 s3Client;

	static {
		final String endpoint = System.getenv("ENDPOINT_OVERRIDE");
		
        if (endpoint != null && !endpoint.isEmpty()) {
        	EndpointConfiguration endpointConfiguration = new EndpointConfiguration(endpoint, "us-east-1");
			s3Client = AmazonS3ClientBuilder.standard().withEndpointConfiguration(endpointConfiguration).build();
        } else {
        	s3Client = AmazonS3ClientBuilder.defaultClient();
        }
	}
}