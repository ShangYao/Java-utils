package com.jinanlongen.flume.interceptors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.base.Charsets;

public class COSMOInterceptor implements Interceptor {
	private final String[] titles;
	private final Charset charset;

	private COSMOInterceptor(String titles, Charset charset) {
		this.titles = titles.split(",");
		this.charset = charset;
	}

	@Override
	public void close() {
	}

	@Override
	public void initialize() {
	}

	@Override
	public Event intercept(Event event) {
		byte[] eventBody = event.getBody();
		String origBody = new String(eventBody, this.charset);
		origBody = origBody.replaceAll("\\t(\\r\\n|\\r|\\n)", " ");
		String[] body_arr = origBody.split("\\t");
		JSONObject obj = new JSONObject();
		Integer titles_len = Integer.valueOf(this.titles.length);
		Integer body_len = Integer.valueOf(body_arr.length);

		Map<String, String> headers = event.getHeaders();
		String basename = headers.get("basename");

		for (int meta_info = 0; meta_info < titles_len.intValue(); meta_info++) {
			if (meta_info < body_len.intValue()) {
				obj.put(this.titles[meta_info], body_arr[meta_info]);
			} else {
				obj.put(this.titles[meta_info], "");
			}
		}
		// 错误四元组信息处理
		if (!GetProp.getCOSMOprops().containsKey(obj.getString(this.titles[6]))
				|| !GetProp.getCOSMOIDprops().containsKey(obj.getString(this.titles[6]))) {
			erro(obj.getString(this.titles[6]), "Brand");
			errofile(basename, origBody, "A-");
			event.setBody("".getBytes());
			System.out.println("brand erro");
			return event;
		}
		if (!GetProp.getCOSMOprops().containsKey(obj.getString(this.titles[1]))
				|| !GetProp.getCOSMOIDprops().containsKey(obj.getString(this.titles[1]))) {
			erro(obj.getString(this.titles[1]), "Taxon");
			errofile(basename, origBody, "A-");
			event.setBody("".getBytes());
			System.out.println("Taxon erro");
			return event;
		}

		JSONObject eventObj = new JSONObject();

		eventObj.put("version", "1.0.0");
		eventObj.put("command", "DISCOVER");
		eventObj.put("trace", getTrace(basename));
		eventObj.put("items", getItems(obj, headers));
		eventObj.put("references", getreference(obj, headers));
		byte[] arg17 = eventObj.toString().getBytes();

		event.setBody(arg17);
		return event;
	}

	public JSONArray getreference(JSONObject obj, Map<String, String> headers) {
		JSONArray reference_arry = new JSONArray();

		reference_arry.put(getAlbum(obj));

		if (isNotBlank(obj.getString(this.titles[12]))) {
			reference_arry.put(getTitle(obj));

		}
		if (isNotBlank(obj.getString(this.titles[13])) || isNotBlank(obj.getString(this.titles[14]))) {
			reference_arry.put(getDesc(obj));

		}
		reference_arry.put(getMainImage(obj));
		// reference_arry.put(getlocalimage(obj, "local2"));
		// reference_arry.put(getlocalimage(obj, "local3"));
		// reference_arry.put(getlocalimage(obj, "local4"));
		// reference_arry.put(getlocalimage(obj, "local5"));
		// reference_arry.put(getlocalimage(obj, "local6"));
		String str = "";
		for (int a = 19; a < 39; a++) {
			if (isNotBlank(obj.getString(titles[a]))) {
				str += obj.getString(titles[a]);

			}
		}
		if (isNotBlank(str)) {

			reference_arry.put(getFeature(obj));
		}
		reference_arry.put(getSpec(obj));

		reference_arry.put(getsku(obj, headers));
		return reference_arry;

	}

	public boolean isNotBlank(String str) {
		return str != null && !"".equals(str) && !" ".equals(str);
	}

	public JSONObject getMainImage(JSONObject obj) {
		JSONObject image = new JSONObject();

		image.put("item_type", "Image");
		image.put("item_id", getSpuRocid(obj) + "::::main");

		String name = obj.getString(this.titles[0]);
		String url = "";
		if (name.contains("-")) {
			url = "http://www.cosmopolitanusa.com/pricelist/img/" + name.substring(0, name.indexOf("-")) + ".jpg";
		} else {
			url = "http://www.cosmopolitanusa.com/pricelist/img/" + name + ".jpg";
		}
		image.put("url", url);
		image.put("init", new Boolean("true"));

		image.put("path", "/cosmo/" + DigestUtils.md5Hex(url) + ".jpg");
		image.put("origin", new Boolean("true"));
		image.put("type", "main");
		return image;
	}

	// 占位图片
	public JSONObject getlocalimage(JSONObject obj, String str) {
		JSONObject localimage = new JSONObject();
		localimage.put("item_type", "Image");
		localimage.put("item_id", getSpuRocid(obj) + "::::" + str);
		localimage.put("url", "http://192.168.200.71:8888/image/0e76292794888d4f1fa75fb3aff4ca27c58f56a6.jpg");
		localimage.put("init", new Boolean("false"));

		localimage.put("path",
				"/cosmo/"
						+ DigestUtils
								.md5Hex("http://192.168.200.71:8888/image/0e76292794888d4f1fa75fb3aff4ca27c58f56a6.jpg")
						+ ".jpg");
		localimage.put("origin", new Boolean("false"));
		localimage.put("type", str);
		return localimage;

	}

	// sku
	public JSONObject getsku(JSONObject obj, Map<String, String> headers) {
		JSONObject sku_info = new JSONObject();
		sku_info.put("item_type", "Sku");
		sku_info.put("item_id", getSkuRocid(obj));
		sku_info.put("meta", getSkumeta(headers));
		sku_info.put("steady_info", getsteady(obj));
		sku_info.put("dyno_info", getdyno(obj));
		return sku_info;

	}

	public JSONObject getdyno(JSONObject obj) {
		JSONObject info = new JSONObject();
		info.put("currency", "USD");
		info.put("list_price", stringToDouble((String) obj.get(this.titles[15])));
		info.put("price", stringToDouble((String) obj.get(this.titles[16])));
		info.put("stock", stringToInt((String) obj.get(this.titles[17])));
		info.put("availability", new Boolean("true"));
		info.put("availability_reason", "");
		return info;

	}

	public Double stringToDouble(String str) {

		double i = Double.parseDouble(str);
		return i;

	}

	public JSONObject getsteady(JSONObject obj) {
		JSONObject stead_info = new JSONObject();

		stead_info.put("album", getSkuAlbum(obj));

		stead_info.put("bullets", getbullts(obj));

		stead_info.put("upc", obj.get(this.titles[8]));
		stead_info.put("mpn", "");
		stead_info.put("ean", "");
		stead_info.put("part_number", "");
		stead_info.put("model_number", "");
		String condition = "";
		if (!isNotBlank(obj.getString(titles[5]))) {
			condition = "new";
		} else if ("NO Batch".equals(obj.getString(titles[5]))) {
			condition = "certified";
		} else if ("Decoded".equals(obj.getString(titles[5]))) {
			condition = "certified";
		} else if ("(Limit)Decoded".equals(obj.getString(titles[5]))) {
			condition = "certified";
		} else {
			condition = "special";
		}
		stead_info.put("condition", condition);

		return stead_info;

	}

	public JSONArray getbullts(JSONObject obj) {
		JSONArray info = new JSONArray();

		if (isNotBlank(obj.getString(this.titles[12]))) {
			info.put(bullttitle(obj));

		}
		if (isNotBlank(obj.getString(this.titles[13])) || isNotBlank(obj.getString(this.titles[14]))) {
			info.put(bulltdesc(obj));

		}
		String str = "";
		for (int a = 19; a < 39; a++) {
			if (isNotBlank(obj.getString(titles[a]))) {
				str += obj.getString(titles[a]);

			}
		}
		if (isNotBlank(str)) {

			info.put(bulltfeature(obj));
		}
		info.put(bulltspec(obj));

		return info;

	}

	public JSONObject bulltspec(JSONObject obj) {
		JSONObject info = new JSONObject();
		info.put("item_type", "Bullet::Specs");
		info.put("item_id", getSpec(obj).get("item_id"));
		return info;

	}

	public JSONObject bulltfeature(JSONObject obj) {
		JSONObject info = new JSONObject();
		info.put("item_type", "Bullet::Feature");
		info.put("item_id", getFeature(obj).get("item_id"));
		return info;

	}

	public JSONObject bulltdesc(JSONObject obj) {
		JSONObject info = new JSONObject();
		info.put("item_type", "Bullet::Desc");
		info.put("item_id", getSpuRocid(obj) + "::"
				+ DigestUtils.md5Hex(obj.getString(this.titles[13]) + obj.getString(this.titles[14])).substring(0, 7));
		return info;

	}

	public JSONObject bullttitle(JSONObject obj) {
		JSONObject info = new JSONObject();
		info.put("item_type", "Bullet::Title");
		info.put("item_id",
				getSpuRocid(obj) + "::" + DigestUtils.md5Hex(obj.getString(this.titles[12])).substring(0, 7));
		return info;

	}

	public JSONObject getSkuAlbum(JSONObject obj) {
		JSONObject album_info = new JSONObject();
		album_info.put("item_type", "Album");
		album_info.put("item_id", getSpuRocid(obj) + "::");
		return album_info;

	}

	public JSONObject getSkumeta(Map<String, String> headers) {
		JSONObject info = new JSONObject();
		info.put("timestamp", stringToLong(headers.get("timestamp")));
		return info;

	}

	// feature
	public JSONObject getFeature(JSONObject obj) {
		JSONObject info = new JSONObject();
		info.put("item_type", "Bullet::Features");
		String str = "";
		JSONArray fea = new JSONArray();
		for (int a = 19; a < 39; a++) {
			if (isNotBlank(obj.getString(this.titles[a]))) {
				str += obj.getString(this.titles[a]);
				fea.put(obj.getString(this.titles[a]));

			}
		}
		info.put("item_id", getSpuRocid(obj) + "::" + DigestUtils.md5Hex(str).substring(0, 7));
		info.put("en", fea.toString());
		return info;

	}

	// Spec
	public JSONObject getSpec(JSONObject obj) {
		JSONObject info = new JSONObject();
		info.put("item_type", "Bullet::Specs");

		String[][] spec = { { "SIZE", obj.getString(this.titles[4]) }, { "WEIGHT", obj.getString(this.titles[11]) } };

		info.put("item_id", getSpuRocid(obj) + "::"
				+ DigestUtils.md5Hex(obj.getString(this.titles[4]) + obj.getString(this.titles[11])).substring(0, 7));
		info.put("en", spec);
		return info;

	}

	// 相册
	public JSONObject getAlbum(JSONObject obj) {
		JSONObject album_info = new JSONObject();
		album_info.put("item_type", "Album");
		album_info.put("item_id", getSpuRocid(obj) + "::");

		album_info.put("images", getAlbumImages(obj));
		return album_info;
	}

	public JSONArray getAlbumImages(JSONObject obj) {
		JSONArray album_arry = new JSONArray();

		album_arry.put(getImagesAlbum(obj, "main"));
		album_arry.put(getImagesAlbum(obj, "main"));
		album_arry.put(getImagesAlbum(obj, "main"));
		album_arry.put(getImagesAlbum(obj, "main"));
		album_arry.put(getImagesAlbum(obj, "main"));
		album_arry.put(getImagesAlbum(obj, "main"));

		return album_arry;
	}

	public JSONObject getImagesAlbum(JSONObject obj, String images) {
		JSONObject imagesAlbum = new JSONObject();
		imagesAlbum.put("item_type", "Image");
		imagesAlbum.put("item_id", getSpuRocid(obj) + "::::" + images);

		return imagesAlbum;

	}

	// 描述
	public JSONObject getDesc(JSONObject obj) {
		JSONObject desc = new JSONObject();
		desc.put("item_type", "Bullet::Desc");
		desc.put("item_id", getSpuRocid(obj) + "::"
				+ DigestUtils.md5Hex(obj.getString(this.titles[13]) + obj.getString(this.titles[14])).substring(0, 7));
		desc.put("en", obj.getString(this.titles[13]) + obj.getString(this.titles[14]));
		return desc;

	}

	// 标题
	public JSONObject getTitle(JSONObject obj) {
		JSONObject title = new JSONObject();
		title.put("item_type", "Bullet::Title");
		title.put("item_id",
				getSpuRocid(obj) + "::" + DigestUtils.md5Hex(obj.getString(this.titles[12])).substring(0, 7));
		title.put("en", obj.getString(this.titles[12]));
		return title;

	}

	public JSONArray getItems(JSONObject obj, Map<String, String> headers) {
		JSONObject items_info = getItem(obj, headers);
		JSONArray items_arry = new JSONArray().put(items_info);
		return items_arry;

	}

	public JSONObject getItem(JSONObject obj, Map<String, String> headers) {
		JSONObject items_info = new JSONObject();

		items_info.put("item_type", "Spu");
		items_info.put("item_id", getSpuRocid(obj));

		items_info.put("meta", getmeta(obj, headers));// 四元组
		items_info.put("skus", getskus(obj));

		return items_info;

	}

	public JSONArray getskus(JSONObject obj) {
		JSONObject skus_info = new JSONObject();

		skus_info.put("item_type", "Sku");
		skus_info.put("item_id", getSkuRocid(obj));
		JSONArray skus_arry = new JSONArray().put(skus_info);

		return skus_arry;

	}

	public JSONObject getmeta(JSONObject obj, Map<String, String> headers) {
		JSONObject meta_info = new JSONObject();
		meta_info.put("source_site_code", "COSM");
		meta_info.put("source_site_id", 90);
		meta_info.put("all_skus", new Boolean("false"));
		meta_info.put("gender_code", "unisex");
		meta_info.put("gender_id", 7);
		meta_info.put("brand_code", GetProp.getCOSMOprops().getString(obj.getString(this.titles[6])));
		meta_info.put("brand_id", stringToInt(GetProp.getCOSMOIDprops().getString(obj.getString(this.titles[6]))));
		meta_info.put("taxon_code", GetProp.getCOSMOprops().getString(this.titles[1]));
		meta_info.put("taxon_id", stringToInt(GetProp.getCOSMOIDprops().getString(obj.getString(this.titles[1]))));

		meta_info.put("url", "");
		meta_info.put("timestamp", stringToLong(headers.get("timestamp")));
		meta_info.put("status_code", "NORMAL");
		return meta_info;

	}

	public String getSkuRocid(JSONObject obj) {

		return "COSM#" + obj.getString(this.titles[0]) + "#" + obj.getString(this.titles[0]);

	}

	public String getSpuRocid(JSONObject obj) {

		return "COSM#" + obj.getString(this.titles[0]);

	}

	public Integer stringToInt(String str) {

		int i = Integer.parseInt(str);
		return i;

	}

	public Long stringToLong(String str) {

		Long i = Long.parseLong(str);
		return i;

	}

	public JSONObject getTrace(String basename) {

		JSONObject trace_info = new JSONObject();
		trace_info.put("app", "flume");
		trace_info.put("file_name", basename);

		return trace_info;

	}

	public void erro(String contex, String pref) {
		String filename = GetProp.getCOSMOprops().getString("errofilefold") + "cosmo-" + pref + ".txt";
		File file = new File(filename);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file, true);
			out.write(contex.getBytes());
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

	public void errofile(String basename, String origBody, String pref) {
		String filename = GetProp.getCOSMOprops().getString("errofilefold") + "cosmo-" + pref + basename;
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

	@Override
	public List<Event> intercept(List<Event> events) {
		Iterator<Event> var2 = events.iterator();

		while (var2.hasNext()) {
			Event event = var2.next();
			this.intercept(event);
		}

		return events;
	}

	public static class Constants {
		public static String TITLES = "titles";
	}

	public static class Builder implements Interceptor.Builder {
		private String titles = Constants.TITLES;
		private Charset charset = Charsets.UTF_8;

		@Override
		public void configure(Context context) {
			this.titles = context.getString(Constants.TITLES);
		}

		@Override
		public Interceptor build() {
			return new COSMOInterceptor(this.titles, this.charset);
		}
	}
}