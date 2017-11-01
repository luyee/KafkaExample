package com.gmail.alexandrtalan.kafka.listeners;

import com.gmail.alexandrtalan.persistence.entities.PersonRecordInfo;
import com.gmail.alexandrtalan.persistence.entities.Person;
import com.gmail.alexandrtalan.services.PersonRecordInfoService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Optional;


public class PersonConsumerRebalanceListener implements ConsumerRebalanceListener {

    private final Consumer<String, Optional<Person>> consumer;
    private final PersonRecordInfoService offsetStoreService;

    public PersonConsumerRebalanceListener(
            Consumer<String, Optional<Person>> consumer,
            PersonRecordInfoService offsetStoreService
    ) {
        this.consumer = consumer;
        this.offsetStoreService = offsetStoreService;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        partitions.forEach(topicPartition ->
                offsetStoreService.save(
                        new PersonRecordInfo(topicPartition.topic(), topicPartition.partition(), consumer.position(topicPartition))
                ));
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        partitions.forEach(topicPartition -> {
            Optional<Long> optionalOffset = offsetStoreService.findOffset(topicPartition.topic(), topicPartition.partition());
            if (optionalOffset.isPresent()) {
                consumer.seek(topicPartition, optionalOffset.get() + 1);
            }
        });
    }
}
