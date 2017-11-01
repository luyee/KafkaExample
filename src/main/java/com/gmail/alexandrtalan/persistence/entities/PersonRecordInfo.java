package com.gmail.alexandrtalan.persistence.entities;

import javax.persistence.*;

@Entity
public class PersonRecordInfo {

    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private String topic;
    @Column(nullable = false)
    private long partition;
    @Column(name = "_offset", nullable = false)
    private long offset;

    public PersonRecordInfo(){}

    public PersonRecordInfo(String topic, long partition, long offset) {
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getPartition() {
        return partition;
    }

    public void setPartition(long partition) {
        this.partition = partition;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
