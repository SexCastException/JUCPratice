package com.huazai.juc.test.threadlocal;

public class Dog {

    public Dog(String name) {
        this.name = name;
    }

    public Dog() {
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                '}';
    }
}
