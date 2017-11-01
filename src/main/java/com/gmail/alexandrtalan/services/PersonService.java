package com.gmail.alexandrtalan.services;


import com.gmail.alexandrtalan.persistence.entities.PersonRecordInfo;
import com.gmail.alexandrtalan.persistence.entities.Person;

import java.util.List;

public interface PersonService {

    void send(Person person);

    void save(Person person);

    void save(Person person, PersonRecordInfo offsetStore);

    void save(Iterable<Person> persons);

    List<Person> getAll();

    void removeAll();

}
