package com.jinanlongen.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年11月15日
 */
public class ESTest {
	TransportClient transportClient;
	// 索引库名
	String index = "roc";
	// 类型名称
	String type = "wares";

	public void before() {
		/**
		 * 1:通过 setting对象来指定集群配置信息
		 */
		Settings setting = ImmutableSettings.settingsBuilder().put("cluster.name", "production_roc")// 指定集群名称
				// .put("client.transport.sniff", true)// 启动嗅探功能
				.build();

		/**
		 * 2：创建客户端 通过setting来创建，若不指定则默认链接的集群名为elasticsearch 链接使用tcp协议即9300
		 */
		transportClient = new TransportClient(setting);
		TransportAddress transportAddress = new InetSocketTransportAddress("192.168.200.110", 9200);
		transportClient.addTransportAddresses(transportAddress);

		/**
		 * 3：查看集群信息 注意我的集群结构是： 131的elasticsearch.yml中指定为主节点不能存储数据，
		 * 128的elasticsearch.yml中指定不为主节点只能存储数据。 所有控制台只打印了192.168.79.128,只能获取数据节点
		 * 
		 */
		ImmutableList<DiscoveryNode> connectedNodes = transportClient.connectedNodes();
		for (DiscoveryNode node : connectedNodes) {
			System.out.println(node.getHostAddress());
		}

	}
}
