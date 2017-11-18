package com.jinanlongen.flume.test;

import com.jinanlongen.flume.interceptors.GetProp;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年11月17日
 */
public class Test1 {
	public static void main(String[] args) {
		String str = "1682002870,Books,http://images.areatrend.com/products/1682002870-500-500.jpg,Gary Vaynerchuk,#ASKGARYVEE,Unisex,29.99,2,93%,50,\r\n";
		String[] s = str.split(",");
		System.out.println(s.length);
		System.out.println(!GetProp.getARTDWprops().containsKey(s[1]) || !GetProp.getARTDWprops().containsKey(s[3])
				|| !GetProp.getARTDWprops().containsKey(s[5]));
		System.out.println(GetProp.getARTDWprops().containsKey(s[1]));
		System.out.println(GetProp.getARTDWprops().containsKey(s[3]));
		System.out.println(GetProp.getARTDWprops().containsKey(s[5]));
		// System.out.println(GetProp.getARTDWprops().getString("Gary Vaynerchuk"));
		// ResourceBundle b = GetProp.getARTDBprops();
		// Enumeration<String> d = b.getKeys();
		// while (d.hasMoreElements()) {
		// System.out.println(d.nextElement());
		// }
	}
}
