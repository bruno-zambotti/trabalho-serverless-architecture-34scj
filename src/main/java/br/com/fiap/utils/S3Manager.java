package br.com.fiap.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3Manager {

	public static final AmazonS3 s3Client;

	static {
		s3Client = AmazonS3ClientBuilder.defaultClient();
	}
}