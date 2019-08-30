package com.zero.zeros.javaTest.kafka.consume;

import java.util.Collection;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

public class HandleRebalance implements ConsumerRebalanceListener {
  @Override
  public void onPartitionsRevoked(Collection<TopicPartition> partitions){
    System.out.println("== onPartitionsRevoked start");
	try{
	  Thread.sleep(5000);
	  partitions.forEach(topicPartition -> System.out.println("=== onPartitionsRevoked topic: " + topicPartition.topic() + ", partition: " + topicPartition.partition()));
	}catch(Exception e){
	  e.printStackTrace();
	}
	System.out.println("== onPartitionsRevoked end");
  }
  
  @Override
  public void onPartitionsAssigned(Collection<TopicPartition> partitions){
    //TODO
  }
}