package com.zero.zeros.javaTest.error;

public class ErrorTest {
  public static void main(String[] args){
    System.out.println("start");
    test2(2);
    //test1(2);
    System.out.println("end");
  }

  public static int test1(int x) throws ArithmeticException{
    System.out.println("test1 start");
    int v = 0;
    int res;
    try {
      res = x/v;
    } catch(Exception e){
      System.out.println("test1 Exception : " + e.getMessage());
      throw e;
    } finally {
      System.out.println("test1 final");
    }
    System.out.println("test1 res");
    return res;
  }

  public static int test2(int a) throws ArithmeticException{
    System.out.println("test2 start");
    int result = 0;
    try {
      result = test1(2);
    } catch(Exception e){
      System.out.println("test2 Exception : " + e.getMessage());
    } finally {
      System.out.println("test2 final");
    }
    System.out.println("test2 res");
    return result;
  }
}
