package com.lespayne.smartwear.model;

public class LoginBean {
    private String telephone;
    private String password;

    public LoginBean(String telephone, String password) {
        this.telephone = telephone;
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
