package com.zero.zeros.javaTest.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ParserTest2 {
  
  public static void main(String[] args) {
    String none = "[\"1aa11.1b\",\"2bb22.2c\",\"3cc33.3d\"]";
    String csv = "[\"1,aa,11.1,b\",\"2,bb,22.2,c,5\",\"3,cc,33.3,d\"]";
    String json = "[{\"name\":\"aa\",\"age\":20,\"gender\":\"male\"},{\"name\":\"bb\",\"age\":21,\"gender\":\"female\"},{\"name\":\"cc\",\"age\":22,\"gender\":\"male\"}]";
    String trash = "[{\"name\":\"aa\",\"age\":20,\"gender\":\"male\"},\"1aa11.1b\",{\"name\":\"cc\",\"age\":22,\"gender\":\"male\"}]";
    String trash2 = "[\"1,aa,11.1,b\",{\"name\":\"aa\",\"age\":20,\"gender\":\"male\"},\"3,cc,33.3,d\"]";

    String inputData = json;
    String inputFormat = "json";
    System.out.println(inputData);
    
    HashMap<String, Object> resultMap = new HashMap<String, Object>();
    resultMap = parse(inputData,inputFormat);
    
    /*
    if(inputFormat.equals("csv")) {
      ArrayList<HashMap<String, String>> List = new ArrayList<HashMap<String, String>>();
      HashMap<String, String> map = new HashMap<String, String>();      map.put("column","col_1");      List.add(map);
      HashMap<String, String> map1 = new HashMap<String, String>();      map1.put("column","col_2");      List.add(map1);
      HashMap<String, String> map2 = new HashMap<String, String>();      map2.put("column","col_3");      List.add(map2);
      resultMap.put("columnList", List);
    }
    if(inputFormat.equals("json")) {
      ArrayList<HashMap<String, String>> List = new ArrayList<HashMap<String, String>>();
      HashMap<String, String> map = new HashMap<String, String>();      map.put("column","name");      List.add(map);
      HashMap<String, String> map1 = new HashMap<String, String>();      map1.put("column","age");      List.add(map1);
      HashMap<String, String> map2 = new HashMap<String, String>();      map2.put("column","height");      List.add(map2);
      resultMap.put("columnList", List);
    }
    */
    
    System.out.println(resultMap);
    
    if(resultMap.get("status").equals("success")){
      resultMap = formattedData(inputData, inputFormat, (ArrayList)resultMap.get("columnList"));
    }
    System.out.println(resultMap);
  }
  
  //streaming data/컬럼정보로(columnList) formmated Data 조회
  public static HashMap<String, Object> formattedData(String str, String dataFormat, ArrayList<HashMap<String, String>> columnList){
    //result
    HashMap<String, Object> result = new HashMap<String, Object>();
    //raw데이터를 테이블 형태로 변환
    ArrayList<HashMap<String, String>> formattedList = new ArrayList<HashMap<String, String>>();
    try {
      JsonArray jsonArray = new JsonParser().parse(str).getAsJsonArray();
      if(dataFormat.equals("json")) {
        for (JsonElement element : jsonArray) {
          //각 row의 데이터
          HashMap<String, String> formattedData = new HashMap<String, String>();
          JsonObject jsonObj = element.getAsJsonObject();
          for (HashMap<String, String> keyMap : columnList) {
            if(jsonObj.has(keyMap.get("column"))) formattedData.put(keyMap.get("column"), jsonObj.get(keyMap.get("column")).getAsString());
          }
          formattedList.add(formattedData);
        }
      }else if(dataFormat.equals("csv")) {
        for (JsonElement element : jsonArray) {
          //각 row의 데이터
          HashMap<String, String> formattedData = new HashMap<String, String>();
          String[] eachColData = element.getAsString().split(",");
          for (int num = 0; num < eachColData.length; num++) {
            if(columnList.size() > num) formattedData.put(columnList.get(num).get("column"), eachColData[num]);
            else break;
          }
          formattedList.add(formattedData);
        }
      }
      result.put("status", "success");
      result.put("message", "formatted data 조회 완료");
      result.put("formattedList", formattedList);
    }catch(Exception e) {
      result.put("status", "fail");
      result.put("message", "데이터 타입을 알 수 없습니다.");
    }
    return result;
  }
  
  //str : jsonArray string, format : json/csv
  public static HashMap<String, Object> parse(String str, String format) {
    //result
    HashMap<String, Object> result = new HashMap<String, Object>();

    try {
      JsonArray jsonArray = new JsonParser().parse(str).getAsJsonArray();
      // json/csv 형식인지 아닌지
      String dataFormat = checkDataFormat(jsonArray);
      
      if(!dataFormat.equals("none") && dataFormat.equals(format)) {
        ArrayList<HashMap<String, String>> columnList = new ArrayList<HashMap<String, String>>();
        columnList = getCloumnNames(jsonArray, dataFormat);
        result.put("status", "success");
        result.put("message", "자동 파싱 완료");
        result.put("columnList", columnList);
      }else {
        result.put("status", "fail");
        result.put("message", format + "타입의 데이터가 아닙니다.");
      }
    }catch(Exception e) {
      result.put("status", "fail");
      result.put("message", "데이터 타입을 알 수 없습니다.");
    }
    return result;
  }
  
  //전체 data format 체크
  private static String checkDataFormat(JsonArray jsonArray) {
    String dataFormat = "none";
    for (JsonElement element : jsonArray) {
      if (element.isJsonObject()) {
        if (dataFormat.equals("json") || dataFormat.equals("none")) {
          dataFormat = "json";
        } else {
          dataFormat = "none";
          break;
        }
      } else if(element.getAsString().contains(",")){
        if (dataFormat.equals("csv") || dataFormat.equals("none")) {
          dataFormat = "csv";
        } else {
          dataFormat = "none";
          break;
        }
      } else {
        dataFormat = "none";
        break;
      }
    }
    return dataFormat;
  }
  
  //데이터의 컬럼명/데이터타입 리스트 반환
  private static ArrayList<HashMap<String, String>> getCloumnNames(JsonArray jsonArray, String dataFormat){
    // 컬럼의 데이터 타입
    LinkedHashMap<String, String> columnDataType = new LinkedHashMap<String, String>();
    if(dataFormat.equals("json")) {
      for (JsonElement element : jsonArray) {
        JsonObject jsonObj = element.getAsJsonObject();
        for (String key : jsonObj.keySet()) {
          columnDataType.put(key, checkDataType(jsonObj.get(key).getAsString(), columnDataType.get(key)));
        }
      }      
    }else if(dataFormat.equals("csv")) {
      for (JsonElement element : jsonArray) {
        String[] eachColData = element.getAsString().split(",");
        for (int num = 0; num < eachColData.length; num++) {
          columnDataType.put("col_" + (num + 1), checkDataType(eachColData[num], columnDataType.get("col_" + (num + 1))));
        }
      }
    }
    
    return makeCloumnList(columnDataType);
  }
  
  //데이터 타입 체크
  private static String checkDataType(String data, String finalColumnType) {
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
  
  //컬럼map을 컬럼/타입map의 list 형태로 변환
  private static ArrayList<HashMap<String, String>> makeCloumnList(LinkedHashMap<String, String> columnDataType) {
    // 컬럼명, 컬럼 데이터 타입
    ArrayList<HashMap<String, String>> columnList = new ArrayList<HashMap<String, String>>();
    for (String key : columnDataType.keySet()) {
      // 컬럼명, 컬럼 데이터 타입
      HashMap<String, String> columnData = new HashMap<String, String>();
      columnData.put("column", key);
      columnData.put("dataType", columnDataType.get(key));
      columnList.add(columnData);
    }
    return columnList;
  }

}
