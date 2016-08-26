package com.test;

import java.io.Serializable;

/**
 * @author Lenovo
 * @date 2016-08-25
 * @modify
 * @copyright
 */
public class Person implements Serializable {

    public Person() {
        //type = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private String name;
    private int age;
    private byte[] b;

    public Person(String name, int age, byte[] b) {
        this.name = name;
        this.age = age;
        this.b = b;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public byte[] getB() {
        return b;
    }

    public void setB(byte[] b) {
        this.b = b;
    }


}
