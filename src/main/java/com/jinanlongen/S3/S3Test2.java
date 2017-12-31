package com.jinanlongen.S3;

import java.util.List;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.util.StringUtils;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年12月16日
 */
public class S3Test2 {
	static final private String accessKey = "AKIAIZFRYWNCYCF663ZQ";
	static final private String secretKey = "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";
	static final private String host = "ip:port";

	// static final private Integer port = 80;
	public static void main(String[] args) {
		test();
	}

	public static void test() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setConnectionTimeout(3000);
		clientConfig.setProtocol(Protocol.HTTP);
		clientConfig.useGzip();
		clientConfig.useTcpKeepAlive();
		clientConfig.setSignerOverride("S3SignerType");

		@SuppressWarnings("deprecation")
		AmazonS3 conn = new AmazonS3Client(credentials, clientConfig);
		conn.setEndpoint(host);

		// File file = new File("D:/1.jpg");
		List<Bucket> buckets = conn.listBuckets();
		for (Bucket bucket : buckets) {
			System.out.println(bucket.getName() + "\t" + StringUtils.fromDate(bucket.getCreationDate()));

			// 上传文件
			// conn.putObject(new PutObjectRequest(bucket.getName(), "hello.txt", file));

			// 下载文件
			// conn.getObject(new GetObjectRequest(bucket.getName(), "hello.txt"), new
			// File("/Users/maotan/aa.bak"));
		}

	}
}
