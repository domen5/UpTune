package com.uptune;

public class SessionAccount {
    private String mail;
    private String name;
    private String username;
    private String phone;

    public SessionAccount() {
        this.mail = null;
        this.name = null;
        this.username = null;
        this.phone = null;
    }

    public SessionAccount(String mail, String name, String username, String phone) {
        this.mail = mail;
        this.name = name;
        this.username = username;
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
