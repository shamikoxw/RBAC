package com.example.rbacdemo.repo;

import com.example.rbacdemo.model.*;
import java.util.*;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class InMemoryRepo {
    public final Map<String, User> users = new HashMap<>();
    public final Map<String, Role> roles = new HashMap<>();
    public final Map<String, ResourceItem> resources = new HashMap<>();

    @PostConstruct
    public void init() {
        // roles
        Role admin = new Role("ADMIN");
        Role teacher = new Role("TEACHER");
        Role student = new Role("STUDENT");
        Role ta = new Role("TA");

        // permissions (fine-grained)
        // ADMIN: full
        admin.addPermission(new Permission("*","*","*"));

        // TEACHER: create/read/update own reports, share (non-confidential)
        teacher.addPermission(new Permission("UI","report:*","create"));
        teacher.addPermission(new Permission("UI","report:*","read"));
        teacher.addPermission(new Permission("UI","report:*","update"));
        teacher.addPermission(new Permission("UI","resource:*","share"));

        // TA: read reports, send messages
        ta.addPermission(new Permission("UI","report:*","read"));
        ta.addPermission(new Permission("UI","message:*","send"));

        // STUDENT: read their own resources, send message to teacher
        student.addPermission(new Permission("UI","resource:*","read"));
        student.addPermission(new Permission("UI","message:*","send"));

        roles.put(admin.getName(), admin);
        roles.put(teacher.getName(), teacher);
        roles.put(student.getName(), student);
        roles.put(ta.getName(), ta);

        // users
        User alice = new User("alice","Alice (Admin)");
        alice.addRole(admin);
        User bob = new User("bob","Bob (Teacher)");
        bob.addRole(teacher);
        User carol = new User("carol","Carol (TA/Grad)");
        carol.addRole(ta);
        carol.addRole(student); // multiple roles
        User dave = new User("dave","Dave (Student)");
        dave.addRole(student);

        users.put(alice.getUsername(), alice);
        users.put(bob.getUsername(), bob);
        users.put(carol.getUsername(), carol);
        users.put(dave.getUsername(), dave);

        // resources
        ResourceItem r1 = new ResourceItem("res1","bob"); // owned by bob
        ResourceItem r2 = new ResourceItem("res2","dave"); // owned by dave
        r2.setConfidential(true); // MAC: confidential
        resources.put(r1.getId(), r1);
        resources.put(r2.getId(), r2);
    }

    public Optional<User> findUser(String username) { return Optional.ofNullable(users.get(username)); }
    public Optional<Role> findRole(String roleName) { return Optional.ofNullable(roles.get(roleName)); }
    public Optional<ResourceItem> findResource(String id) { return Optional.ofNullable(resources.get(id)); }
}
