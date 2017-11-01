package com.gmail.alexandrtalan.persistence.entities;

import javax.persistence.*;

@Entity
public class Person {

    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String email;
    @Column
    private int age;

    private static final Person EMPTY_PERSON = new Person();

    public Person() {
    }

    public Person(io.codearte.jfairy.producer.person.Person person) {
        this.fullName = person.fullName();
        this.email = person.email();
        this.age = person.age();
    }

    public static Person emptyPerson() {
        return EMPTY_PERSON;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
