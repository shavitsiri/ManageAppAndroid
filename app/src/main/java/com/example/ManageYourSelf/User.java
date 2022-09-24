package com.example.ManageYourSelf;

public class User {

    private String fullName;
    private String email;

    public User(){

    }


    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }


    public String getFullName() {
        return fullName;
    }
    public String getEMail(){return email;}


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setEmail(String email){this.email = email;}

}
