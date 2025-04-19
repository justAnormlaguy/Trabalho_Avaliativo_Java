package com.unicesumar.entities;

import java.util.UUID;

public class User extends Entity{
    private final String name;
    private final String email;
    private final String password;

    public User(UUID uuid, String password, String name, String email) {
        super(uuid);
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(String password, String name, String email) {
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
