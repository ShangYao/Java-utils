package com.jinanlongen.flume.interceptors;
/**  
*   an interceptor for flume to deal with  file from ARTD_B,
*  this version adaptation to items.1.1.1.json
* @author shangyao  
* @date 2017年11月8日  
* @scope test
*/

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

public class ARTD_BInterceptor implements Interceptor {
	private final Charset charset;

	private ARTD_BInterceptor(Charset charset) {
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
		Map<String, String> headers = event.getHeaders();
		String origBody = new String(eventBody, this.charset);
		origBody = origBody.replaceAll("\\r\\n|\\r|\\n", " ");
		String[] body_arr = origBody.split(",");

		String basename = headers.get("basename");
		if (body_arr.length < 29) {
			errofile(basename, origBody, "L-");
			event.setBody("".getBytes());
			return event;
		}
		if (!GetProp.getARTDBprops().containsKey(body_arr[15])
				|| !GetProp.getARTDBIDprops().containsKey(body_arr[15])) {
			erro(body_arr[15], "Gender");
			errofile(basename, origBody, "A-");
			event.setBody("".getBytes());
			return event;
		}
		if (!GetProp.getARTDBprops().containsKey(body_arr[6]) || !GetProp.getARTDBIDprops().containsKey(body_arr[6])) {
			erro(body_arr[6], "Brand");
			errofile(basename, origBody, "A-");
			event.setBody("".getBytes());
			return event;
		}
		if (!GetProp.getARTDBprops().containsKey(body_arr[5]) || !GetProp.getARTDBIDprops().containsKey(body_arr[5])) {
			erro(body_arr[5], "Category");
			errofile(basename, origBody, "A-");
			event.setBody("".getBytes());
			return event;
		}

		JSONObject obj = new JSONObject();

		JSONObject trace_info = new JSONObject();
		trace_info.put("app", "flume");
		trace_info.put("file_name", basename);

		obj.put("version", "1.1.1");
		obj.put("command", "DISCOVER");
		obj.put("trace", trace_info);

		JSONObject items_info = getItems(body_arr, headers);
		JSONArray items_arry = new JSONArray().put(items_info);
		obj.put("items", items_arry);
		obj.put("references", getreference(body_arr, headers));

		byte[] modifiedEvent = obj.toString().getBytes();
		event.setBody(modifiedEvent);
		return event;
	}

	public void erro(String contex, String pref) {
		String filename = GetProp.getARTDBprops().getString("errofilefold") + "artdb-" + pref + ".txt";
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

	// size
	public JSONObject getdimention(String[] body_arr, int a, String str) {
		JSONObject info = new JSONObject();
		info.put("item_type", "Dimension::" + str);
		info.put("item_id", getSpuRocid(body_arr) + "::" + DigestUtils.md5Hex(body_arr[a]).substring(0, 7));
		info.put("key", "");
		info.put("value", body_arr[a]);
		return info;

	}

	public JSONObject getimage(String[] body_arr, int a, String str) {
		JSONObject image1_info = new JSONObject();
		image1_info.put("item_type", "Image");
		image1_info.put("item_id", getSpuRocid(body_arr) + "::Red::" + str);
		image1_info.put("url", body_arr[a]);
		image1_info.put("origin", new Boolean("true"));
		image1_info.put("type", " ");
		return image1_info;

	}

	public JSONArray getreference(String[] body_arr, Map<String, String> headers) {
		JSONArray reference_arry = new JSONArray();

		JSONObject width_info = new JSONObject();
		width_info.put("item_type", "Dimension::Width");
		width_info.put("item_id", getSpuRocid(body_arr) + "::" + DigestUtils.md5Hex("").substring(0, 7));
		width_info.put("key", "");
		width_info.put("value", "");

		JSONObject album_info = new JSONObject();
		album_info.put("item_type", "Album");
		album_info.put("item_id", getSpuRocid(body_arr) + "::Red");
		JSONArray album_arry = new JSONArray();
		JSONObject album1 = new JSONObject();
		album1.put("item_type", "Image");
		album1.put("item_id", getSpuRocid(body_arr) + "::Red::1");

		JSONObject album2 = new JSONObject();
		album2.put("item_type", "Image");
		album2.put("item_id", getSpuRocid(body_arr) + "::Red::2");

		JSONObject album3 = new JSONObject();
		album3.put("item_type", "Image");
		album3.put("item_id", getSpuRocid(body_arr) + "::Red::3");

		JSONObject title = new JSONObject();
		title.put("item_type", "Bullet::Title");
		title.put("item_id", getSpuRocid(body_arr) + "::" + DigestUtils.md5Hex(body_arr[12]).substring(0, 7));
		title.put("en", body_arr[12]);

		JSONObject desc = new JSONObject();
		desc.put("item_type", "Bullet::Desc");
		desc.put("item_id", getSpuRocid(body_arr) + "::" + DigestUtils.md5Hex(body_arr[13]).substring(0, 7));
		desc.put("en", body_arr[13]);

		album_arry.put(album1);
		album_arry.put(album2);
		album_arry.put(album3);
		album_info.put("images", album_arry);

		reference_arry.put(getdimention(body_arr, 14, "Color"));
		reference_arry.put(getdimention(body_arr, 28, "Size"));
		reference_arry.put(width_info);
		reference_arry.put(getimage(body_arr, 24, "1"));
		reference_arry.put(getimage(body_arr, 25, "2"));
		reference_arry.put(getimage(body_arr, 26, "3"));
		reference_arry.put(album_info);
		reference_arry.put(desc);
		reference_arry.put(title);

		reference_arry.put(getfeature(body_arr));
		reference_arry.put(getsku(body_arr, headers));
		return reference_arry;

	}

	public JSONObject getfeature(String[] body_arr) {
		JSONObject info = new JSONObject();
		info.put("item_type", "Bullet::Features");
		String str = body_arr[16] + body_arr[17] + body_arr[18] + body_arr[19] + body_arr[20] + body_arr[21]
				+ body_arr[22] + body_arr[23];
		info.put("item_id", getSpuRocid(body_arr) + "::" + DigestUtils.md5Hex(str).substring(0, 7));

		JSONArray fea = new JSONArray();
		fea.put(body_arr[16]);
		fea.put(body_arr[17]);
		fea.put(body_arr[18]);
		fea.put(body_arr[19]);
		fea.put(body_arr[20]);
		fea.put(body_arr[21]);
		fea.put(body_arr[22]);
		fea.put(body_arr[23]);
		info.put("en", fea.toString());
		return info;

	}

	public JSONObject getsku(String[] body_arr, Map<String, String> headers) {
		JSONObject sku_info = new JSONObject();
		sku_info.put("item_type", "Sku");
		sku_info.put("item_id", getSkuRocid(body_arr));
		sku_info.put("meta", getSkumeta(headers));
		sku_info.put("steady_info", getsteady(body_arr));
		sku_info.put("dyno_info", getdyno(body_arr));
		return sku_info;

	}

	public JSONObject getSkumeta(Map<String, String> headers) {
		JSONObject info = new JSONObject();
		info.put("timestamp", stringToLong(headers.get("timestamp")));
		return info;

	}

	public JSONObject getdyno(String[] body_arr) {
		JSONObject info = new JSONObject();
		info.put("currency", "USD");
		info.put("list_price", stringToDouble(body_arr[11]));
		info.put("price", stringToDouble(body_arr[10]));
		info.put("stock", stringToInt(body_arr[27]));
		info.put("availability", new Boolean("true"));
		info.put("availability_reason", "");
		return info;

	}

	public JSONObject getsteady(String[] body_arr) {
		JSONObject stead_info = new JSONObject();

		JSONArray dimention_arry = new JSONArray();
		dimention_arry.put(getskudimention(body_arr, 14, "Color"));
		dimention_arry.put(getskudimention(body_arr, 28, "Size"));
		stead_info.put("dimensions", dimention_arry);

		JSONObject album_info = new JSONObject();
		album_info.put("item_type", "Album");
		album_info.put("item_id", getSpuRocid(body_arr) + "::Red");
		stead_info.put("album", album_info);

		stead_info.put("bullets", getbullts(body_arr));

		stead_info.put("upc", body_arr[3]);
		stead_info.put("mpn", body_arr[0]);
		stead_info.put("ean", "");
		stead_info.put("part_number", "");
		stead_info.put("model_number", "");
		stead_info.put("condition", "");

		return stead_info;

	}

	public JSONArray getbullts(String[] body_arr) {
		JSONArray info = new JSONArray();
		info.put(bullt(body_arr, 12, "Title"));
		info.put(bullt(body_arr, 13, "Desc"));
		info.put(faeature(body_arr));

		return info;

	}

	public JSONObject faeature(String[] body_arr) {
		JSONObject info = new JSONObject();
		info.put("item_type", "Bullet::Features");
		String str = body_arr[16] + body_arr[17] + body_arr[18] + body_arr[19] + body_arr[20] + body_arr[21]
				+ body_arr[22] + body_arr[23];
		info.put("item_id", getSpuRocid(body_arr) + "::" + DigestUtils.md5Hex(str).substring(0, 7));

		return info;

	}

	public JSONObject bullt(String[] body_arr, int a, String str) {
		JSONObject info = new JSONObject();
		info.put("item_type", "Bullet::" + str);
		info.put("item_id", getSpuRocid(body_arr) + "::" + DigestUtils.md5Hex(body_arr[a]).substring(0, 7));

		return info;

	}

	public JSONObject getskudimention(String[] body_arr, int a, String str) {
		JSONObject info = new JSONObject();
		info.put("item_type", "Dimension::" + str);
		info.put("item_id", getSpuRocid(body_arr) + "::" + DigestUtils.md5Hex(body_arr[a]).substring(0, 7));

		return info;

	}

	public void errofile(String basename, String origBody, String pref) {
		String filename = GetProp.getARTDBprops().getString("errofilefold") + "artdb-" + pref + basename;
		File file = new File(filename);
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file, true);
			out.write(origBody.getBytes());
			// out.write("\r\n".getBytes());
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

	public JSONObject getItems(String[] body_arr, Map<String, String> headers) {
		JSONObject items_info = new JSONObject();

		items_info.put("item_type", "Spu");
		items_info.put("item_id", getSpuRocid(body_arr));

		items_info.put("meta", getmeta(body_arr, headers));// 四元组
		items_info.put("skus", getskus(body_arr));

		return items_info;

	}

	public JSONArray getskus(String[] body_arr) {
		JSONObject skus_info = new JSONObject();

		skus_info.put("item_type", "Sku");
		skus_info.put("item_id", getSkuRocid(body_arr));
		JSONArray skus_arry = new JSONArray().put(skus_info);

		return skus_arry;

	}

	public String getSpuRocid(String[] body_arr) {
		if ("".endsWith(body_arr[1])) {
			return "ARTD_B#" + body_arr[2];
		} else {
			return "ARTD_B#" + body_arr[1];
		}

	}

	public String getSkuRocid(String[] body_arr) {
		if ("".endsWith(body_arr[1])) {
			return "ARTD_B#" + body_arr[2] + "#" + body_arr[2];
		} else {
			return "ARTD_B#" + body_arr[1] + "#" + body_arr[2];
		}

	}

	public JSONObject getmeta(String[] body_arr, Map<String, String> headers) {
		JSONObject meta_info = new JSONObject();
		meta_info.put("source_site_code", "ARTD_B");
		meta_info.put("source_site_id", 117);
		meta_info.put("all_skus", new Boolean("false"));
		meta_info.put("gender", GetProp.getARTDBprops().getString(body_arr[15]));
		meta_info.put("gender_id", stringToInt(GetProp.getARTDBIDprops().getString(body_arr[15])));
		meta_info.put("brand", GetProp.getARTDBprops().getString(body_arr[6]));
		meta_info.put("brand_id", stringToInt(GetProp.getARTDBIDprops().getString(body_arr[6])));
		meta_info.put("taxon", GetProp.getARTDBprops().getString(body_arr[5]));
		meta_info.put("taxon_id", stringToInt(GetProp.getARTDBIDprops().getString(body_arr[5])));

		meta_info.put("url", "");
		meta_info.put("timestamp", stringToLong(headers.get("timestamp")));
		meta_info.put("status_code", "NORMAL");
		return meta_info;

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

	public Integer stringToInt(String str) {

		int i = Integer.parseInt(str);
		return i;

	}

	public Double stringToDouble(String str) {

		double i = Double.parseDouble(str);
		return i;

	}

	public Long stringToLong(String str) {

		Long i = Long.parseLong(str);
		return i;

	}

	public static class Constants {
		public static String TITLES = "titles";
		public static String TAGS = "tags";

		public Constants() {
		}
	}

	public static class Builder implements org.apache.flume.interceptor.Interceptor.Builder {
		private Charset charset;

		public Builder() {
			this.charset = Charsets.UTF_8;
		}

		@Override
		public Interceptor build() {
			return new ARTD_BInterceptor(this.charset);
		}

		@Override
		public void configure(Context arg0) {

		}
	}
}
