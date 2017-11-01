package com.gmail.alexandrtalan.kafka.deserializers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.alexandrtalan.persistence.entities.Person;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class PersonFromJsonDeserializer implements Deserializer<Optional<Person>> {
    @Override
    public void configure(Map<String, ?> map, boolean isKey) {
    }

    @Override
    public Optional<Person> deserialize(String s, byte[] bytes) {
        try {
            return Optional.of(new ObjectMapper().readValue(bytes, Person.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void close() {
    }
}
