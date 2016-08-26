package com.test.serializers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.lang.reflect.Method;
import java.util.Map;

public class KafkaJsonDeserializer<T> implements Deserializer<T> {

    private ObjectMapper objectMapper;
    private Class<T> type;

    public KafkaJsonDeserializer() {
        //type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public void configure(Map<String, ?> props, boolean isKey) {
        configure(new KafkaJsonDeserializerConfig(props), isKey);
    }

    protected void configure(KafkaJsonDecoderConfig config, Class<T> type) {
        this.objectMapper = new ObjectMapper();
        this.type = type;
        //reference = new TypeReference<Person<Item>>(){};

        boolean failUnknownProperties = config.getBoolean(KafkaJsonDeserializerConfig.FAIL_UNKNOWN_PROPERTIES);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failUnknownProperties);
    }

    @SuppressWarnings("unchecked")
    private void configure(KafkaJsonDeserializerConfig config, boolean isKey) {
        if (isKey) {
            configure(config, (Class<T>) config.getClass(KafkaJsonDeserializerConfig.JSON_KEY_TYPE));
        } else {
            configure(config, (Class<T>) config.getClass(KafkaJsonDeserializerConfig.JSON_VALUE_TYPE));
        }

    }

    @Override
    public T deserialize(String _, byte[] bytes) {
        if (bytes == null || bytes.length == 0)
            return null;

        try {
            return objectMapper.readValue(bytes, type);
//            T t=objectMapper.readValue(bytes,type);
//            Method methodClass = t.getClass().getMethod("getType");
//            Method method = t.getClass().getMethod("getMessage");
//            Method methodSet = t.getClass().getMethod("setMessage",Object.class);
//            Class clazz = (Class) methodClass.invoke(t);
//            Object object = method.invoke(t);
//            byte[] messageBytes=objectMapper.writeValueAsBytes(object);
//            methodSet.invoke(t,objectMapper.readValue(messageBytes, clazz));
//            return t;
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }

    protected Class<T> getType() {
        return type;
    }

    @Override
    public void close() {

    }
}
