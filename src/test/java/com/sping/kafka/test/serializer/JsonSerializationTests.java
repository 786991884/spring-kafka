package com.sping.kafka.test.serializer;

import com.fasterxml.jackson.core.JsonParseException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class JsonSerializationTests {

    private StringSerializer stringWriter;

    private StringDeserializer stringReader;

    private JsonSerializer<DummyEntity> jsonWriter;

    private JsonDeserializer<DummyEntity> jsonReader;

    private DummyEntity entity;

    private String topic;

    @Before
    public void init() {
        entity = new DummyEntity();
        entity.intValue = 19;
        entity.longValue = 7L;
        entity.stringValue = "dummy";
        List<String> list = Arrays.asList("dummy1", "dummy2");
        entity.complexStruct = new HashMap<>();
        entity.complexStruct.put((short) 4, list);

        topic = "topic-name";

        jsonReader = new JsonDeserializer<DummyEntity>() {
        };
        jsonReader.configure(new HashMap<String, Object>(), false);
        jsonReader.close(); // does nothing, so may be called any time, or not called at all
        jsonWriter = new JsonSerializer<>();
        jsonWriter.configure(new HashMap<String, Object>(), false);
        jsonWriter.close(); // does nothing, so may be called any time, or not called at all
        stringReader = new StringDeserializer();
        stringReader.configure(new HashMap<String, Object>(), false);
        stringWriter = new StringSerializer();
        stringWriter.configure(new HashMap<String, Object>(), false);
    }

    /*
     * 1. Serialize test entity to byte array.
     * 2. Deserialize it back from the created byte array.
     * 3. Check the result with the source entity.
     */
    @Test
    public void testDeserializeSerializedEntityEquals() {
        assertThat(jsonReader.deserialize(topic, jsonWriter.serialize(topic, entity))).isEqualTo(entity);
    }

    /*
     * 1. Serialize "dummy" String to byte array.
     * 2. Deserialize it back from the created byte array.
     * 3. Fails with SerializationException.
     */
    @Test
    public void testDeserializeSerializedDummyException() {
        assertThatExceptionOfType(SerializationException.class)
                .isThrownBy(() -> jsonReader.deserialize(topic, stringWriter.serialize(topic, "dummy")))
                .withMessageStartingWith("Can't deserialize data [")
                .withCauseExactlyInstanceOf(JsonParseException.class);
    }

    @Test
    public void testSerializedStringNullEqualsNull() {
        assertThat(stringWriter.serialize(topic, null)).isEqualTo(null);
    }

    @Test
    public void testSerializedJsonNullEqualsNull() {
        assertThat(jsonWriter.serialize(topic, null)).isEqualTo(null);
    }

    @Test
    public void testDeserializedStringNullEqualsNull() {
        assertThat(stringReader.deserialize(topic, null)).isEqualTo(null);
    }

    @Test
    public void testDeserializedJsonNullEqualsNull() {
        assertThat(jsonReader.deserialize(topic, null)).isEqualTo(null);
    }

}