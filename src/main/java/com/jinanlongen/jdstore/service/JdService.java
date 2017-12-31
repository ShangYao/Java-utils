package com.jinanlongen.jdstore.service;
/**  
* jd api
*  
* @author shangyao  
* @date 2017年11月14日  
*/

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.Prop;
import com.jd.open.api.sdk.request.category.CategorySearchRequest;
import com.jd.open.api.sdk.request.imgzone.ImgzonePictureDeleteRequest;
import com.jd.open.api.sdk.request.imgzone.ImgzonePictureQueryRequest;
import com.jd.open.api.sdk.request.imgzone.ImgzonePictureUploadRequest;
import com.jd.open.api.sdk.request.list.CategoryReadFindAttrByIdRequest;
import com.jd.open.api.sdk.request.list.CategoryReadFindAttrsByCategoryIdRequest;
import com.jd.open.api.sdk.request.list.CategoryReadFindValuesByAttrIdJosRequest;
import com.jd.open.api.sdk.request.list.CategoryReadFindValuesByIdJosRequest;
import com.jd.open.api.sdk.request.list.CategoryReadFindValuesByIdRequest;
import com.jd.open.api.sdk.request.list.PopVenderCenerVenderBrandQueryRequest;
import com.jd.open.api.sdk.request.sellercat.SellerCatsGetRequest;
import com.jd.open.api.sdk.request.ware.PriceWriteUpdateSkuJdPriceRequest;
import com.jd.open.api.sdk.request.ware.PriceWriteUpdateWareMarketPriceRequest;
import com.jd.open.api.sdk.request.ware.SkuReadFindSkuByIdRequest;
import com.jd.open.api.sdk.request.ware.SkuReadSearchSkuListRequest;
import com.jd.open.api.sdk.request.ware.SkuWriteDeleteSkuRequest;
import com.jd.open.api.sdk.request.ware.WareReadFindWareByIdRequest;
import com.jd.open.api.sdk.request.ware.WareWriteUpOrDownRequest;
import com.jd.open.api.sdk.request.ware.WareWriteUpdateWareSaleAttrvalueAliasRequest;
import com.jd.open.api.sdk.response.category.CategorySearchResponse;
import com.jd.open.api.sdk.response.imgzone.ImgzonePictureDeleteResponse;
import com.jd.open.api.sdk.response.imgzone.ImgzonePictureQueryResponse;
import com.jd.open.api.sdk.response.imgzone.ImgzonePictureUploadResponse;
import com.jd.open.api.sdk.response.list.CategoryReadFindAttrByIdResponse;
import com.jd.open.api.sdk.response.list.CategoryReadFindAttrsByCategoryIdResponse;
import com.jd.open.api.sdk.response.list.CategoryReadFindValuesByAttrIdJosResponse;
import com.jd.open.api.sdk.response.list.CategoryReadFindValuesByIdJosResponse;
import com.jd.open.api.sdk.response.list.CategoryReadFindValuesByIdResponse;
import com.jd.open.api.sdk.response.list.PopVenderCenerVenderBrandQueryResponse;
import com.jd.open.api.sdk.response.sellercat.SellerCatsGetResponse;
import com.jd.open.api.sdk.response.ware.PriceWriteUpdateSkuJdPriceResponse;
import com.jd.open.api.sdk.response.ware.PriceWriteUpdateWareMarketPriceResponse;
import com.jd.open.api.sdk.response.ware.SkuReadFindSkuByIdResponse;
import com.jd.open.api.sdk.response.ware.SkuReadSearchSkuListResponse;
import com.jd.open.api.sdk.response.ware.SkuWriteDeleteSkuResponse;
import com.jd.open.api.sdk.response.ware.WareReadFindWareByIdResponse;
import com.jd.open.api.sdk.response.ware.WareWriteUpOrDownResponse;
import com.jd.open.api.sdk.response.ware.WareWriteUpdateWareSaleAttrvalueAliasResponse;

public class JdService {
	public static final String SERVER_URL = "https://api.jd.com/routerjson";
	public static final String accessToken = "4df49cc3-8034-48e8-a5a3-70b5072c4117";
	public static final String appKey = "1B679CD46420FE1324FCE5ABD71A22F1";
	public static final String appSecret = "9d47da9c095f4d469fd1a9ccdb1b2469";
	public static final JdClient client = new DefaultJdClient(SERVER_URL, accessToken, appKey, appSecret);

	// 获取类目属性
	public void findAttrById(Long id) {
		String field = "categoryAttr";
		CategoryReadFindAttrByIdRequest req = new CategoryReadFindAttrByIdRequest();
		req.setAttrId(id);
		req.setField(field);
		try {
			CategoryReadFindAttrByIdResponse response = client.execute(req);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}

	}

	// 获取类目属性
	public void findAttrById() {
		String field = "categoryAttr,categoryAttrId,categoryId,attName,attrIndexId,attrValueFeatures,categoryAttrGroup";
		CategoryReadFindAttrByIdRequest req = new CategoryReadFindAttrByIdRequest();
		req.setField(field);
		try {
			CategoryReadFindAttrByIdResponse response = client.execute(req);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}

	}

	// 查询商家已授权的品牌
	public void queryBrand() {
		PopVenderCenerVenderBrandQueryRequest request = new PopVenderCenerVenderBrandQueryRequest();
		// request.setName("jingdong");

		try {
			PopVenderCenerVenderBrandQueryResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}

	}

	// 获取类目属性列表
	public void findAttrsByCategoryId(Long id) {
		CategoryReadFindAttrsByCategoryIdRequest request = new CategoryReadFindAttrsByCategoryIdRequest();
		request.setCid(id);
		request.setField("categoryAttrs");
		try {
			CategoryReadFindAttrsByCategoryIdResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}

	}

	// 获取类目属性值
	public void findValuesByIdJos(Long id) {
		CategoryReadFindValuesByIdJosRequest request = new CategoryReadFindValuesByIdJosRequest();
		request.setId(id);
		request.setField("categoryAttrValue ");
		try {
			CategoryReadFindValuesByIdJosResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}

	}

	// 获取shop类目
	public void getSellerC() {
		SellerCatsGetRequest request = new SellerCatsGetRequest();

		try {
			SellerCatsGetResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 获取类目属性值liebiao
	public void findValuesByAttrIdJos(Long id) {
		CategoryReadFindValuesByAttrIdJosRequest request = new CategoryReadFindValuesByAttrIdJosRequest();
		CategoryReadFindValuesByAttrIdJosResponse response = null;
		request.setCategoryAttrId(id);
		// request.setField("jingdong,yanfa,pop");

		try {
			response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 获取类目属性值
	public void findValuesById(Long id) {
		CategoryReadFindValuesByIdRequest request = new CategoryReadFindValuesByIdRequest();

		request.setId(id);
		// request.setField("jingdong,yanfa,pop");

		try {
			CategoryReadFindValuesByIdResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 删除图片
	public void delete(String id) {
		ImgzonePictureDeleteRequest request = new ImgzonePictureDeleteRequest();

		request.setPictureIds(id);

		try {
			ImgzonePictureDeleteResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}

	}

	// 传单张图片
	public void upload(byte[] pic, Long id, String name) {
		ImgzonePictureUploadRequest request = new ImgzonePictureUploadRequest();

		request.setImageData(pic);// 图片二进制文件流，允许png、jpg、gif、jpeg、bmp图片格式，1M以内。
		request.setPictureCateId(id);// 上传到的图片分类ID，为空上传至 默认分类
		request.setPictureName(name);// 图片名称，不超过64字节，为空默认 未命名

		try {
			ImgzonePictureUploadResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}
	}

	// 查询图片
	public void query(Long id) {
		ImgzonePictureQueryRequest request = new ImgzonePictureQueryRequest();
		request.setPictureId("jingdong");// 图片ID
		request.setPictureCateId(123);// 图片所属分类ID
		request.setPictureName("jingdong");// 图片名称，支持后模糊查询
		// request.setEndDate("2012-12-12 12:12:12");//结束创建时间
		// request.setStartDate("2012-12-12 12:12:12");//创建开始时间
		request.setPageNum(123);// 页码值，对应分页结果页数，为空或非正整数时默认为1，超过最大页数返回空
		request.setPageSize(123);// 每页条数，为空或非正整数时默认为20，最多返回100条

		try {
			ImgzonePictureQueryResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}
	}

	// 查询单个商品
	public void findWaresbyId(Long id) {
		WareReadFindWareByIdRequest request = new WareReadFindWareByIdRequest();

		request.setWareId(id);
		request.setField("ware,title,adWords,logo");

		try {
			WareReadFindWareByIdResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}
	}

	// 获取商家类目信息
	public void get() {
		CategorySearchRequest request = new CategorySearchRequest();
		// request.setFields("id,fid,status,lev,name,index_id ");
		try {
			CategorySearchResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}

	}

	// SKU搜索服务
	public void searchSkuList(String sku) {
		SkuReadSearchSkuListRequest request = new SkuReadSearchSkuListRequest();

		// request.setWareId("1960141036");
		request.setSkuId(sku);
		// request.setSkuStatuValue("123,234,345");// SKU状态：1:上架 2:下架 4:删除
		// request.setMaxStockNum((long) 123);
		// request.setMinStockNum((long) 123);
		// request.setEndCreatedTime("2012-12-12 12:12:12");
		// request.setEndModifiedTime("2012-12-12 12:12:12");
		// request.setStartCreatedTime("2012-12-12 12:12:12");
		// request.setStartModifiedTime("2012-12-12 12:12:12");
		// request.setOutId("jingdong,yanfa,pop");
		// request.setColType(123);// 合作类型
		// request.setItemNum("jingdong");// 货号
		// request.setWareTitle("jingdong");// 商品名称
		// request.setOrderFiled("jingdong,yanfa,pop");// 排序字段.目前支持skuId、stockNum
		// request.setOrderType("jingdong,yanfa,pop");// 排序类型：asc、desc
		// request.setPageNo(123);
		// request.setPageSize(123);
		// request.setField("outerId");

		try {

			SkuReadSearchSkuListResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}

	}

	// 删除sku
	public void deleteSku(Long id) {
		SkuWriteDeleteSkuRequest request = new SkuWriteDeleteSkuRequest();

		request.setSkuId(id);

		try {
			SkuWriteDeleteSkuResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}

	}

	// 查询sku
	public void findSkuById(Long id) {
		SkuReadFindSkuByIdRequest request = new SkuReadFindSkuByIdRequest();

		request.setSkuId(id);
		request.setField("wareTitle,created,features");

		try {
			SkuReadFindSkuByIdResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}
	}

	// 商品上下架
	public void upOrDown(Long id) {
		WareWriteUpOrDownRequest request = new WareWriteUpOrDownRequest();
		request.setNote("jingdong");// 原因
		request.setWareId(id);
		request.setOpType(123);// 操作类型：1表示上架 2表示下架

		try {
			WareWriteUpOrDownResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}

	}

	// 更新商品市场价
	public void updateWareMarketPrice(Long id, BigDecimal price) {
		PriceWriteUpdateWareMarketPriceRequest request = new PriceWriteUpdateWareMarketPriceRequest();
		request.setWareId(id);
		request.setMarketPrice(price);
		try {
			PriceWriteUpdateWareMarketPriceResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}

	}

	// 更新sku京东价
	public void updateSkuJdPrice(Long id, BigDecimal price) {
		PriceWriteUpdateSkuJdPriceRequest request = new PriceWriteUpdateSkuJdPriceRequest();

		request.setJdPrice(price);
		request.setSkuId(id);

		try {
			PriceWriteUpdateSkuJdPriceResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			e.printStackTrace();
		}

	}

	// 更新商品维度的销售属性值别名
	public void updateWareSaleAttrvalueAlias(Long id, String[] attr, String[] value) {
		WareWriteUpdateWareSaleAttrvalueAliasRequest request = new WareWriteUpdateWareSaleAttrvalueAliasRequest();
		Prop prop = new Prop();
		prop.setAttrId("");
		prop.setAttrValues(attr);
		Set<Prop> props = new HashSet<>();
		props.add(prop);
		request.setProps(props);
		request.setWareId(id);

		try {
			WareWriteUpdateWareSaleAttrvalueAliasResponse response = client.execute(request);
			System.out.println(response.getMsg());
		} catch (JdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
