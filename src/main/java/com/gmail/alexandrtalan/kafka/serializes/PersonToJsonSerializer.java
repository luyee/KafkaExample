package com.gmail.alexandrtalan.kafka.serializes;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.alexandrtalan.persistence.entities.Person;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class PersonToJsonSerializer implements Serializer<Person> {

    @Override
    public void configure(Map<String, ?> config, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Person person) {
        try {
            return new ObjectMapper().writeValueAsString(person).getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public void close() {
    }
}
