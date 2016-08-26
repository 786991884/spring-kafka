package com.mapbar;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class ConsumerTest {

	public static void consumer() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.9.3:9092,10.10.31.8:9092");
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		props.put("security.protocol", "SSL");
		props.put("ssl.truststore.location", "/var/private/ssl/kafka.client.truststore.jks");
		props.put("ssl.truststore.password", "123456");
		props.put("ssl.keystore.location", "/var/private/ssl/kafka.client.keystore.jks");
		props.put("ssl.keystore.password", "123456");
		props.put("ssl.key.password", "123456");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList("TBTest"));
		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records)
				System.out.printf("消费者消费到了,offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value());
		}
	}
}
