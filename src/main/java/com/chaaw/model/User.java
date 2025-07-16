package com.chaaw.model;

public class User {
    public final String username;
    public final String password;
    public final String email;

    public User(String u, String p, String e) {
        this.username = u;
        this.password = p;
        this.email = e;
    }
}