package com.gmail.alexandrtalan.services;

import com.gmail.alexandrtalan.persistence.entities.PersonRecordInfo;
import com.gmail.alexandrtalan.persistence.entities.Person;
import com.gmail.alexandrtalan.persistence.repositories.PersonRepository;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    @Value("${person.producer.topic.name}")
    private String topic;
    private final KafkaProducer<String, Person> kafkaProducer;
    private final PersonRepository personRepository;
    private final PersonRecordInfoService offsetStoreService;


    @Autowired
    public PersonServiceImpl(
            KafkaProducer<String, Person> kafkaProducer,
            PersonRepository personRepository,
            PersonRecordInfoService offsetStoreService
    ) {
        this.kafkaProducer = kafkaProducer;
        this.personRepository = personRepository;
        this.offsetStoreService = offsetStoreService;

    }

    @Override
    public void send(Person person) {
        kafkaProducer.send(new ProducerRecord<>(topic, person));
    }

    @Override
    public void save(Person person) {
        personRepository.save(person);
    }

    @Override
    public void save(Iterable<Person> persons) {
        personRepository.save(persons);
    }

    @Override
    public void save(Person person, PersonRecordInfo offsetStore) {
        personRepository.save(person);
        offsetStoreService.save(offsetStore);
    }

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
    public void removeAll() {
        personRepository.deleteAll();
    }


}
