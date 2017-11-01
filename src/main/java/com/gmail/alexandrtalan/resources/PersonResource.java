package com.gmail.alexandrtalan.resources;

import com.gmail.alexandrtalan.kafka.consumers.AtLeastOncePersonConsumer;
import com.gmail.alexandrtalan.kafka.consumers.AtMostOncePersonConsumer;
import com.gmail.alexandrtalan.kafka.consumers.ExactlyOncePersonConsumer;
import com.gmail.alexandrtalan.persistence.entities.PersonRecordInfo;
import com.gmail.alexandrtalan.persistence.entities.Person;
import com.gmail.alexandrtalan.services.PersonRecordInfoService;
import com.gmail.alexandrtalan.services.PersonService;
import io.codearte.jfairy.Fairy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("person")
public class PersonResource {

    private final AtMostOncePersonConsumer atMostOnceConsumer;
    private final AtLeastOncePersonConsumer atLeastOnceConsumer;
    private final ExactlyOncePersonConsumer exactlyOnceConsumer;
    private final ExecutorService executorService;
    private final PersonService personService;
    private final PersonRecordInfoService offsetStoreService;

    @Autowired
    public PersonResource(
            PersonService personService,
            AtMostOncePersonConsumer atMostOnceConsumer,
            AtLeastOncePersonConsumer atLeastOnceConsumer,
            ExactlyOncePersonConsumer exactlyOnceConsumer,
            PersonRecordInfoService offsetStoreService
    ) {
        this.personService = personService;
        this.offsetStoreService = offsetStoreService;
        this.atMostOnceConsumer = atMostOnceConsumer;
        this.atLeastOnceConsumer = atLeastOnceConsumer;
        this.exactlyOnceConsumer = exactlyOnceConsumer;
        this.executorService = Executors.newFixedThreadPool(3);
    }


    @PostConstruct
    public void init() {
        executorService.execute(atMostOnceConsumer);
        executorService.execute(atLeastOnceConsumer);
        executorService.execute(exactlyOnceConsumer);
    }

    @RequestMapping(method = POST)
    public ResponseEntity<Void> sendRandomPerson() {
        personService.send(new Person(Fairy.create().person()));
        return ok().build();
    }

    @RequestMapping(path = "/all", method = DELETE)
    public ResponseEntity<Void> removeAllPersons() {
        personService.removeAll();
        return ok().build();
    }

    @RequestMapping(path = "/all", method = GET)
    public List<Person> getAllPersons() {
        return personService.getAll();
    }


    @RequestMapping(path = "/offset/all", method = GET)
    public List<PersonRecordInfo> getAllOffsets() {
        return offsetStoreService.getAll();
    }

    @RequestMapping(path = "/offset/all", method = DELETE)
    public ResponseEntity<Void> removeAllOffsets() {
        offsetStoreService.removeAll();
        return ok().build();
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }
}
