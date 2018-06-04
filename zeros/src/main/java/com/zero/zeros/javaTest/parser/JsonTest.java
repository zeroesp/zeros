package com.zero.zeros.javaTest.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonTest {
  
  public static void main(String[] args) {
    JsonObject json1 = new JsonObject();
    json1.addProperty("id", "1");
    json1.addProperty("name", "abc");
    
    JsonObject json2 = new JsonObject();
    json2.addProperty("id", "2");
    json2.addProperty("name", "xyz");
    
    JsonObject json3 = new JsonObject();
    json3.addProperty("id", "3");
    json3.addProperty("name", "asd");
    
    JsonArray array1 = new JsonArray();
    array1.add(json1);
    array1.add(json2);
    array1.add(json3);
    
    System.out.println("json1 : " + json1);
    System.out.println("json2 : " + json2);
    System.out.println("json3 : " + json3);
    System.out.println("array1 : " + array1);
    
    System.out.println("=====");
    
    JsonObject json4 = new JsonObject();
    json4.addProperty("id", "1");
    json4.addProperty("desc", "abc111");
    
    JsonObject json5 = new JsonObject();
    json5.addProperty("id", "2");
    json5.addProperty("desc", "xyz222");
    
    JsonArray array2 = new JsonArray();
    array2.add(json4);
    array2.add(json5);
    
    System.out.println("json4 : " + json4);
    System.out.println("json5 : " + json5);
    System.out.println("array2 : " + array2);
    
    System.out.println("===== merge");
    
    JsonObject jsonNew1 = merge(json1, json4);
    System.out.println("json1 + json4 : " + jsonNew1);
    
    JsonObject json = new JsonObject();
    json.addProperty("statusCode", 200);
    json.add("data", new JsonArray());
    System.out.println("empty dataArray return json :" + json);
    
    JsonObject jsonTest = new JsonObject();
    jsonTest.addProperty("statusCode", 200);
    JsonArray jsonArrayTest = new JsonArray();
    JsonObject jsonTest2 = new JsonObject();
    jsonTest2.addProperty("id", 1106);
    jsonTest2.addProperty("category", "default");
    jsonTest2.addProperty("name", "cluster");
    jsonArrayTest.add(jsonTest2);
    jsonTest.add("data", jsonArrayTest);
    System.out.println("dataArray return json :" + jsonTest);
  }
  
  public static JsonObject merge(JsonObject a, JsonObject b) {
    /*
    JsonObject total = new JsonObject();
    for(Entry<String, JsonElement> entry : a.entrySet()) {
      total.add(entry.getKey(), entry.getValue());
    }
    */
    for(Entry<String, JsonElement> entry : b.entrySet()) {
      a.add(entry.getKey(), entry.getValue());
    }
    //return total;
    return a;
  }
}