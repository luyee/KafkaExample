package com.gmail.alexandrtalan.configurations;

import com.gmail.alexandrtalan.kafka.consumers.AtLeastOncePersonConsumer;
import com.gmail.alexandrtalan.kafka.consumers.AtMostOncePersonConsumer;
import com.gmail.alexandrtalan.kafka.consumers.ExactlyOncePersonConsumer;
import com.gmail.alexandrtalan.kafka.listeners.PersonConsumerRebalanceListener;
import com.gmail.alexandrtalan.persistence.entities.Person;
import com.gmail.alexandrtalan.services.PersonRecordInfoService;
import com.gmail.alexandrtalan.services.PersonService;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Configuration
@PropertySource("classpath:kafka.clients.properties")
public class KafkaConsumersConfiguration {

    @Value("${person.consumer.bootstrap.servers}")
    public String bootstrapServers;
    @Value("${person.consumer.group.id}")
    public String groupId;
    @Value("${person.consumer.auto.commit.interval.ms}")
    public int autoCommitIntervalMs;
    @Value("${person.consumer.key.deserializer}")
    public String keyDeserializer;
    @Value("${person.consumer.value.deserializer}")
    public String valueDeserializer;

    @Value("${topics}")
    private List<String> topics;

    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRecordInfoService offsetStoreService;

    @Bean
    @Scope("prototype")
    public AtLeastOncePersonConsumer atLeastOnceConsumer(Properties properties){
        properties.put("enable.auto.commit", false);
        return new AtLeastOncePersonConsumer(new KafkaConsumer<>(properties), topics, personService);
    }

    @Bean
    @Scope("prototype")
    public AtMostOncePersonConsumer atMostOnceConsumer(Properties properties){
        return new AtMostOncePersonConsumer(new KafkaConsumer<>(properties), topics, personService);
    }

    @Bean
    @Scope("prototype")
    public ExactlyOncePersonConsumer exactlyOnceConsumer(Properties properties){
        properties.put("enable.auto.commit", false);
        KafkaConsumer<String, Optional<Person>> consumer = new KafkaConsumer<>(properties);
        ConsumerRebalanceListener rebalanceListener = new PersonConsumerRebalanceListener(consumer, offsetStoreService);
        return new ExactlyOncePersonConsumer(consumer, topics, personService, rebalanceListener);
    }

    @Bean
    public Properties properties(){
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("group.id", groupId);
        properties.put("auto.commit.interval.ms", autoCommitIntervalMs);
        properties.put("key.deserializer", keyDeserializer);
        properties.put("value.deserializer", valueDeserializer);
        return properties;
    }

}
