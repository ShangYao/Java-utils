package com.jinanlongen.flume.interceptors;
/**  
*   an interceptor for flume to deal with  file from ARTD_W,
*  this version adaptation to items.1.1.1.json
* @author shangyao  
* @date 2017年11月17日 
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

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.common.base.Charsets;

public class ARTD_WInterceptor implements Interceptor {
	private final Charset charset;

	private ARTD_WInterceptor(Charset charset) {
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
		String[] body_arr = origBody.split(",");

		String basename = headers.get("basename");
		if ("".equals(body_arr[0])) {
			errofile(basename, origBody, "I-");
			event.setBody("".getBytes());
			return event;
		}
		if (body_arr.length < 11) {
			errofile(basename, origBody, "L-");
			event.setBody("".getBytes());
			return event;
		}
		if (!GetProp.getARTDWprops().containsKey(body_arr[5])) {
			erro(body_arr[5], "Gender");
			errofile(basename, origBody, "A-");
			event.setBody("".getBytes());
			return event;
		}
		if (!GetProp.getARTDWprops().containsKey(body_arr[3])) {
			erro(body_arr[3], "Brand");
			errofile(basename, origBody, "A-");
			event.setBody("".getBytes());
			return event;
		}
		if (!GetProp.getARTDWprops().containsKey(body_arr[1])) {
			erro(body_arr[1], "Category");
			errofile(basename, origBody, "A-");
			event.setBody("".getBytes());
			return event;
		}

		JSONObject obj = new JSONObject();

		JSONObject trace_info = new JSONObject();
		trace_info.put("app", "flume");
		trace_info.put("file_name", basename);

		obj.put("version", "1.1.1");
		obj.put("command", "explore");
		obj.put("trace", trace_info);

		JSONObject items_info = getItems(body_arr, headers);
		JSONArray items_arry = new JSONArray().put(items_info);
		obj.put("items", items_arry);
		obj.put("references", getreference(body_arr));

		byte[] modifiedEvent = obj.toString().getBytes();
		event.setBody(modifiedEvent);
		return event;
	}

	public JSONObject getimage(String[] body_arr, int a, String str) {
		JSONObject image1_info = new JSONObject();
		image1_info.put("item_type", "Image");
		image1_info.put("item_id", getSpuRocid(body_arr) + "::Red::" + str);
		image1_info.put("url", body_arr[a]);
		image1_info.put("origin", " true");
		image1_info.put("type", " ");
		return image1_info;

	}

	public JSONArray getreference(String[] body_arr) {
		JSONArray reference_arry = new JSONArray();

		JSONObject album_info = new JSONObject();
		album_info.put("item_type", "Album");
		album_info.put("item_id", getSpuRocid(body_arr) + "::Red");
		JSONArray album_arry = new JSONArray();
		JSONObject album1 = new JSONObject();
		album1.put("item_type", "Image");
		album1.put("item_id", getSpuRocid(body_arr) + "::Red::1");

		album_arry.put(album1);

		album_info.put("images", album_arry);

		reference_arry.put(getimage(body_arr, 2, "1"));

		reference_arry.put(album_info);

		reference_arry.put(getsku(body_arr));
		return reference_arry;

	}

	public JSONObject getsku(String[] body_arr) {
		JSONObject sku_info = new JSONObject();
		sku_info.put("item_type", "Sku");
		sku_info.put("item_id", getSkuRocid(body_arr));
		sku_info.put("meta", "");
		sku_info.put("steady_info", getsteady(body_arr));
		sku_info.put("dyno_info", getdyno(body_arr));
		return sku_info;

	}

	public JSONObject getdyno(String[] body_arr) {
		JSONObject info = new JSONObject();
		info.put("currency", "USD");
		info.put("list_price", body_arr[6]);
		info.put("price", body_arr[7]);
		info.put("stock", body_arr[9]);
		info.put("availability", "true");
		info.put("availability_reason", "");
		return info;

	}

	public JSONObject getsteady(String[] body_arr) {
		JSONObject stead_info = new JSONObject();

		JSONArray dimention_arry = new JSONArray();
		dimention_arry.put(getskudimention(body_arr, "color"));
		dimention_arry.put(getskudimention(body_arr, "size"));
		stead_info.put("dimensions", dimention_arry);

		JSONObject album_info = new JSONObject();
		album_info.put("item_type", "Album");
		album_info.put("item_id", getSpuRocid(body_arr) + "::Red");
		stead_info.put("album", album_info);

		stead_info.put("bullets", getbullts(body_arr));

		stead_info.put("upc", "");
		stead_info.put("mpn", body_arr[0]);
		stead_info.put("ean", "");
		stead_info.put("part_number", "");
		stead_info.put("model_number", "");
		stead_info.put("condition", "");

		return stead_info;

	}

	public JSONObject getbullts(String[] body_arr) {
		JSONObject info = new JSONObject();
		info.put("title", "");
		info.put("feature", "");
		info.put("description", "");

		return info;

	}

	public JSONObject getskudimention(String[] body_arr, String str) {
		JSONObject info = new JSONObject();
		info.put("item_type", "Dimension::" + str);
		info.put("item_id", "");

		return info;

	}

	public void errofile(String basename, String origBody, String pref) {
		String filename = GetProp.getARTDWprops().getString("errofilefold") + "artdw-" + pref + basename;
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

	public void erro(String contex, String pref) {
		String filename = GetProp.getARTDWprops().getString("errofilefold") + "artdw-" + pref + ".txt";
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
		return "ARTD_W#" + body_arr[0];
	}

	public String getSkuRocid(String[] body_arr) {
		return "ARTD_W#" + body_arr[0] + "#" + body_arr[0];
	}

	public JSONObject getmeta(String[] body_arr, Map<String, String> headers) {
		JSONObject meta_info = new JSONObject();
		meta_info.put("source_site_code", "ARTD_W");
		meta_info.put("all_skus", "false");
		meta_info.put("gender", GetProp.getARTDWprops().getString(body_arr[5]));
		meta_info.put("brand", GetProp.getARTDWprops().getString(body_arr[3]));
		meta_info.put("category", GetProp.getARTDWprops().getString(body_arr[1]));

		meta_info.put("url", "");
		meta_info.put("timestamp", headers.get("timestamp"));
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

	public static class Builder implements org.apache.flume.interceptor.Interceptor.Builder {
		private Charset charset;

		public Builder() {
			this.charset = Charsets.UTF_8;
		}

		@Override
		public Interceptor build() {
			return new ARTD_WInterceptor(this.charset);
		}

		@Override
		public void configure(Context arg0) {

		}
	}
}
