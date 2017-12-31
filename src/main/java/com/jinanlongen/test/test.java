package com.jinanlongen.test;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年12月4日
 */
public class test {
	public static void main(String[] args) {
		String str = "1682002870,Books,http://images.areatrend.com/products/1682002870-500-500.jpg,Gary Vaynerchuk,#ASKGARYVEE,Unisex,29.99,2,93%,50,";
		String[] body_arr = str.split(",");
		System.out.println(body_arr.length);

	}
}
