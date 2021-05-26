package com.uptune;

public class SessionAccount {
    private static String mail;
    private static String name;
    private static String username;
    private static String phone;
    private static String img;

    public SessionAccount() {
        this.mail = null;
        this.name = null;
        this.username = "leleshady";
        this.phone = null;
        this.img = "";
    }

    public SessionAccount(String mail, String name, String username, String phone) {
        this.mail = mail;
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.img = "";
    }

    public static String getMail() {
        return mail;
    }

    public static String geImg() {
        return img;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public static String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getUsername() {
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
