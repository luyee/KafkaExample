package com.gmail.alexandrtalan.services;

import com.gmail.alexandrtalan.persistence.entities.PersonRecordInfo;
import com.gmail.alexandrtalan.persistence.repositories.PersonRecordInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class PersonRecordServiceImpl implements PersonRecordInfoService {

    private final PersonRecordInfoRepository offsetStoreRepository;

    @Autowired
    public PersonRecordServiceImpl(PersonRecordInfoRepository offsetStoreRepository) {
        this.offsetStoreRepository = offsetStoreRepository;
    }

    @Override
    public void save(PersonRecordInfo offsetStore) {
        Optional<Long> optionalId =
                offsetStoreRepository.findIdByTopicAndPartition(offsetStore.getTopic(), offsetStore.getPartition());

        if (optionalId.isPresent()) {
            offsetStore.setId(optionalId.get());
        }
        offsetStoreRepository.save(offsetStore);
    }

    @Override
    public Optional<Long> findOffset(String topic, long partition) {
        return offsetStoreRepository.findOffsetByTopicAndPartition(topic, partition);
    }

    @Override
    public List<PersonRecordInfo> getAll() {
        return offsetStoreRepository.findAll();
    }

    @Override
    public void removeAll() {
        offsetStoreRepository.deleteAll();
    }
}
