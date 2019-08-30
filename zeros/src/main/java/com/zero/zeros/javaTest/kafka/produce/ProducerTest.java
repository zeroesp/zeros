package com.zero.zeros.javaTest.kafka.produce;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class ProducerTest{
  //compile : javac -cp .:kafka-clients-2.1.0.jar:slf4j-api-1.7.25.jar:gson-2.8.1.jar:lz4-java-1.4.1.jar:snappy-java-1.1.7.1.jar ProducerTest.java
  //compile : java -cp .:kafka-clients-2.1.0.jar:slf4j-api-1.7.25.jar:gson-2.8.1.jar:lz4-java-1.4.1.jar:snappy-java-1.1.7.1.jar ProducerTest test.topic
  public static void main(String[] args){
    Properties props = new Properties();
	props.put("bootstrap.servers","server1:9092,server2:9092,server3:9092"); //broker 리스트
	props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer"); //Serializer (글자 깨질경우 ByteArraySerializer 등 변경)
	props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer"); //Serializer (글자 깨질경우 ByteArraySerializer 등 변경)
	
	props.put("acks","all"); //응답 유형 : kafka가 받고 모두 복제 후에 응답처리
	props.put("retries","50"); //요청 실패 시 재시도 횟수(필요 시 증가)
	props.put("max.in.flight.requests.per.connection","5"); //connection별 request 요청 수
	props.put("enable.idempotence","true"); //정확히 한번 보내기 위한 옵션 (위 3개 옵션과 함께 써야함)
	
	//Kerberos 인증 (사용자별 keytab, principal 정보만 변경)
	props.put("security.protocol","SASL_PLAINTEXT");
	props.put("sasl.mechanism","GSSAPI");
	props.put("sasl.kerberos.service.name","kafka");
	props.put("sasl.jaas.config","com.sun.security.auth.module.Krb5LoginModule required useKeyTab=true storeKey=true keyTab=\"C:/kerberos/kafka_plugin.keytab\" principal=\"plugin@SK.COM\";");
	
	//batch, compression : Async 처리 방식에 사용 (Sync 방식에서 사용할 경우 linger.ms만큼 대기 후 전송 및 건건 압축)
	//props.put("compression.type","lz4"); //lz4, snappy : 배치 압축 방식
	//props.put("batch.size","1048576"); //1MB : 배치 대기 크기 (필요 시 조정)
	//props.put("linger.ms","10"); //10ms : 배치 대기 시간 (필요 시 조정)
	
	//=== Transaction - id
	props.put("transactional.id","transaction.produce.test"); //transaction 처리를 위한 App별 고유한 ID
	
	//토픽명
	String topicName = args[0];
	
	Producer<String,String> producer = new KafkaProducer<String,String>(props);
	
	//=== Transaction - init
	producer.initTransactions();
	
	try {
	  //Source System에서 데이터를 받아 오는 단위에 따른 Transaction 처리
	  for(int j = 0; j < 3; j++){
			//=== Transaction - begin
			producer.beginTransaction();
		
	    for(int i = 0; i < 1000; i++){
		  //비지니스 로직에 따른 메시지 처리
		  String msg = "{\"eventTime\":" + System.currentTimeMillis() + "}";
		  
		  //토픽명과 메시지를 셋팅하여 전송 (Sync 전송 및 meta 정보 조회)
		  RecordMetadata res = producer.send(new ProducerRecord<String,String>(topicName, msg)).get();
	      System.out.println("meta=== topic: " + res.topic() + ", offset: " + res.partition() + "-" + res.offset() + ", timestamp:" + res.timestamp());
		  
		  //토픽명과 메시지를 셋팅하여 전송 (Async 전송 및 callback으로 받아 처리)
		  /*
		  producer.send(new ProducerRecord<String,String>(topicName, msg), new Callback(){
		    public void onCompletion(RecordMetadata metadata, Exception exception){
			  if(){
			    //에러 - 재처리/로깅 또는 App 종료 등 비지니스 로직 필요
				System.out.println("Exception: " + exception.toString());
			  }else{
			    //정상
				System.out.println("Ack [" + System.currentTimeMillis() + "] topic: " + metadata.topic() + ", offset: " + metadata.partition() + "-" + metadata.offset());
			  }
			}
		  });
		  */
	    }
		
		//=== Transaction - commit
		producer.commitTransaction();
	  }
	} catch(Exception e){
      //서비스 별 에러 로직 추가
	  e.printStackTrace();
	  
	  //=== Transaction - abort
	  producer.abortTransaction();
	}finally{
	  producer.close();
	}
  }
}