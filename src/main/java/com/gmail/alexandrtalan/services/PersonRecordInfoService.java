package com.gmail.alexandrtalan.services;


import com.gmail.alexandrtalan.persistence.entities.PersonRecordInfo;

import java.util.List;
import java.util.Optional;

public interface PersonRecordInfoService {

    void save(PersonRecordInfo offsetStore);

    Optional<Long> findOffset(String topic, long partition);

    List<PersonRecordInfo> getAll();

    void removeAll();

}
