package com.gmail.alexandrtalan.kafka.consumers;


import com.gmail.alexandrtalan.persistence.entities.Person;
import com.gmail.alexandrtalan.services.PersonService;
import org.apache.kafka.clients.consumer.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class AtLeastOncePersonConsumer extends AbstractConsumer {

    private static final int PERSONS_BATCH_SIZE = 5;
    private static final int POLL_INTERVAL = 100;

    public AtLeastOncePersonConsumer(
            Consumer<String, Optional<Person>> consumer,
            List<String> topics,
            PersonService personService
    ) {
        super(consumer, topics, personService);
    }

    @Override
    public void run() {
        List<Person> persons = new ArrayList<>(PERSONS_BATCH_SIZE);
        try {
            consumer.subscribe(topics);

            while (!Thread.currentThread().isInterrupted()) {
                StreamSupport.stream(consumer.poll(POLL_INTERVAL).spliterator(), false)
                        .filter(record -> record.value().isPresent())
                        .map(super::convertRecordToPerson)
                        .collect(Collectors.toCollection(() -> persons));

                if (persons.size() >= PERSONS_BATCH_SIZE) {
                    personService.save(persons);
                    persons.clear();
                    consumer.commitSync();
                }
            }
        } finally {
            consumer.close();
        }
    }
}
