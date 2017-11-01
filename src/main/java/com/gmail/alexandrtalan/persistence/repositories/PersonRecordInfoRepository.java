package com.gmail.alexandrtalan.persistence.repositories;

import com.gmail.alexandrtalan.persistence.entities.PersonRecordInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRecordInfoRepository extends JpaRepository<PersonRecordInfo, Long> {

    @Query("select offset from PersonRecordInfo where topic = :topic and partition = :partition")
    Optional<Long> findOffsetByTopicAndPartition(@Param("topic") String topic, @Param("partition") long partition);

    @Query("select id from PersonRecordInfo where topic = :topic and partition = :partition")
    Optional<Long> findIdByTopicAndPartition(@Param("topic") String topic, @Param("partition") long partition);

}
