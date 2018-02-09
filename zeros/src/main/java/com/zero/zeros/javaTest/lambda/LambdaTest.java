package com.zero.zeros.javaTest.lambda;

public class LambdaTest {
	public static void main(String[] args) {
		Calculate calc = (a,b) -> a+b;
		System.out.println(calc.operation(1, 2));
	}
}
