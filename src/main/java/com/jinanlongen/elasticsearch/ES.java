package com.jinanlongen.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * 
 * IndexResponse indexResponse = client.prepareIndex("test",
 * "info","100").setSource(infoMap).execute().actionGet(); GetResponse response
 * = client.prepareGet("sxq", "user", "2") .execute().actionGet();
 * 
 * 
 * @author shangyao
 * @date 2017年11月18日
 */
public class ES {

	public static void main(String[] args) {
		// GetResponse response = getClient().prepareGet("test", "info",
		// "100").execute().actionGet();
		GetResponse response = getClient().prepareGet("roc", "parent_ware", "914024").execute().actionGet();
		System.out.println("response.getId():" + response.getId());
		System.out.println("response.getSourceAsString():" + response.getSourceAsString());
	}

	public static TransportClient getClient() {
		TransportClient client;

		Settings esSettings = Settings.builder().put("cluster.name", "production_roc") // 设置ES实例的名称
				.put("client.transport.sniff", true) // 自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
				.build();
		client = new PreBuiltTransportClient(esSettings);// 初始化client较老版本发生了变化，此方法有几个重载方法，初始化插件等。
		// 此步骤添加IP，至少一个，其实一个就够了，因为添加了自动嗅探配置
		try {
			client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.200.110"), 9300));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return client;

	}

	public void queryByFilter(TransportClient client, String index, String type) {
		QueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("list_price").gt(50);
		SearchResponse searchResponse = client.prepareSearch(index).setTypes(type).setQuery(rangeQueryBuilder)
				/* .addSort("age", SortOrder.DESC) */.setSize(20).execute().actionGet();
		SearchHits hits = searchResponse.getHits();
		System.out.println("查到记录数：" + hits.getTotalHits());
		SearchHit[] searchHists = hits.getHits();
		if (searchHists.length > 0) {
			for (SearchHit hit : searchHists) {
				String name = (String) hit.getSource().get("name");
				Integer age = (Integer) hit.getSource().get("age");
				System.out.format("name:%s ,age :%d \n", name, age);
			}
		}
	}

}
