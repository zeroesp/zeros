package com.zero.zeros.javaTest.jarRun.test;

import java.util.Date;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TestWriterKafka {
  public int init(Object args) {
    return 0;
  }

  public String mapper(Object data) {
    String input = (String)data;
    String result = "{\"result\":\"fail\"}";
    if(input != null) {
      JsonObject json = new JsonParser().parse(input).getAsJsonObject();
      json.addProperty("ctime", new Date(System.currentTimeMillis()).toString());
      json.addProperty("random", Math.random());
      Properties kafkaProps = new Properties();
      //kafkaProps.put("bootstrap.servers", "10.250.64.118:6667");
      kafkaProps.put("bootstrap.servers", "10.178.50.75:9111");
      kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      Producer< String, String> kafkaProcuder = null;
      try {
        
        kafkaProcuder = new KafkaProducer<String, String>(kafkaProps);
        kafkaProcuder.send(new ProducerRecord<String, String>("writer_test", json.toString()));
        //kafkaProcuder.send(new ProducerRecord<String, String>("test13", json.toString()));
        result = "{\"result\":\"success\"}";
      }catch(Exception e){
          e.printStackTrace();
      }finally {
          kafkaProcuder.close();
      }
      return result;
    }
    return null;
  }

  public void clear() {}
}
