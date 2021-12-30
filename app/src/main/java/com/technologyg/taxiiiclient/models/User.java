package com.technologyg.taxiiiclient.models;

public class User {
    String id;
    String email;
    String name;
    String f_name;
    String s_name;
    String phone;
    String pass;
    String image;

    //Construct empty
    public User() {
    }

    //Construct for user register
    public User(String id, String email, String name, String f_name, String s_name, String phone, String pass) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.f_name = f_name;
        this.s_name = s_name;
        this.phone = phone;
        this.pass = pass;
    }

    //Constructor for user profile
    public User(String id, String email, String name, String f_name, String s_name, String phone, String pass, String image) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.f_name = f_name;
        this.s_name = s_name;
        this.phone = phone;
        this.pass = pass;
        this.image = image;
    }

    //Getters and Setters

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

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
