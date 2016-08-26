package com.sping.kafka.test.serializer;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DummyEntity {

    public int intValue;

    public Long longValue;

    public String stringValue;

    public Map<Short, List<String>> complexStruct;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DummyEntity that = (DummyEntity) o;
        return intValue == that.intValue &&
                Objects.equals(longValue, that.longValue) &&
                Objects.equals(stringValue, that.stringValue) &&
                Objects.equals(complexStruct, that.complexStruct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intValue, longValue, stringValue, complexStruct);
    }

}