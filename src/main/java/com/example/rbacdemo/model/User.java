package com.example.rbacdemo.model;

import java.util.HashSet;
import java.util.Set;

public class User {
    private final String username;
    private final String displayName;
    private final Set<Role> roles = new HashSet<>();

    public User(String username, String displayName) {
        this.username = username;
        this.displayName = displayName;
    }
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public Set<Role> getRoles() { return roles; }
    public void addRole(Role r) { roles.add(r); }
}
