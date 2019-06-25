package com.zero.zeros.javaTest.etc;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperTest {
  public static void main(String[] args) throws Exception {
    //가장 많이들 사용하는 json 처리 관련 java 라이브러리는 다음과 같다.
    //FasterXML의 Jackson
    //Google의 Gson
    //Yidong Fang의 JSON.simple
    // 여기선 Jsckson
    
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> resultMap = new HashMap<>();
    String jsonString = "{\"name\":\"aa\",\"age\":20,\"gender\":\"male\"}";
    System.out.println("jsonString : " + jsonString);
    
    //JsonString to Map
    resultMap = mapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {});
    System.out.println("resultMap : " + resultMap);
    
    //Map to JsonString
    String resultString = mapper.writeValueAsString(resultMap);
    System.out.println("resultString : " + resultString);
  }
}
