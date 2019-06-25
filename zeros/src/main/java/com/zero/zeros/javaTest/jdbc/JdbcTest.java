package com.zero.zeros.javaTest.jdbc;

import java.sql.*;
import com.google.gson.*;

public class JdbcTest {
  public static void main(String[] args) throws Exception {
    String param = "{\"message\":\"3\",\"hosts\":\"test\",\"ips\":\"123.2.3.4\"}";
    System.out.println(param);
    JsonObject json = new JsonParser().parse(param).getAsJsonObject();
    
    System.out.println("■ start");
    Class.forName("com.mysql.jdbc.Driver");
    Connection conn = DriverManager.getConnection("jdbc:mysql://169.56.124.28:3306/test","pipeline","pipeline");
    
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    
    try{
     System.out.println("■ select Begin");
     pstmt = conn.prepareStatement("select  id, value from test_meta");
     rset = pstmt.executeQuery();
    
     while(rset.next()){
       System.out.println("select " + rset.getString("id") + ", " + rset.getString("value"));
     } /* end of while */
     System.out.println("■ select end");
     
     
     pstmt.close();
     pstmt = null;
     pstmt = conn.prepareStatement("insert into json_table (message, hosts, ips) values (?,?,?)");
     pstmt.setString(1, json.get("message").getAsString());
     pstmt.setString(2, json.get("hosts").getAsString());
     pstmt.setString(3, json.get("ips").getAsString());
     pstmt.execute();
     
    }catch (Throwable t){
      throw t;
    }finally {
     try{
      if(rset != null){
        rset.close();
        rset = null;
      }
     }catch(Throwable t){
       t.printStackTrace();
     }
    
     if(pstmt != null){
       pstmt.close();
       pstmt = null;
     }
     
     try{
       if(conn != null){
         conn.close();
         conn = null;
         System.out.println("conn disconect");
       }
     }catch(Throwable t){
       t.printStackTrace();
     }
    }
  }
}
