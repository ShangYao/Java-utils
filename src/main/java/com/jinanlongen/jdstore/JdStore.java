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
		// jd.queryBrand();
		// jd.findAttrById();
		// jd.get();
		// long id = 9772;
		long id = 1345;
		jd.findAttrsByCategoryId(id);
		// jd.findValuesByIdJos(id);
		// jd.findValuesByAttrIdJos(id);
		// jd.getSellerC();
	}
}
