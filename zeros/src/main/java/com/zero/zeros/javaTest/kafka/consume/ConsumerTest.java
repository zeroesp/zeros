package com.zero.zeros.javaTest.kafka.consume;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class ConsumerTest{
  //compile : javac -cp .:kafka-clients-2.1.0.jar:slf4j-api-1.7.25.jar:gson-2.8.1.jar:lz4-java-1.4.1.jar:snappy-java-1.1.7.1.jar ConsumerTest.java
  //compile : java -cp .:kafka-clients-2.1.0.jar:slf4j-api-1.7.25.jar:gson-2.8.1.jar:lz4-java-1.4.1.jar:snappy-java-1.1.7.1.jar ConsumerTest test.topic test.group
  public static void main(String[] args){
    Properties props = new Properties();
		props.put("bootstrap.servers","localhost:9092,localhost:9093,localhost:9094");
		props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
		props.put("client.id","zeroID");

		props.put("group.id","AccuInsightAPI11"); //consumer의 그룹명
		props.put("enable.auto.commit","false"); //자동으로 offset을 commit하지 않음
		props.put("auto.offset.reset","latest"); //offset 정보가 없는 경우 최신 데이터 부터 읽음 (earlist: 가장 오래된 데이터 부터)
		props.put("isolation.level","read_uncommitted"); //모든 데이터 읽음 (read_committed: 정상 트랜잭션 처리 또는 트랜잭션 처리하지 않은 데이터만 읽음)

		//Kerberos 인증 (사용자별 keytab, principal 정보만 변경)
		//props.put("security.protocol","SASL_PLAINTEXT");
		//props.put("sasl.mechanism","GSSAPI");
		//props.put("sasl.kerberos.service.name","kafka");
		//props.put("sasl.jaas.config","com.sun.security.auth.module.Krb5LoginModule required useKeyTab=true storeKey=true keyTab=\"C:/kerberos/kafka_plugin.keytab\" principal=\"plugin@SK.COM\";");

		//polling해오는 데이터를 조정할 필요가 있는 경우 사용
		props.put("fetch.min.bytes","1048576"); //카프카로 부터 받는 데이터 최소량
		props.put("fetch.max.wait.ms","1000"); //카프카 fetch 대기 시간
		props.put("max.poll.records","100"); //poll반환 최대 메시지 수
		props.put("fetch.max.bytes","52428800"); //poll반환 최대 바이트 수

		//kafka poll timeout
		long pollTimeout = Long.MAX_VALUE;
		//토픽명
		String topicName = "test2";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		KafkaConsumer<String,String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(topicName));
		//rebalance Assign/Revoke 시 선행/후행 처리되어야 할 로직이 있는 경우 로직 구현
		//consumer.subscribe(Arrays.asList(topicName), new HandleRebalance());

		//offset Map
		Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

		try {
			while(true){
				//메시지 polling, poll timeout 셋팅
				ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(pollTimeout)); //version 2.0 이상
				//ConsumerRecords<String,String> records = consumer.poll(pollTimeout);
				System.out.println("===== poll num: " + records.count());

				for(ConsumerRecord<String,String> record : records){
					//비지니스 로직
					System.out.printf("=== %s, topic: %s, offset: %d-%d, timestamp: %d, key: %s, value: %s %n", format.format(System.currentTimeMillis()), record.topic(), record.partition(), record.offset(), record.timestamp(), record.key(), record.value());

					//current offset commit(건별 commit) - start
					/*
					currentOffsets.put(
						new TopicPartition(record.topic(), record.partition()),
						new OffsetAndMetadata(record.offset() + 1)
					);
					consumer.commitSync(currentOffsets);
					*/
					//current offset commit - end
				}
				//polling된 레코드 전체 처리 후 offset commit
				consumer.commitSync();
			}
		} catch(Exception e){
				//서비스 별 에러 로직 추가
			e.printStackTrace();
		}finally{
			consumer.close();
		}
  }
}