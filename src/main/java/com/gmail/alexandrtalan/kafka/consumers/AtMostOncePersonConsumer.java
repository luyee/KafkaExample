package com.gmail.alexandrtalan.kafka.consumers;


import com.gmail.alexandrtalan.persistence.entities.Person;
import com.gmail.alexandrtalan.services.PersonService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.errors.WakeupException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public final class AtMostOncePersonConsumer extends AbstractConsumer {

    private static final int POLL_INTERVAL = 100;

    public AtMostOncePersonConsumer(
            Consumer<String, Optional<Person>> consumer,
            List<String> topics,
            PersonService personService
    ) {
        super(consumer, topics, personService);
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(topics);

            while (!Thread.currentThread().isInterrupted()) {
                StreamSupport.stream(consumer.poll(POLL_INTERVAL).spliterator(), false)
                        .filter(record -> record.value().isPresent())
                        .map(super::convertRecordToPerson)
                        .forEach(personService::save);
            }
        } finally {
            consumer.close();
        }
    }

}
