package com.zero.zeros.javaTest.kafka.produce;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import java.util.Properties;
import java.util.Date;

@Component
public class ProducerTestSimple {
	public static void main(String[] args) {
	//public boolean produceMessage(int topicGenNum) {
		boolean result = false;
		
		Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "localhost:9092");
        kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer< String, String> kafkaProcuder = null;
        
        String[] ips = {"101","12","131","143","112","51","74","36","29","181"};
        String[] hosts = {"poc","stg","dev","op","test"};

        try {
        	kafkaProcuder = new KafkaProducer<String, String>(kafkaProps);
        	//for (int i = 0; i < topicGenNum; i++) {
					for (int i = 0; i < 10000; i++) {
                //String msg = "Message " + i + new Date(System.currentTimeMillis());
        	    String msg = "{\"Message\":" + i + ",\"host\":\"" + hosts[(int)Math.floor(Math.random()*4.9)] + "\",\"ip\":\""
        	                 + ips[(int)Math.floor(Math.random()*9.9)] + "." + ips[(int)Math.floor(Math.random()*9.9)] + "."
        	                 + ips[(int)Math.floor(Math.random()*9.9)] + "." + ips[(int)Math.floor(Math.random()*9.9)] + "\"}" ;
                kafkaProcuder.send(new ProducerRecord<String, String>("test", msg));
                System.out.println("Sent:" + msg);
                Thread.sleep(1000);
            }
	        result = true;
        }catch(Exception e){
        	e.printStackTrace();
        }finally {
        	kafkaProcuder.close();
        }
		//return result;
	}

}
