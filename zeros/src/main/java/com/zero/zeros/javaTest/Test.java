package com.zero.zeros.javaTest;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.web.util.UriComponentsBuilder;
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
    
    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
    
    uriComponentsBuilder.scheme("http").host("poc02:7070")
    .path("/api/v1")
    .path("/util/hdfs/web").queryParam("op", "create").queryParam("path", URLEncoder.encode("http://poc02:7070/api/v1/meta_sql_clone(5s) test_20180627.json", "UTF-8"))
    .queryParam("user.name", "test").queryParam("overwrite", true);
    
    System.out.println(uriComponentsBuilder.build().toUriString());
    
    String apiUrl = "http://poc02:7070/api/v1/util/hdfs/web?op=create" + "&path=" + URLEncoder.encode("http://poc02:7070/api/v1/meta_sql_clone(5s) test_20180627.json", "UTF-8") + "&user.name=test&overwrite=true" + "&webhdfsUrl=url";
    
    System.out.println(apiUrl);
    
    System.out.println(URLEncoder.encode("http://poc02:7070/api/v1/meta sql clone(5s) test_20180627.json", "UTF-8").replaceAll( "\\+", "_" ));
    
    /*while(true) {
      System.out.println(System.currentTimeMillis());
      Thread.sleep(1000);
    }*/
    
    String input = (String)"1,2,3,4";
    if(input != null && input != "") {
      String[] split = input.split(",");
      if(split.length > 0 ) {
        String res = "{\"message\":\"" + split[0] + "\",\"time\":\"" + split[1] + "\",\"hosts\":\"" + split[2] + "\",\"ips\":\"" + split[3] + "\"}";
        System.out.println(res);
        System.out.println("{\"result\":\"success\"}");
      }
    }
    
  }
}
