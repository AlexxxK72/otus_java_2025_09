package ru.otus.jdbc.crm.model;

import ru.otus.jdbc.annotation.Id;

public class Client {
    @Id
    private Long id;

    private String name;

    private Integer age;

    public Client() {}

    public Client(String name, Integer age) {
        this.id = null;
        this.name = name;
        this.age = age;
    }

    public Client(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + '}';
    }
}
