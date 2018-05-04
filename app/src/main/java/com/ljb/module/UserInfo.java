package com.ljb.module;

/**
 * Author      :meloon
 * Date        :2018/5/3
 * Description :
 */

public class UserInfo {
    private String name;
    private String sex;
    private String age;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "UserInfo{" + "name='" + name + '\'' + ", sex='" + sex + '\'' + ", age='" + age + '\'' + '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
