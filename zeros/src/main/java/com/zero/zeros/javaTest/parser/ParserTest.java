package com.zero.zeros.javaTest.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
import org.json.*;

public class ParserTest {

	public static void main(String[] args) {
	    /*
	    String num1 = "98765432109876543210";
	    String num2 = "9876543210987654321098765432109876543210";
	    String num3 = "987654321098765432109876543210987654321098765432109876543210";
	    BigDecimal test1 = new BigDecimal(num1);
	    BigDecimal test2 = new BigDecimal(num2);
	    BigDecimal test3 = new BigDecimal(num3);
	    BigDecimal test4 = new BigDecimal(Long.MAX_VALUE);
	    //compareTo 1: 크다, 0:같다, -1:작다)
	    System.out.println(test1.compareTo(test2));
	    System.out.println(test2.compareTo(test1));
	    System.out.println(test2.compareTo(test3));
	    System.out.println(test2.compareTo(test2));
	    */
		/*
		String jsonBody = "{'name':'test','age':20,'adult':true}";
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(jsonBody);
    	System.out.println(element);
    	System.out.println(element.getAsJsonObject().size());
    	*/
	  
		/*
		HashMap map = new HashMap();
        map.put("test","1");
        Object test = (Object)map.get("test");
		 
		if (test instanceof String) {
			System.out.println("String");
		}else if (test instanceof Integer) {
			System.out.println("Integer");
		}else if (test instanceof Long) {
			System.out.println("Long");
		}else if (test instanceof Float) {
			System.out.println("Float");
		}else if (test instanceof Double) {
			System.out.println("Double");
		}else {
			System.out.println("fail");
		}
		*/
	  
	  ArrayList<String> list = new ArrayList<String>();
	  list.add("1,aa,11.1,b");
	  list.add("2,bb,,22.2,c");
	  list.add("3,cc,33.3,d");
	  list.add("4,dd,44.4,e");
	  list.add("5,ee,55.5,f");
	  list.add("6,ff,66.6,g");
	  list.add("7,gg,77.7,h");
	  list.add("8,hh,88.8,i");
	  list.add("9,ii,99.9,j");
	  list.add("10,jj,10,k");

	  for (String str : list) {
	    System.out.println(str);
	    String[] ecchdata = str.split(",");
	  
  	    for (String test : ecchdata) {
  	      // -? --> negative sign, could have none or one
  	      // \\d+   --> one or more digits
  	      System.out.println("test : " + test + ", number : " + test.matches("-?\\d+") + ", " + checkDataType(test)); 
  	    }
	  }
	  
	}
	
	public static String checkDataType(String data) {
	  if(data.matches( "^[-+]?(0|[1-9][0-9]*)$")) return "integer";
	  if(data.matches( "^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+)?$")) return "double";
	  return "string";
	}
}
