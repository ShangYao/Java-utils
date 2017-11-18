package com.jinanlongen.flume.test;

import java.io.UnsupportedEncodingException;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年11月8日
 */
public class Test {
	public static void main(String[] args) {
		// IN in = new IN();
		// String origBody = "hfahfh,hajfhds,hfafdsk,fahf,fhfh";
		// String basename = "test.csv";
		// in.intercept(origBody, basename);

		// System.out.println(GetProp.getprops().getString("errofilefold"));
		// System.out.println(GetProp.getprops().containsKey("name"));

		// ReadExcle re = new ReadExcle();
		// File file = new File("D:\\zzz\\ARTD_B_性别.xlsx");
		// ArrayList<ArrayList<Object>> list = re.readExcel(file);
		// WriteFile write = new WriteFile();
		// for (ArrayList<Object> l : list) {
		// String line = l.get(0) + "=" + l.get(1);
		// write.write(line);
		// }

		// List list = new ArrayList<>();
		// list.add(1);
		// list.add("2");
		// Map<Class<?>, List<?>> map = (Map<Class<?>, List<?>>) list.stream()
		// .collect(Collectors.groupingBy(Object::getClass));
		// List<Integer> ints = (List<Integer>) map.get(Integer.class);
		// List<String> strings = (List<String>) map.get(String.class);
		// System.out.println(ints.toString());
		// System.out.println(strings.toString());

		String str = "w f  g  sf  ";
		try {
			byte[] ss = str.getBytes("ISO-8859-1");
			System.out.println(new String(ss, "UTF-8"));

			byte[] s = str.getBytes("ISO-8859-1");

			System.out.println(new String(s));
			String s2 = new String(s, "ISO-8859-1");
			System.out.println(s2);
			System.out.println(new String(s2.getBytes("UTF-8"), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// String str1 = new String(str.getBytes(), Charset.forName("ISO-8859-1"));
		// String str2 = null;
		// try {
		// str2 = new String(str1.getBytes("ISO-8859-1"), Charset.forName("UTF-8"));
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println(str);
		// System.out.println(str1);
		// System.out.println(str2);
		// Properties properties = System.getProperties();
		// String encodingStr = properties.getProperty("file.encoding");
		// System.out.println(encodingStr);
	}

}
