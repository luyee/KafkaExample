package com.gmail.alexandrtalan.kafka.consumers;


import com.gmail.alexandrtalan.persistence.entities.PersonRecordInfo;
import com.gmail.alexandrtalan.persistence.entities.Person;
import com.gmail.alexandrtalan.services.PersonService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public final class ExactlyOncePersonConsumer extends AbstractConsumer {

    private static final int POLL_INTERVAL = 100;
    private final ConsumerRebalanceListener consumerRebalanceListener;

    public ExactlyOncePersonConsumer(
            Consumer<String, Optional<Person>> consumer,
            List<String> topics,
            PersonService personService,
            ConsumerRebalanceListener consumerRebalanceListener
    ) {
        super(consumer, topics, personService);
        this.consumerRebalanceListener = consumerRebalanceListener;
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(topics, consumerRebalanceListener);

            while (!Thread.currentThread().isInterrupted()) {
                StreamSupport.stream(consumer.poll(POLL_INTERVAL).spliterator(), false)
                        .filter(record -> record.value().isPresent())
                        .forEach(record -> personService.save(
                                record.value().get(),
                                new PersonRecordInfo(record.topic(), record.partition(), record.offset())
                        ));
            }
        } finally {
            consumer.close();
        }
    }
}
