package com.jinanlongen.java8;

import java.util.Arrays;
import java.util.List;

/**
 * 类说明
 * 
 * @author shangyao
 * @date 2017年11月27日
 */
public class Test {
	public static void main(String[] args) {
		System.out.println(args.length);
		Arrays.asList("a", "b", "d").forEach(e -> System.out.println(e));
		Arrays.asList("a", "b", "d").sort((e1, e2) -> e1.compareTo(e2));

		// Java 8之前：
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Before Java8, too much code for too little to do");
			}
		}).start();

		// Java 8方式：
		new Thread(() -> System.out.println("In Java8, Lambda expression rocks !!")).start();

		// Java 8之前：
		List<String> features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
		for (String feature : features) {
			System.out.println(feature);
		}

		// Java 8之后：
		features.forEach(n -> System.out.println(n));
		// 使用Java 8的方法引用更方便，方法引用由::双冒号操作符标示，
		// 看起来像C++的作用域解析运算符
		features.forEach(System.out::println);

	}
}
