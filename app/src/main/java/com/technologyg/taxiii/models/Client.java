package com.technologyg.taxiii.models;

public class Client {
    // Elementos del primer registro
    String id;
    String email;
    String name;
    String apePat;
    String apeMat;
    String tel;
    String pass;

    // Elementos del segundo registro


    public Client() {
    }

    public Client(String id, String email, String name, String apePat, String apeMat, String tel, String pass) {
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

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApePat() {
        return apePat;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public String getApeMat() {
        return apeMat;
    }

    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
