package com.zero.zeros.javaTest.jarRun.test;

public class TestFilter {
  public int init(Object args) {
    return 0;
  }
       
  public String mapper(Object json) {
    String input = (String)json;
    if(input.contains("stg")) {
      return input;
    }
    return null;
  }
       
  public void clear() {
  }
}
