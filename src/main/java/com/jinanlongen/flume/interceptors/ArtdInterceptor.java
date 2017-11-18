package com.jinanlongen.flume.interceptors;

/**  
*   an interceptor for flume to deal with  file from ARTD_B,
*   this version adaptation to json formmat like Perfume's
* @author shangyao  
* @date 2017年11月8日  
*/
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.json.JSONObject;

import com.google.common.base.Charsets;

public class ArtdInterceptor implements Interceptor {
	private final String[] titles;
	private final String[] tags;
	private final Charset charset;

	private ArtdInterceptor(String titles, String tags, Charset charset) {
		this.titles = titles.split(",");
		this.charset = charset;
		this.tags = tags.split(",");
	}

	public void close() {
	}

	public void initialize() {
	}

	public Event intercept(Event event) {
		byte[] eventBody = event.getBody();
		String origBody = new String(eventBody, this.charset);
		String[] body_arr = origBody.split(",");
		JSONObject obj = new JSONObject();
		Integer titles_len = this.titles.length;
		Integer body_len = body_arr.length;

		for (int i = 0; i < titles_len.intValue(); ++i) {
			if (i < body_len.intValue()) {
				obj.put(this.titles[i], body_arr[i]);
			} else {
				obj.put(this.titles[i], "");
			}
		}

		obj.put("source_site_code", "ARTD_B");
		JSONObject meta_info = new JSONObject();
		Map<String, String> headers = event.getHeaders();
		Iterator<Entry<String, String>> var10 = headers.entrySet().iterator();

		while (var10.hasNext()) {
			Entry<String, String> entry = var10.next();
			meta_info.put(entry.getKey(), entry.getValue());
		}

		JSONObject tags_info = new JSONObject();
		Integer tags_len = this.tags.length;

		for (int i = 0; i < tags_len.intValue(); ++i) {
			String tag = this.tags[i];
			String value = headers.get(tag);
			if (value != null) {
				tags_info.put(tag, value);
			} else {
				tags_info.put(tag, "");
			}
		}

		meta_info.put("tags", tags_info);
		obj.put("meta", meta_info);
		byte[] modifiedEvent = obj.toString().getBytes();
		event.setBody(modifiedEvent);
		return event;
	}

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
		public static String TAGS = "tags";

		public Constants() {
		}
	}

	public static class Builder implements org.apache.flume.interceptor.Interceptor.Builder {
		private String titles;
		private String tags;
		private Charset charset;

		public Builder() {
			this.titles = ArtdInterceptor.Constants.TITLES;
			this.tags = ArtdInterceptor.Constants.TAGS;
			this.charset = Charsets.UTF_8;
		}

		public void configure(Context context) {
			this.titles = context.getString(ArtdInterceptor.Constants.TITLES);
			this.tags = context.getString(ArtdInterceptor.Constants.TAGS);
		}

		public Interceptor build() {
			return new ArtdInterceptor(this.titles, this.tags, this.charset);
		}
	}
}
