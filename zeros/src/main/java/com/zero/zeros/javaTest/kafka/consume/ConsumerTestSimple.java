package com.zero.zeros.javaTest.kafka.consume;

import java.time.Duration;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Properties;

@Component
public class ConsumerTestSimple {
	public static void main(String[] args) {
	//public boolean consumerMessage() {
		boolean result = false;
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
		props.put("group.id", "test12");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		props.put("client.id","ConsumerTestSimple");
		/*
		props.put("enable.auto.commit","false"); //자동으로 offset을 commit하지 않음
		props.put("auto.offset.reset","latest"); //offset 정보가 없는 경우 최신 데이터 부터 읽음 (earlist: 가장 오래된 데이터 부터)
		props.put("isolation.level","read_uncommitted"); //모든 데이터 읽음 (read_committed: 정상 트랜잭션 처리 또는 트랜잭션 처리하지 않은 데이터만 읽음)

		props.put("fetch.min.bytes","1048576"); //카프카로 부터 받는 데이터 최소량
		props.put("fetch.max.wait.ms","1000"); //카프카 fetch 대기 시간
		props.put("max.poll.records","100"); //poll반환 최대 메시지 수
		props.put("fetch.max.bytes","52428800"); //poll반환 최대 바이트 수
		*/

		KafkaConsumer<String, String> consumer = null;
		//kafka poll timeout
		long pollTimeout = Long.MAX_VALUE;

		try {
			consumer = new KafkaConsumer<>(props);
			consumer.subscribe(Arrays.asList("test2"));
			while (true) {
				//ConsumerRecords<String, String> records = consumer.poll(100);
				ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(pollTimeout)); //version 2.0 이상
				System.out.println("===== poll num: " + records.count());
				for (ConsumerRecord<String, String> record : records)
						System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
				consumer.commitSync();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			consumer.close();
		}
		//return result;
	}

}
