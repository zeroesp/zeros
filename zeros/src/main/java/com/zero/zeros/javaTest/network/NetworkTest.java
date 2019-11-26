package com.zero.zeros.javaTest.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkTest {
  public static void main(String[] args){
    try {
      InetAddress addr = InetAddress.getLocalHost();
      String hostAddress = addr.getHostAddress();
      String hostName = addr.getHostName();
      String canonicalHostName = addr.getCanonicalHostName();
      byte[] address = addr.getAddress();
      System.out.println("hostAddress : " + hostAddress);
      System.out.println("hostName : " + hostName);
      System.out.println("canonicalHostName : " + canonicalHostName);
      System.out.println("address : " + address.toString());
      for (byte b : address) {
        System.out.println(b);
      }

      InetAddress addr2 = InetAddress.getByName("poc02");
      System.out.println("hostAddress : " + addr2.getHostAddress());
      System.out.println("hostName : " + addr2.getHostName());
      System.out.println("canonicalHostName : " + addr2.getCanonicalHostName());

      System.setProperty("skuniv1.skcc.com","169.56.87.58");
      System.setProperty("169.56.87.58","skuniv1.skcc.com");

      //InetAddress addr3 = InetAddress.getByName("skuniv1.skcc.com");
      //System.out.println("hostAddress : " + addr3.getHostAddress());
      //System.out.println("hostName : " + addr3.getHostName());

      InetAddress addr4 = InetAddress.getByName("169.56.87.58");
      System.out.println("hostAddress : " + addr4.getHostAddress());
      System.out.println("hostName : " + addr4.getHostName());
      System.out.println("canonicalHostName : " + addr4.getCanonicalHostName());

    } catch (Exception e) {
      System.out.println("Exception : " +e.getMessage());
    }
  }
}
