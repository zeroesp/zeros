package com.zero.zeros.javaTest.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.MalformedJsonException;

public class ParserTest2 {
  
  public static void main(String[] args) {
    String none = "[\"1aa11.1b\",\"2bb22.2c\",\"3cc33.3d\"]";
    String csv = "[\"1,aa,11.1,b\",\"2,bb,22.2,c\",\"3,cc,33.3,d\"]";
    String json = "[{\"name\":\"aa\",\"age\":20,\"gender\":\"male\"},{\"name\":\"bb\",\"age\":21,\"gender\":\"female\"},{\"name\":\"cc\",\"age\":22,\"gender\":\"male\"}]";
    System.out.println(csv);
    System.out.println(json);
    
    //result
    HashMap<String, Object> result = new HashMap<String, Object>();
    ArrayList<HashMap<String, String>> columnList = new ArrayList<HashMap<String, String>>();
    
    JsonParser parser = new JsonParser();
    JsonArray jsonArray;
    try {
      jsonArray = parser.parse(json).getAsJsonArray();
      
      // json/csv 형식인지 아닌지
      String dataFormat = "none";
      
      for (JsonElement element : jsonArray) {
        if (element.isJsonObject()) {
          if (dataFormat.equals("json") || dataFormat.equals("none")) {
            dataFormat = "json";
          } else {
            result.put("result", "데이터 타입을 알 수 없습니다.");
            break;
          }
        } else if(element.getAsString().contains(",")){
          if (dataFormat.equals("csv") || dataFormat.equals("none")) {
            dataFormat = "csv";
            
          } else {
            result.put("result", "데이터 타입을 알 수 없습니다.");
            break;
          }
        }
      }
      
      if(dataFormat.equals("none")) System.out.println("실패");
      
      columnList = getCloumnNames(jsonArray, dataFormat);
      
      System.out.println("컬럼 정보");
    }catch(Exception e) {
      System.out.println("파싱실패");
    }
  }
  
  public static ArrayList<HashMap<String, String>> getCloumnNames(JsonArray jsonArray, String dataFormat){
    // 컬럼명, 컬럼 데이터 타입
    ArrayList<HashMap<String, String>> columnList = new ArrayList<HashMap<String, String>>();
    // 컬럼의 데이터 타입
    LinkedHashMap<String, String> columnDataType = new LinkedHashMap<String, String>();
    if(dataFormat.equals("json")) {
      for (JsonElement element : jsonArray) {
        JsonObject jsonObj = element.getAsJsonObject();
        for (String key : jsonObj.keySet()) {
          columnDataType.put(key, checkDataType(jsonObj.get(key).getAsString(), columnDataType.get(key)));
        }
      }
      for (String key : columnDataType.keySet()) {
        // 컬럼명, 컬럼 데이터 타입
        HashMap<String, String> columnData = new HashMap<String, String>();
        columnData.put("columnName", key);
        columnData.put("columnType", columnDataType.get(key));
        columnList.add(columnData);
      }
      
    }else if(dataFormat.equals("csv")) {
      for (JsonElement element : jsonArray) {
        String[] eachColData = element.getAsString().split(",");
        int minSize = 0;
        if(eachColData.length < minSize || minSize == 0) minSize = eachColData.length;
        for (int num = 0; num < eachColData.length; num++) {
          columnDataType.put("col_" + (num + 1), checkDataType(eachColData[num], columnDataType.get("col_" + (num + 1))));
        }
      }
      for (String key : columnDataType.keySet()) {
        // 컬럼명, 컬럼 데이터 타입
        HashMap<String, String> columnData = new HashMap<String, String>();
        columnData.put("columnName", key);
        columnData.put("columnType", columnDataType.get(key));
        columnList.add(columnData);
      }
    }
    
    return columnList;
  }
  
  public static String checkDataType(String data, String finalColumnType) {
    if (data.matches("^[-+]?(0|[1-9][0-9]*)$")) {
      if("long".equals(finalColumnType) || finalColumnType == null) {
        return "long";
      }else {
        return finalColumnType;
      }
    }else if (data.matches("^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+)?$")) {
      if(!"string".equals(finalColumnType) || finalColumnType == null) {
        return "double";
      }else {
        return finalColumnType;
      }
    }
    return "string";
  }

}
