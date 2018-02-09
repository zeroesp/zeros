package com.zero.zeros.javaTest.versionTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class javaVersionTest {
	public static void main(String[] args) throws Exception {
		System.out.println("Hello Zero, " +  new Date(System.currentTimeMillis()));
		
		// java 1.7
		System.out.println(100_000); //숫자사이 쉼표개념 _ 사용 가능
		
		String test = "";
		switch(test) {
			case "test" : System.out.println("test print"); break;
			case "java" : System.out.println("java print"); break;
			default : System.out.println("def print");
		} //swith case에 string 사용가능 (null인 경우 exception)
		
		try {
			// catch의 exception 내용이 같다면 |로 묶어줄 수 있음
		}catch(NullPointerException | NumberFormatException ex) {
			ex.printStackTrace();			
		}
		 
		// java 1.8
		//Optional 클래스 - 간편한 null 처리
		//default 메소드 - 하위 호환성 때문, 인터페이스에 새로운 메소드 추가 시 사용. 구현된 메소드 

		// stream
		List<Integer> ar = new ArrayList<>();
		ar.add(1);
		ar.add(2);
		ar.add(3);
		ar.stream().map(a->Integer.parseInt(a.toString())+1).forEach(b->System.out.println(b));
	}
}
