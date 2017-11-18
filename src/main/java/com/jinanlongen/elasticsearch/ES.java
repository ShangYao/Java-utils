package com.jinanlongen.elasticsearch;

import java.util.List;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年11月15日
 */
public class ES {
	IndexRequest source = null;

	public TransportClient getClient() {
		TransportClient client = null;
		//
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("cluster.name", "production_roc");
		// Settings.Builder settings = Settings.builder().put(map);
		// try {
		// client = TransportClient.builder().settings(settings).build()
		// .addTransportAddress(new
		// InetSocketTransportAddress(InetAddress.getByName("www.wenbronk.com"),
		// Integer.parseInt("9300")));
		// } catch (NumberFormatException e) {
		// e.printStackTrace();
		// } catch (UnknownHostException e) {
		// e.printStackTrace();
		// }
		return client;
	}

	public void testInfo() {
		List<DiscoveryNode> nodes = getClient().connectedNodes();
		for (DiscoveryNode node : nodes) {
			System.out.println(node.getHostAddress());
		}
	}

	/**
	 * get API 获取指定文档信息
	 */
	public void testGet() {
		// GetResponse response = client.prepareGet("twitter", "tweet", "1")
		// .get();
		GetResponse response = getClient().prepareGet("twitter", "tweet", "1").setOperationThreaded(false) // 线程安全
				.get();
		System.out.println(response.getSourceAsString());
	}

}
