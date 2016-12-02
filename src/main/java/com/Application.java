package com;

import com.test.KafkaListener;
import com.test.Person;
import com.test.serializers.KafkaJsonDeserializer;
import com.test.serializers.KafkaJsonSerializer;
import com.test.serializers.MyKafkaConsumerFactory;
import kafka.message.ExchangeMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);


    @Value("${kafka.topic}")
    private String[] topic;

    @Value("${kafka.broker.address}")
    private String brokerAddress;

    @Autowired
    private KafkaListener kafkaListener;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void run(String... args) throws Exception {
        long start = System.currentTimeMillis();
        logger.info(start + " start");
        for (int i = 0; i < 20000; i++) {
            ExchangeMessage.Order order = ExchangeMessage.Order.newBuilder().setOid("xxx" + String.valueOf(1)).build();
            kafkaTemplate.send("topic1", "0", new Person("zs", 11, order.toByteArray()));
//            kafkaTemplate.send("topic2", "1", "bar");
//            kafkaTemplate.send("topic1", "2", "baz");
//            kafkaTemplate.send("topic2", "3", "qux");
            // kafkaTemplate.flush();
        }
        long end = System.currentTimeMillis();
        logger.info(end + " end,end-start=" + (end - start) + "ms");

    }

    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(Application.class);
        application.setWebEnvironment(false);
        application.run(args);

    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokerAddress);
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    /*@Bean
    public KafkaMessageListenerContainer<String, Object> container() throws Exception {
        ContainerProperties containerProps = new ContainerProperties(topic);
        containerProps.setMessageListener(kafkaListener);
        KafkaMessageListenerContainer<String, Object> container = new KafkaMessageListenerContainer<>(consumerFactory(), containerProps);
        container.getContainerProperties().setPollTimeout(3000);
        return container;
    }*/

    @Bean
    public ConcurrentMessageListenerContainer<String, Object> concurrentContainer() throws Exception {
        ContainerProperties containerProps = new ContainerProperties(topic);
        containerProps.setMessageListener(kafkaListener);
       /* containerProps.setErrorHandler(new ErrorHandler() {
            @Override
            public void handle(Exception thrownException, ConsumerRecord<?, ?> record) {
                System.out.println(record);
            }
        });*/
        //containerProps.setAckOnError(false);
        ConcurrentMessageListenerContainer<String, Object> container = new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProps);
        container.setConcurrency(2);
        container.getContainerProperties().setPollTimeout(300);
        return container;
    }


   /* @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> listenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        factory.setMessageConverter(new StringJsonMessageConverter());
        return factory;
    }*/

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = getConsumerConfig();
        return new MyKafkaConsumerFactory<>(props);
    }

    public Map<String, Object> getConsumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokerAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "siTestGroup");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "TestGroup");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
        //props.put(ConsumerConfig.)
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonDeserializer.class);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG,"consumer-id-"+ new Random().nextInt());
        return props;
    }

}