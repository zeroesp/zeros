package com.zero.zeros.javaTest.flume.thrift;

import java.nio.charset.Charset;
import org.apache.flume.Event;
//import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

public class FlumeThriftTest {
  public static void main(String[] args) throws Exception { //EventDeliveryException {
    //If you using avro or thrift source as message input, following is a java client to send message into flume
    
    //javac -cp .:flume-ng-sdk-1.8.0.jar:libthrift-0.10.0.jar:slf4j-api-1.7.25.jar FlumeThriftTest.java
    //java -cp .:flume-ng-sdk-1.8.0.jar:libthrift-0.10.0.jar:slf4j-api-1.7.25.jar FlumeThriftTest
    
    String ip = "127.0.0.1";
    int port = 17888;
     
    //  RpcClient client = RpcClientFactory.getDefaultInstance(ip, port); // Avro
    RpcClient client = RpcClientFactory.getThriftInstance(ip, port);  // Thrift
    for(int i = 0; i <= 10000; i++) {
      Event event = EventBuilder.withBody("hello flume"+i, Charset.forName("UTF8"));
      client.append(event);
      System.out.println(i);
      Thread.sleep(50);
    }
    client.close();
  }
}
