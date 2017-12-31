package com.jinanloongen.kafka;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class kafkaConsumer extends Thread {
	// 要消费的话题
	private String topic;

	public kafkaConsumer(String topic) {
		super();
		this.topic = topic;
	}

	@Override
	public void run() {
		ConsumerConnector consumer = createConsumer();
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, 1); // 一次从主题中获取一个数据
		Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream = messageStreams.get(topic).get(0);// 获取每次接收到的这个数据
		ConsumerIterator<byte[], byte[]> iterator = stream.iterator();

		while (iterator.hasNext()) {
			String message = null;
			try {
				message = new String(iterator.next().message(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("接收到: " + message);
			// System.out.println(iterator.length());
			System.out.println("----------------------" + Charset.defaultCharset());
		}
	}

	private ConsumerConnector createConsumer() {
		Properties properties = new Properties();
		// 配置要连接的zookeeper地址与端口
		properties.put("zookeeper.connect", "192.168.200.31:2181");// 声明zk
		// properties.put("zookeeper.connect", "192.168.200.31:9092");// 声明zk
		properties.put("group.id", "group5");// 必须要使用别的组名称， 如果生产者和消费者都在同一组，则不能访问同一组内的topic数据
		properties.put("zookeeper.session.timeout.ms", "10000");
		return Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
	}

	public static void main(String[] args) {
		// new kafkaConsumer("artd_b").start();// 使用kafka集群中创建好的主题 test
		new kafkaConsumer("artd_b").start();// 使用kafka集群中创建好的主题 test

	}

}
