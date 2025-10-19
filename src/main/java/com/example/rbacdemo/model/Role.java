package com.example.rbacdemo.model;

import java.util.HashSet;
import java.util.Set;

public class Role {
    private final String name;
    private final Set<Permission> permissions = new HashSet<>();
    public Role(String name) { this.name = name; }
    public String getName() { return name; }
    public Set<Permission> getPermissions() { return permissions; }
    public void addPermission(Permission p) { permissions.add(p); }
}
