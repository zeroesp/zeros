package com.zero.zeros.javaTest.jarRun.test;

public class TestParser {
  public int init(Object args) {
    return 0;
  }
       
  public String mapper(Object data) {
    String input = (String)data;
    if(input != null && input != "" && input.contains(",")) {
      String[] split = input.split(",");
      if(split.length == 4) {
        return "{\"message\":\"" + split[0] + "\",\"time\":\"" + split[1] + "\",\"hosts\":\"" + split[2] + "\",\"ips\":\"" + split[3] + "\"}";
      }else {
        return "{\"message\":\"\",\"time\":\"\",\"hosts\":\"\",\"ips\":\"\"}";
      }
    }
    return null;
  }
       
  public void clear() {
  }
}
