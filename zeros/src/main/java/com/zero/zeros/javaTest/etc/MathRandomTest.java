package com.zero.zeros.javaTest.etc;

public class MathRandomTest {
  public static void main(String[] args) throws Exception {
    String[] ips = {"101","12","131","143","112","51","74","36","29","181"};
    String[] hosts = {"poc","stg","dev","op","test"};
    for (int i = 0; i < 5; i++) {
      System.out.println("{\"message\":" + i + ",\"hosts\":\"" + hosts[(int)Math.floor(Math.random()*4.9)] + "\",\"ips\":\""
        + ips[(int)Math.floor(Math.random()*9.9)] + "." + ips[(int)Math.floor(Math.random()*9.9)] + "."
        + ips[(int)Math.floor(Math.random()*9.9)] + "." + ips[(int)Math.floor(Math.random()*9.9)] + "\"}");
    }
    
    String[] strings = {"poc","stg","dev","op","test"};
    int[] ints = {101,12,131,143,112,51,74,36,29,181};
    int[] bigints = {101112,14452,166731,155443,133412,55561,73344,34456,22239,144534581};
    double[] floats = {101.1,12.1,13.11,14.13,112.1,51.1,74.1,3.16,2,181.1};
    double[] doubles = {1031.12,142.12,16543.121,14663.12,112.12,5.37721,74.12,36,29.12,181.12};
    for (int i = 0; i < 5; i++) {
      String msg = "Message" + i + "," + strings[(int)Math.floor(Math.random()*4.9)] + ","
          + ints[(int)Math.floor(Math.random()*9.9)] + "," + bigints[(int)Math.floor(Math.random()*9.9)] + ","
          + floats[(int)Math.floor(Math.random()*9.9)] + "," + doubles[(int)Math.floor(Math.random()*9.9)] ;
      System.out.println(msg);
    }
  }
}
