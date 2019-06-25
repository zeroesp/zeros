package com.zero.zeros.javaTest.jarRun.test;

import com.google.gson.JsonObject;

public class TestParser2 {
  public int init(Object args) {
    return 0;
  }
       
  public String mapper(Object data) {
    String input = (String)data;
    JsonObject json = new JsonObject();
    if(input != null && input != "" && input.contains(",")) {
      String[] split = input.split(",");
      if(split.length == 4) {
        json.addProperty("message", split[0]);
        json.addProperty("time", split[1]);
        json.addProperty("hosts", split[2]);
        json.addProperty("ips", split[3]);
        return json.toString();
      }else {
        return "{\"message\":\"\",\"time\":\"\",\"hosts\":\"\",\"ips\":\"\"}";
      }
    }
    return null;
  }
       
  public void clear() {
  }
}
