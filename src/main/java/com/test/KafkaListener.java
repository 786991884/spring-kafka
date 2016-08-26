package com.test;

import com.google.protobuf.InvalidProtocolBufferException;
import com.kafka.message.ExchangeMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author Lenovo
 * @date 2016-08-24
 * @modify
 * @copyright
 */
@Component
public class KafkaListener implements MessageListener<String, Object> {

    @Override
    public void onMessage(ConsumerRecord record) {
        System.out.println(record);
        Person value = (Person) record.value();
        System.out.println(value.getB());
        try {
            ExchangeMessage.Order order = ExchangeMessage.Order.parseFrom(value.getB());
            System.out.println(order.getOid());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }
}
