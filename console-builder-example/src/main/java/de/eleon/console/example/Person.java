package de.eleon.console.example;

import com.google.common.base.Objects;

public class Person {

    public enum Gender {
        MALE, FEMALE
    }

    private String name;
    private Gender gender;
    private Integer age;

    public Person(String name, Gender gender, Integer age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(Person.class)
                .add("name", name)
                .add("gender", gender)
                .add("age", age)
                .toString();
    }
}
