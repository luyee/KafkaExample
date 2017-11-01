package com.gmail.alexandrtalan.configurations;

import com.gmail.alexandrtalan.persistence.entities.Person;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

@Configuration
@PropertySource("classpath:kafka.clients.properties")
public class KafkaProducersConfiguration {

    @Value("${person.producer.bootstrap.servers}")
    private String bootstrapServers;
    @Value("${person.producer.acks}")
    private String acks;
    @Value("${person.producer.retries}")
    private int retries;
    @Value("${person.producer.batch.size}")
    private int batchSize;
    @Value("${person.producer.linger.ms}")
    private int lingerMs;
    @Value("${person.producer.buffer.memory}")
    private int bufferMemory;
    @Value("${person.producer.key.serializer}")
    private String keySerializer;
    @Value("${person.producer.value.serializer}")
    private String valueSerializer;

    @Bean
    public KafkaProducer<String, Person> personKafkaProducer(){
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("acks", acks);
        props.put("retries", retries);
        props.put("batch.size", batchSize);
        props.put("linger.ms", lingerMs);
        props.put("buffer.memory", bufferMemory);
        props.put("key.serializer", keySerializer);
        props.put("value.serializer", valueSerializer);

        return new KafkaProducer<>(props);
    }

}
