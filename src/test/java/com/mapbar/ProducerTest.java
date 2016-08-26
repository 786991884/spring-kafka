package com.mapbar;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerTest {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.9.3:9092,10.10.31.8:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("security.protocol", "SSL");
		props.put("ssl.truststore.location", "/var/private/ssl/kafka.client.truststore.jks");
		props.put("ssl.truststore.password", "123456");
		props.put("ssl.keystore.location", "/var/private/ssl/kafka.client.keystore.jks");
		props.put("ssl.keystore.password", "123456");
		props.put("ssl.key.password", "123456");
		Producer<String, String> producer = new KafkaProducer<>(props);
		for (int i = 0; i < 100; i++) {
			producer.send(new ProducerRecord<String, String>("TBTest", Integer.toString(i), Integer.toString(i)));
			System.out.println("向topic：TBTest发送了消息，key为" + Integer.toString(i) + ",value为" + Integer.toString(i));
		}
		producer.close();
	}
}
