package com.test;

import com.google.protobuf.InvalidProtocolBufferException;
import com.kafka.message.ExchangeMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.metrics.stats.SampledStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(KafkaListener.class);

    private static int i=0;

    @Override
    public void onMessage(ConsumerRecord record) {
        long start = System.currentTimeMillis();
        logger.info(start + " start" + (i++));
        //System.out.println(record);
        Person value = (Person) record.value();
        // System.out.println(value.getB());
        try {
            ExchangeMessage.Order order = ExchangeMessage.Order.parseFrom(value.getB());
            //System.out.println(order.getOid());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        //long end = System.currentTimeMillis();
        //logger.info(end + " end,end-start=" + (end - start) + "ms");
    }
}
