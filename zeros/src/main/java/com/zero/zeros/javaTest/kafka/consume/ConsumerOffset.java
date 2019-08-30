package com.zero.zeros.javaTest.kafka.consume;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

public class ConsumerOffset {
  //compile : javac -cp .:kafka-clients-2.1.0.jar:slf4j-api-1.7.25.jar:gson-2.8.1.jar:lz4-java-1.4.1.jar:snappy-java-1.1.7.1.jar ConsumerTestOffsetAndTimestamp.java
  //compile : java -cp .:kafka-clients-2.1.0.jar:slf4j-api-1.7.25.jar:gson-2.8.1.jar:lz4-java-1.4.1.jar:snappy-java-1.1.7.1.jar ConsumerTestOffsetAndTimestamp test.topic test.group
  public static void main(String[] args){
    Properties props = new Properties();
		props.put("bootstrap.servers","localhost:9092,localhost:9092,localhost:9092");
		props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

		props.put("group.id","AccuInsightAPI222");
		props.put("enable.auto.commit","false");
		props.put("auto.offset.reset","latest");
		props.put("isolation.level","read_uncommitted");
		//props.put("max.poll.records","100");

		//props.put("security.protocol","SASL_PLAINTEXT");
		//props.put("sasl.mechanism","GSSAPI");
		//props.put("sasl.kerberos.service.name","kafka");
		//props.put("sasl.jaas.config","com.sun.security.auth.module.Krb5LoginModule required useKeyTab=true storeKey=true keyTab=\"C:/kerberos/kafka_plugin.keytab\" principal=\"plugin@SK.COM\";");

		KafkaConsumer<String,String> consumer = new KafkaConsumer<String,String>(props);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		try {
			String topicName = "test";
			//partition assign 및 offset 조회
			Collection<TopicPartition> partitions = new ArrayList<>();

			List<PartitionInfo> plist = consumer.partitionsFor(topicName);
			plist.forEach(p->{
				System.out.println("===== topic: " + p.topic() + ", partition: " + p.partition() + ", info: " + p.toString());
				partitions.add(new TopicPartition(topicName, p.partition()));
			});

			//partition assign
			consumer.assign(partitions);

			Thread.sleep(15000);

			//partition assign 및 offset 조회
			Map<TopicPartition, Long> currentOffsets = new HashMap<>();
			partitions.forEach(t ->
					currentOffsets.put(t, consumer.position(t))
			);
			Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions);
			Map<TopicPartition, Long> beginOffsets = consumer.beginningOffsets(partitions);


			partitions.forEach(t->{
				//offset 조회
				System.out.println("===== TopicPartition: " + t + ", begin: " + beginOffsets.get(t) + ", end: " + endOffsets.get(t)
							 + ", current: " + currentOffsets.get(t) + ", lag: " + (endOffsets.get(t) - currentOffsets.get(t)) );
			});

			Thread.sleep(15000);

		} catch(Exception e){
			e.printStackTrace();
		}finally{
			System.out.println("consumer.close()");
			consumer.close();
		}
  }
}