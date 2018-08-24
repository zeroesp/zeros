package com.zero.zeros.javaTest.jarRun.test;

import java.sql.*;
import com.google.gson.*;

public class TestWriterJdbc {
  public int init(Object args) {
    return 0;
  }
       
  public String mapper(Object data) throws Exception{
    String input = (String)data;
    if(input != null) {
      JsonObject json = new JsonParser().parse(input).getAsJsonObject();
      Class.forName("com.mysql.jdbc.Driver");
      //Connection conn = DriverManager.getConnection("jdbc:mysql://169.56.124.28:3306/test","pipeline","pipeline");
      Connection conn = DriverManager.getConnection("jdbc:mysql://10.178.50.88:3306/test","pipeline","pipeline");

      PreparedStatement pstmt = null;
      ResultSet rset = null;
      try {
        pstmt = conn.prepareStatement("insert into json_table (message, time, hosts, ips) values (?,?,?,?)");
        pstmt.setString(1, json.get("message").getAsString());
        pstmt.setString(2, json.get("time").getAsString());
        pstmt.setString(3, json.get("hosts").getAsString());
        pstmt.setString(4, json.get("ips").getAsString());
        pstmt.execute();
      }catch(Throwable t) {
        t.printStackTrace();
      }finally {
        try{
          if (rset != null){
            rset.close();
            rset = null;
          }
        }catch (Throwable t){
           t.printStackTrace();
        }
        
        if (pstmt != null){
          pstmt.close();
          pstmt = null;
        }
        
        try {
          if(conn != null){
            conn.close();
            conn = null;
            System.out.println("conn disconect");
          }
        }catch(Throwable t){
          t.printStackTrace();
        }
      }
      return "{\"result\":\"success\"}";
    }
    return null;
  }
       
  public void clear() {
  }
}
