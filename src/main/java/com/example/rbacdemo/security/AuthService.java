package com.example.rbacdemo.security;

import com.example.rbacdemo.entity.UserEntity;
import com.example.rbacdemo.repo.UserRepository;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepo;
    public AuthService(UserRepository userRepo) { this.userRepo = userRepo; }

    public Optional<UserEntity> login(String username) {
        return userRepo.findByUsername(username);
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
