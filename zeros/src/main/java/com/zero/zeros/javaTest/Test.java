package com.zero.zeros.javaTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Test {
  public static void main(String[] args) throws Exception {
    String test = "{\"test1\":\"404\"}";
    System.out.println("test : " + test);
    JsonElement elm = new JsonParser().parse(test);
    System.out.println("elm : " + elm);
    if(elm.isJsonObject()) {
      JsonObject obj = elm.getAsJsonObject();
      System.out.println("obj : " + obj);
      if(obj.has("test")) {
        obj.addProperty("test", "500");
      }else {
        obj.addProperty("test2", "500");
      }
      System.out.println("obj : " + obj);
    }
  }
}
