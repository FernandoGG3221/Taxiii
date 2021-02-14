package com.technologyg.taxiii.models;

public class User {
    String id;
    String email;
    String name;
    String apePat;
    String apeMat;
    String tel;
    String pass;

    public User() {
    }

    public User(String id, String email, String name, String apePat, String apeMat, String tel, String pass) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.apePat = apePat;
        this.apeMat = apeMat;
        this.tel = tel;
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getApePat() {
        return apePat;
    }

    public String getApeMat() {
        return apeMat;
    }

    public String getTel() {
        return tel;
    }

    public String getPass(){ return pass; }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public void setApeMat(String apeMat){
        this.apeMat = apeMat;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setPass(String pass){
        this.pass = pass;
    }
}


