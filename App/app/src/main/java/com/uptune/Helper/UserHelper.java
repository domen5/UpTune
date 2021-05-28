package com.uptune.Helper;

import com.google.android.material.textfield.TextInputLayout;
import com.uptune.SessionAccount;

public class UserHelper {
    String name, username, mail, phone, passw, img;

    public UserHelper() {
    }

    public UserHelper(TextInputLayout name, TextInputLayout username, TextInputLayout mail, TextInputLayout phone, TextInputLayout passw) {
        this.name = name.getEditText().getText().toString();
        this.username = username.getEditText().getText().toString();
        this.mail = mail.getEditText().getText().toString();
        this.phone = phone.getEditText().getText().toString();
        this.passw = passw.getEditText().getText().toString();
        this.img = "https://firebasestorage.googleapis.com/v0/b/uptune-2d37c.appspot.com/o/logo.png?alt=media&token=d9476c5b-9973-4e7e-872a-c2278a61b722";
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
