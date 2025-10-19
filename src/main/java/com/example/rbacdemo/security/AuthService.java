package com.example.rbacdemo.security;

import com.example.rbacdemo.model.User;
import com.example.rbacdemo.repo.InMemoryRepo;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class AuthService {
    private final InMemoryRepo repo;
    public AuthService(InMemoryRepo repo) { this.repo = repo; }

    public Optional<User> login(String username) {
        return repo.findUser(username);
    }

    public void setCurrentRole(HttpSession session, String roleName) {
        session.setAttribute("currentRole", roleName);
    }
    public String getCurrentRole(HttpSession session) {
        Object r = session.getAttribute("currentRole");
        return r == null ? null : r.toString();
    }
    public String getCurrentUsername(HttpSession session) {
        Object u = session.getAttribute("username");
        return u == null ? null : u.toString();
    }
}
