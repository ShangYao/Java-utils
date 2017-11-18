package com.jinanlongen.jdstore;

import com.jinanlongen.jdstore.service.JdService;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年11月14日
 */
public class JdStore {
	public static void main(String[] args) {
		JdService jd = new JdService();
		// Long id = (long) 1960141036;
		Long sku = (long) 1985209215;
		// Long sku = (long) 20045455944;
		// jd.findAttrsByCategoryId(id);// 获取类目属性列表
		// jd.findValuesByIdJos(id);// 获取类目属性值
		// jd.findWaresbyId(id);
		// jd.query(id);
		// jd.searchSkuList(sku);
		jd.findSkuById(sku);
		// jd.get();
	}
}
