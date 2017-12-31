package com.jinanlongen.S3;

import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年12月16日
 */
public class S3Test {
	private static String bucketName = "test20171216";
	private static String keyName = "s3test.jpg"; // 保存的名字
	private static String uploadFileName = "D://1.jpg"; // 本地要上传的文件

	public static void main(String[] args) throws IOException {

		String ak = "AKIAIZFRYWNCYCF663ZQ";
		String sk = "d8i7EcJFM+js+dM71rLenkv3T42sh2JjAPuKd0aP";
		AWSCredentials awsCredentials = new BasicAWSCredentials(ak, sk);

		AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials));
		builder.setRegion(Regions.AP_SOUTHEAST_1.getName());
		// builder.setRegion(Regions.CN_NORTH_1.getName());
		AmazonS3 s3Client = builder.build();

		try {
			System.out.println("Uploading a new object to S3 from a file\n");
			File file = new File(uploadFileName);
			s3Client.putObject(new PutObjectRequest(bucketName, keyName, file));
			System.out.println("ok");
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which " + "means your request made it "
					+ "to Amazon S3, but was rejected with an error response" + " for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which " + "means the client encountered "
					+ "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}

	}

}
