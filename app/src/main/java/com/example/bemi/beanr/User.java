package com.example.bemi.beanr;

/**
 * Created by jimmy on 2/10/16.
 */
public class User {
    public String username;
    public String email;
    public String password;
    public String gender;

    public User(){

    }

    public User(String username, String email, String password, String gender){
        this.username=username;
        this.email=email;
        this.password=password;
        this.gender=gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
