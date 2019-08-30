package com.zero.zeros.javaTest.kafka.consume;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

public class DataSampling {
  public static void main(String[] args) {
    String topic = "test2_1";
    int maxMessages = 20;
    String brokerList = "localhost:9092,localhost:9093,localhost:9094";

    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxMessages);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "accuInsightApi");

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

    List<TopicPartition> topicPartitions = new ArrayList<>();

    List<PartitionInfo> partitionInfoList = consumer.partitionsFor(topic);
    partitionInfoList.forEach(partitionInfo -> {
      TopicPartition topicPartition = new TopicPartition(topic, partitionInfo.partition());

      topicPartitions.add(topicPartition);
    });

    consumer.assign(topicPartitions);

    Map<TopicPartition, Long> offsets = consumer.endOffsets(topicPartitions);
    offsets.forEach((k, v) -> {
      System.out.println(k.toString() + ",end : " + v);
      long sampleingSize;
      if (v > maxMessages) {
        sampleingSize = v - maxMessages;
      } else {
        sampleingSize = 0;
      }
      consumer.seek(k, sampleingSize);
    });

    JsonArray resultJson = new JsonArray();
    ConsumerRecords<String, String> records = consumer.poll(100);
    for (ConsumerRecord<String, String> record : records) {
      JsonObject item = new JsonObject();
      item.addProperty("partition", record.partition());
      item.addProperty("offset", record.offset());
      item.addProperty("value", record.value());

      resultJson.add(item);
    }

    resultJson.forEach(res -> {
      System.out.println(res.getAsJsonObject().get("partition") + "-" + res.getAsJsonObject().get("offset")+ " : " + res.getAsJsonObject().get("value"));
    });

    consumer.close();
  }
}
