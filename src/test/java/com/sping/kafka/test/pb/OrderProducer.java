package com.sping.kafka.test.pb;

import com.test.serializers.pb.OrderSerializer;
import kafka.message.ExchangeMessage.Order;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class OrderProducer extends Thread {
    private final KafkaProducer<Integer, Order> producer;
    private final String topic;

    public OrderProducer(String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "10.199.90.108:9092");
        props.put("client.id", "OrderProducer");
        producer = new KafkaProducer<>(props, new IntegerSerializer(), new OrderSerializer());
        this.topic = topic;
    }

    public void run() {
        int messageNo = 1;
        while (true) {
            Order order = Order.newBuilder().setOid("xxx" + String.valueOf(messageNo)).build();
            try {
                producer.send(new ProducerRecord<>(topic,
                        messageNo,
                        order)).get();
                System.out.println("Sent message: (" + messageNo + ", " + order.toString() + ")");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            ++messageNo;
        }
    }
}