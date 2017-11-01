package com.gmail.alexandrtalan.kafka.consumers;


import com.gmail.alexandrtalan.persistence.entities.Person;
import com.gmail.alexandrtalan.services.PersonService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

abstract class AbstractConsumer implements Runnable {

    protected final Consumer<String, Optional<Person>> consumer;
    protected final List<String> topics;
    protected final PersonService personService;

    public AbstractConsumer(
            Consumer<String, Optional<Person>> consumer,
            List<String> topics,
            PersonService personService
    ) {
        this.consumer = consumer;
        this.topics = Collections.unmodifiableList(topics);
        this.personService = personService;
    }

    protected Person convertRecordToPerson(ConsumerRecord<String, Optional<Person>> record){
        return record.value().get();
    }

}
