package com.mapbar.test;

public class KafkaConsumerProducerDemo {

	public static void main(String[] args) {
		KafkaProducer producerThread = new KafkaProducer();
		producerThread.start();
		KafkaConsumer consumerThread = new KafkaConsumer();
		consumerThread.start();
	}
}