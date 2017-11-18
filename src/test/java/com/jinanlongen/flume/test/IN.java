package com.jinanlongen.flume.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jinanlongen.flume.interceptors.GetMap;
import com.jinanlongen.flume.interceptors.GetProp;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年11月9日
 */
public class IN {
	public JSONObject intercept(String origBody, String basename) {
		String[] body_arr = origBody.split(",");
		if (!GetProp.getARTDBprops().containsKey(body_arr[1]) || !GetProp.getARTDBprops().containsKey(body_arr[2])
				|| !GetProp.getARTDBprops().containsKey(body_arr[3])) {
			System.out.println("!GetProp.getprops().containsKey(body_arr[1]");
			errofile(basename, origBody);
			return null;
		}
		if (body_arr.length < 29) {
			System.out.println("body_arr.length=" + body_arr.length);
			errofile(basename, origBody);
			return null;
		}
		String gender = GetMap.sexmap.get(body_arr[15]);
		String brand = GetMap.brandmap.get(body_arr[6]);
		String category = GetMap.taxonmap.get(body_arr[5]);

		System.out.println(gender);
		System.out.println(brand);
		System.out.println(category);

		if (gender == null || brand == null || category == null) {
			errofile(basename, origBody);
			return null;
		}

		JSONObject obj = new JSONObject();

		JSONObject trace_info = new JSONObject();
		trace_info.put("app", "flume");
		trace_info.put("file_name", basename);

		obj.put("command", "explore");
		obj.put("trace", trace_info);

		JSONObject items_info = getItems(body_arr);
		JSONArray items_arry = new JSONArray().put(items_info);
		obj.put("items", items_arry);
		return obj;

	}

	public void errofile(String basename, String origBody) {
		String filename = GetProp.getARTDBprops().getString("errofilefold") + "erro-" + basename;
		File file = new File(filename);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file, true);
			out.write(origBody.getBytes());
			out.write("\r\n".getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public JSONObject getItems(String[] body_arr) {
		JSONObject items_info = new JSONObject();
		String titleid = DigestUtils.md5Hex(body_arr[12]);
		String descid = DigestUtils.md5Hex(body_arr[13]);
		String feastr = body_arr[16] + body_arr[17] + body_arr[18] + body_arr[19] + body_arr[20] + body_arr[21]
				+ body_arr[22] + body_arr[23];
		String featureid = DigestUtils.md5Hex(feastr);

		items_info.put("item_type", "spu");
		items_info.put("roc_id", getSpuRocid(body_arr));
		items_info.put("item_url", "");
		items_info.put("item_timestamp", "timestamp");
		items_info.put("item_status_code", "NORMAL");
		items_info.put("meta", getmeta(body_arr));// 四元组
		items_info.put("albums", getAlbums(body_arr));
		items_info.put("bullets", getBullets(body_arr, titleid, descid, featureid));
		items_info.put("dimentions", getDimentions(body_arr));
		items_info.put("items", new JSONArray().put(getItem(body_arr, titleid, descid, featureid)));

		return items_info;

	}

	public String getSpuRocid(String[] body_arr) {
		if (body_arr[1] == "") {
			return "ARTD_B#" + body_arr[2];
		} else {
			return "ARTD_B#" + body_arr[1];
		}

	}

	public String getSkuRocid(String[] body_arr) {
		if (body_arr[1] == "") {
			return "ARTD_B#" + body_arr[2] + "#" + body_arr[2];
		} else {
			return "ARTD_B#" + body_arr[1] + "#" + body_arr[2];
		}

	}

	public JSONObject getmeta(String[] body_arr) {
		JSONObject meta_info = new JSONObject();
		meta_info.put("source_site_code", "ARTD_B");
		meta_info.put("all_skus", "false");
		meta_info.put("gender", GetMap.sexmap.get(body_arr[15]));
		meta_info.put("brand", GetMap.brandmap.get(body_arr[6]));
		meta_info.put("category", GetMap.taxonmap.get(body_arr[5]));

		return meta_info;

	}

	public JSONObject getAlbums(String[] body_arr) {
		JSONObject albums_info = new JSONObject();
		JSONArray albums_arry = new JSONArray();
		albums_arry.put(new JSONObject().put("url", body_arr[24]));
		albums_arry.put(new JSONObject().put("url", body_arr[25]));
		albums_arry.put(new JSONObject().put("url", body_arr[26]));
		albums_info.put("blue", albums_arry);
		return albums_info;

	}

	public JSONObject getBullets(String[] body_arr, String titleid, String descid, String featureid) {
		JSONObject bullets_info = new JSONObject();
		JSONObject title_info = new JSONObject();
		title_info.put(titleid, body_arr[12]);
		bullets_info.put("title", title_info);

		JSONObject description_info = new JSONObject();
		description_info.put(descid, body_arr[13]);
		bullets_info.put("description", description_info);

		JSONObject features_info = new JSONObject();
		JSONArray fea = new JSONArray();
		fea.put(body_arr[16]);
		fea.put(body_arr[17]);
		fea.put(body_arr[18]);
		fea.put(body_arr[19]);
		fea.put(body_arr[20]);
		fea.put(body_arr[21]);
		fea.put(body_arr[22]);
		fea.put(body_arr[23]);
		features_info.put(featureid, fea);
		bullets_info.put("features", features_info);// 16-23

		JSONObject spec_info = new JSONObject();
		JSONObject spec = new JSONObject();
		spec_info.put(DigestUtils.md5Hex(""), spec);
		bullets_info.put("spec", spec_info);
		return bullets_info;

	}

	public JSONObject getDimentions(String[] body_arr) {
		JSONObject dimentions_info = new JSONObject();
		JSONObject color_info = new JSONObject();
		color_info.put("1", new JSONObject().put("value", body_arr[14]));

		JSONObject size_info = new JSONObject();
		JSONObject size = new JSONObject();
		size.put("key", "");
		size.put("value", body_arr[28]);
		size_info.put("1", size);

		JSONObject width_info = new JSONObject();
		JSONObject width = new JSONObject();
		width.put("key", "");
		width.put("value", "");
		width_info.put("1", width);

		dimentions_info.put("color", color_info);
		dimentions_info.put("size", size_info);
		dimentions_info.put("width", width_info);
		return dimentions_info;

	}

	public JSONObject getItem(String[] body_arr, String titleid, String descid, String featureid) {
		JSONObject item_info = new JSONObject();
		item_info.put("item_type", "sku");
		item_info.put("roc_id", getSkuRocid(body_arr));
		item_info.put("item_url", "");
		item_info.put("item_timestamp", "timestamp");
		item_info.put("item_status_code", "NORMAL");
		item_info.put("meta", "");
		item_info.put("steady_info", getSteady(body_arr, titleid, descid, featureid));
		item_info.put("dyno_info", getDyno(body_arr));
		return item_info;

	}

	public JSONObject getSteady(String[] body_arr, String titleid, String descid, String featureid) {
		JSONObject stedy_info = new JSONObject();
		stedy_info.put("album", "blue");
		stedy_info.put("upc", body_arr[3]);
		stedy_info.put("ASIN", "");
		stedy_info.put("style_feature", "");
		stedy_info.put("mpn", body_arr[0]);
		stedy_info.put("ean", body_arr[4]);
		stedy_info.put("model_number", "");
		stedy_info.put("part_number", "");
		stedy_info.put("condition", "");

		stedy_info.put("bullets", getBullet(body_arr, titleid, descid, featureid));
		stedy_info.put("dimentions", getDimention());

		return stedy_info;
	}

	public JSONObject getDimention() {
		JSONObject dimention_info = new JSONObject();
		dimention_info.put("color", "1");
		dimention_info.put("size", "1");
		dimention_info.put("width", "1");
		return dimention_info;

	}

	public JSONObject getBullet(String[] body_arr, String titleid, String descid, String featureid) {
		JSONObject bullet_info = new JSONObject();
		bullet_info.put("title_id", titleid);
		bullet_info.put("description_id", descid);
		bullet_info.put("features_id", featureid);
		bullet_info.put("spec_id", DigestUtils.md5Hex(""));
		return bullet_info;

	}

	public JSONObject getDyno(String[] body_arr) {
		JSONObject dyno_info = new JSONObject();
		dyno_info.put("price", body_arr[10]);
		dyno_info.put("list_price", body_arr[11]);
		dyno_info.put("avilability", "true");
		dyno_info.put("availability_reason", "");
		dyno_info.put("stock", body_arr[27]);
		return dyno_info;
	}
}
