package com.lespayne.smartwear.model;

public class RegisterJsonBean {

    public RegisterJsonBean(String name, int sex, String telephone, int age, String header, String password, String username) {
        this.name = name;
        this.sex = sex;
        this.telephone = telephone;
        this.age = age;
        this.header = header;
        this.password = password;
        this.username = username;
    }

    private String name;
    private int sex;
    private String telephone;
    private int age;
    private String header;
    private String password;
    private String username;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
