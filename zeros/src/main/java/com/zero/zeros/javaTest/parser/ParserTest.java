package com.zero.zeros.javaTest.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
import org.json.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
	  
	  /*
	  ArrayList<String> list = new ArrayList<String>();
      list.add("1,aa,11.1,b");
      list.add("2,bb,22.2,c");
      list.add("3,cc,33.3,d");
      list.add("4,dd,44.4,e");
      list.add("5,ee,55.5,f,5");
      list.add("6,ff,66.6,g");
      list.add("7,gg,77.7,h");
      list.add("8,hh,88.8,i");
      list.add("9,ii,99.9,j");
      list.add("10,jj,10,k");
      */
	  
	  //["1,aa,11.1,b","2,bb,,22.2,c","3,cc,33.3,d"]
	  String csv = "[\"1,aa,11.1,b\",\"2,bb,22.2,c\",\"3,cc,33.3,d\"]";
	  System.out.println(csv);
	  //[{"name":"aa","age":20,"gender":"male"},{"name":"bb","age":21,"gender":"female"},{"name":"cc","age":22,"gender":"male"}]
	  String json = "[{\"name\":\"aa\",\"age\":20,\"gender\":\"male\"},{\"name\":\"bb\",\"age\":21,\"gender\":\"female\"},{\"name\":\"cc\",\"age\":22,\"gender\":\"male\"}]";
	  System.out.println(json);

	  JsonParser parser = new JsonParser();
	  JsonArray jsonArray = parser.parse(csv).getAsJsonArray();
	  //Gson gson = new Gson();
      //List<HashMap<String,String>> list1 = gson.fromJson(jsonArray, new TypeToken<List<HashMap<String,String>>>(){}.getType());
      //List<String> list2 = gson.fromJson(jsonArray, new TypeToken<List<String>>(){}.getType());
	  
	  HashMap<String, Object> result= new HashMap<String, Object>();
	  //json 형식인지 아닌지
	  String dataType = "";
	  for(JsonElement element : jsonArray) {
	    if(element.isJsonObject()){
	      if(dataType.equals("") || dataType.equals("json")) {
	        dataType="json";
	      }else {
	        result.put("result", "데이터 타입을 알 수 없습니다.");
	      }
	    }else {
	      if(dataType.equals("") || dataType.equals("csv")) {
	        dataType="csv";
	      }else {
	        result.put("result", "데이터 타입을 알 수 없습니다.");
	      }
	    }
	  }
	  
	  if(dataType.equals("json")) {
	    result = jsonParse(result, jsonArray);
	  }else if (dataType.equals("csv")) {
	    result = csvParse(result, jsonArray);
      }
	  
	  /*
	  for(JsonElement element : jsonArray) {
	    if(element.isJsonObject()){
	      for (String key : element.getAsJsonObject().keySet()) {
	        System.out.println("key : " + key + ", " + element.getAsJsonObject().get(key).getAsString());
	      }  
	    }else {
	      System.out.println(element.getAsString());
	    }
	  }
	  */

	  /*
	  //컬럼명, 컬럼 데이터 타입
	  ArrayList<HashMap<String,String>> columnList = new ArrayList<HashMap<String,String>>();
	  HashMap<String,String> columnData = new HashMap<String,String>();
	  //raw데이터를 테이블 형태로 변환
	  ArrayList<HashMap<String,String>> formattedList = new ArrayList<HashMap<String,String>>();
	  
	  //raw 데이터를 formattedList로 변환
	  for (String str : list) {
	    System.out.println(str);
	    //각 컬럼의 데이터
	    String[] eachColData = str.split(",");
	    HashMap<String,String> formattedData = new HashMap<String,String>();
  	    for (int num=0;num<eachColData.length;num++) {
  	      String columnName = "col_" + (num+1);
  	      String columnType = checkDataType(eachColData[num]);
  	      String finalColumnType = columnData.get(columnName);
  	      System.out.println("columnName : " + columnName + ", columnType : " + columnType + ", columnValue : " + eachColData[num]); 
  	      formattedData.put(columnName, eachColData[num]);
  	      
  	      if(finalColumnType != null) {
  	        if(finalColumnType.equals("string")) break;
  	        if(finalColumnType.equals("long") && !columnType.equals("long")) columnData.put(columnName, columnType);
  	        if(finalColumnType.equals("double") && columnType.equals("string")) columnData.put(columnName, columnType);
  	      }else {
  	        columnData.put(columnName, columnType);
  	      }
  	    }
  	    formattedList.add(formattedData);
	  }
	  */
	  
	}
	
	public static String checkDataType(String data) {
	  if(data.matches( "^[-+]?(0|[1-9][0-9]*)$")) return "long";
	  if(data.matches( "^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+)?$")) return "double";
	  return "string";
	}
	
	public static HashMap<String, Object> jsonParse(HashMap<String, Object> map, JsonArray jsonArray) {
	  for(JsonElement element : jsonArray) {
	    
	  }
	  return map;
	}
	
	public static HashMap<String, Object> csvParse(HashMap<String, Object> map, JsonArray jsonArray) {
	  //컬럼명, 컬럼 데이터 타입
      ArrayList<HashMap<String,String>> columnList = new ArrayList<HashMap<String,String>>();
      //컬럼의 데이터 타입
      HashMap<String,String> columnDataType = new HashMap<String,String>();
      //raw데이터를 테이블 형태로 변환
      ArrayList<HashMap<String,String>> formattedList = new ArrayList<HashMap<String,String>>();
      
	  for(JsonElement element : jsonArray) {
	    String[] eachColData = element.getAsString().split(",");
        HashMap<String,String> formattedData = new HashMap<String,String>();
        for (int num=0;num<eachColData.length;num++) {
          String columnName = "col_" + (num+1);
          String columnType = checkDataType(eachColData[num]);
          String finalColumnType = columnDataType.get(columnName);
          System.out.println("columnName : " + columnName + ", columnType : " + columnType + ", columnValue : " + eachColData[num]); 
          formattedData.put(columnName, eachColData[num]);
          
          if(finalColumnType != null) {
            if(finalColumnType.equals("string")) columnDataType.put(columnName, columnType);
            if(finalColumnType.equals("long") && !columnType.equals("long")) columnDataType.put(columnName, columnType);
            if(finalColumnType.equals("double") && columnType.equals("string")) columnDataType.put(columnName, columnType);
          }else {
            columnDataType.put(columnName, columnType);
          }
        }
        formattedList.add(formattedData);
      }
	  
	  for(int num=0;num<columnDataType.size();num++) {
	    String columnName = "col_" + (num+1);
	    //컬럼명, 컬럼 데이터 타입
	    HashMap<String,String> columnData = new HashMap<String,String>();
	    columnData.put("columnName",columnName);
	    columnData.put("columnType",columnDataType.get(columnName));
	    columnList.add(columnData);
	  }
	  
	  map.put("columnList",columnList);
	  map.put("formattedList",formattedList);
      return map;
    }
}
