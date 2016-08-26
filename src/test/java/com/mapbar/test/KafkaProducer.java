package com.mapbar.test;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducer extends Thread {
	public static final Properties props = new Properties();

	public KafkaProducer() {
		props.put("bootstrap.servers", "192.168.9.3:9092,10.10.31.8:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		props.put("security.protocol", "SSL");
		// props.put("ssl.truststore.location", "/var/private/ssl/kafka.client.truststore.jks");
		props.put("ssl.truststore.location", "D:/workspace01/mapbar/src/main/resources/kafka.client.truststore.jks");
		props.put("ssl.truststore.password", "123456");
		// props.put("ssl.keystore.location", "/var/private/ssl/kafka.client.keystore.jks");
		props.put("ssl.keystore.location", "D:/workspace01/mapbar/src/main/resources/kafka.client.keystore.jks");
		props.put("ssl.keystore.password", "123456");
		props.put("ssl.key.password", "123456");
	}

	@Override
	public void run() {
		while (true) {
			Producer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);
			for (int i = 0; i < 10; i++) {
				producer.send(new ProducerRecord<String, String>("TBTest", Integer.toString(i), Integer.toString(i)));
				System.out.println("向topic：TBTest发送了消息，key为" + Integer.toString(i) + ",value为" + Integer.toString(i));
			}
			producer.close();
			try {
				sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}