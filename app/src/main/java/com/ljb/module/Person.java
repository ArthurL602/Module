package com.ljb.module;

/**
 * Author      :ljb
 * Date        :2018/10/27
 * Description :
 */
public class Person {

    /**
     * name : 张三
     * age : 18
     */

    private String name;
    private int age;
    /**
     * address : address
     */

    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" + "name='" + name + '\'' + ", age=" + age + ", address='" + address + '\'' + '}';
    }
}
