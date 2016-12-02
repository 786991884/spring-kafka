package com.test.serializers;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.core.ConsumerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Lenovo
 * @date 2016-09-05
 * @modify
 * @copyright
 */
public class MyKafkaConsumerFactory<K, V> implements ConsumerFactory<K, V> {

    private final Map<String, Object> configs;

    private Deserializer<K> keyDeserializer;

    private Deserializer<V> valueDeserializer;

    public MyKafkaConsumerFactory(Map<String, Object> configs) {
        this(configs, null, null);
    }

    public MyKafkaConsumerFactory(Map<String, Object> configs,
                                  Deserializer<K> keyDeserializer,
                                  Deserializer<V> valueDeserializer) {
        this.configs = new HashMap<>(configs);
        this.keyDeserializer = keyDeserializer;
        this.valueDeserializer = valueDeserializer;
    }

    public void setKeyDeserializer(Deserializer<K> keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public void setValueDeserializer(Deserializer<V> valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }

    @Override
    public Consumer<K, V> createConsumer() {
        return createKafkaConsumer();
    }

    protected KafkaConsumer<K, V> createKafkaConsumer() {
        String consumerId = (String) this.configs.get(ConsumerConfig.CLIENT_ID_CONFIG);
        configs.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerId+new Random().nextInt());
        return new KafkaConsumer<K, V>(this.configs, this.keyDeserializer, this.valueDeserializer);
    }

    @Override
    public boolean isAutoCommit() {
        Object auto = this.configs.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG);
        return auto instanceof Boolean ? (Boolean) auto
                : auto instanceof String ? Boolean.valueOf((String) auto) : false;
    }

}