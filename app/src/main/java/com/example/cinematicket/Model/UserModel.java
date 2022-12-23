package com.example.cinematicket.Model;

public class UserModel {
    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPasswrod() {
        return Passwrod;
    }

    public void setPasswrod(String passwrod) {
        Passwrod = passwrod;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public UserModel(String fullName, String email, String passwrod, String phone, String token, String type) {
        FullName = fullName;
        Email = email;
        Passwrod = passwrod;
        Phone = phone;
        Token = token;
        Type = type;
    }

    String FullName,Email,Passwrod,Phone,Token,Type;

    public UserModel() {
    }



}
