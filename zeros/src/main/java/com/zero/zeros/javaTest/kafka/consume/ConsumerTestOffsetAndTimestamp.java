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

public class ConsumerTestOffsetAndTimestamp{
  //compile : javac -cp .:kafka-clients-2.1.0.jar:slf4j-api-1.7.25.jar:gson-2.8.1.jar:lz4-java-1.4.1.jar:snappy-java-1.1.7.1.jar ConsumerTestOffsetAndTimestamp.java
  //compile : java -cp .:kafka-clients-2.1.0.jar:slf4j-api-1.7.25.jar:gson-2.8.1.jar:lz4-java-1.4.1.jar:snappy-java-1.1.7.1.jar ConsumerTestOffsetAndTimestamp test.topic test.group
  public static void main(String[] args){
    Properties props = new Properties();
		props.put("bootstrap.servers","server1:9092,server2:9092,server3:9092");
		props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

		props.put("group.id",args[1]);
		props.put("enable.auto.commit","false");
		props.put("auto.offset.reset","latest");
		props.put("isolation.level","read_uncommitted");
		props.put("max.poll.records","100");

		props.put("security.protocol","SASL_PLAINTEXT");
		props.put("sasl.mechanism","GSSAPI");
		props.put("sasl.kerberos.service.name","kafka");
		props.put("sasl.jaas.config","com.sun.security.auth.module.Krb5LoginModule required useKeyTab=true storeKey=true keyTab=\"C:/kerberos/kafka_plugin.keytab\" principal=\"plugin@SK.COM\";");

		KafkaConsumer<String,String> consumer = new KafkaConsumer<String,String>(props);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		try {
			String topicName = args[0];
			String time = "1557964524036";
			long timeStamp = Long.parseLong(time);
			//offset Map
			Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

			//timestamp로 offset 조회
			Map<TopicPartition, Long> timePartition = new HashMap<TopicPartition, Long>();

			//partition assign 및 offset 조회
			Collection<TopicPartition> partitions = new ArrayList<>();

			List<PartitionInfo> plist = consumer.partitionsFor(topicName);
			for(PartitionInfo p : plist){
				System.out.println("===== topic: " + p.topic() + ", partition: " + p.partition() + ", info: " + p.toString());

				timePartition.put(new TopicPartition(topicName, p.partition()), timeStamp);
				partitions.add(new TopicPartition(topicName, p.partition()));
			}

			//timestamp로 offset 조회
			Map<TopicPartition, OffsetAndTimestamp> result = consumer.offsetsForTimes(timePartition);

			//partition assign 및 offset 조회
			Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions);
			Map<TopicPartition, Long> beginOffsets = consumer.beginningOffsets(partitions);

			//partition assign
			consumer.assign(partitions);

			for(TopicPartition t : timePartition.keySet()){
				//offset 조회
				System.out.println("===== TopicPartition: " + t + ", begin: " + beginOffsets.get(t) + ", end: " + endOffsets.get(t)
							 + ", current: " + consumer.position(t) + ", lag: " + (endOffsets.get(t) - consumer.position(t)) );

				//timestamp로 offset 조회
				OffsetAndTimestamp res = result.get(t);
				System.out.println("=== time TopicPartition: " + t + ", offset: " + res.offset() + ", time: " + res.timestamp());

				//offset을 변경하는 경우 offset 셋팅
				//consumer.seek(t, res.offset());
			}
			//offset을 변경하는 경우 commit
			//consumer.commitSync();


			//consume (assign 된 partition만 consume)
			/*
			while(){
				ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(Long.MAX_VALUE));
			System.out.println("===== poll num: " + records.count());

			for(ConsumerRecord<String,String> record : records){
				System.out.println("=== %s, topic: %s, offset: %d-%d, timestamp: %d, key: %s, value: %s %n",
							format.format(System.currentTimeMillis()), record.topic(), record.partition(), record.offset(), record.timestamp(), record.key(), record.value());

				//current offset commit(건건 commit) - start
				//currentOffsets.put(
				//  new TopicPartition(record.topic(), record.partition()),
				//  new OffsetAndMetadata(record.offset() + 1)
				//);
				//consumer.commitSync(currentOffsets);
				//current offset commit - end
			}
			consumer.commitSync();
			}
			*/

		} catch(Exception e){
			e.printStackTrace();
		}finally{
			consumer.close();
		}
  }
}