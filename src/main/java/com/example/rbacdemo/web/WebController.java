package com.example.rbacdemo.web;

import com.example.rbacdemo.entity.ResourceItemEntity;
import com.example.rbacdemo.entity.UserEntity;
import com.example.rbacdemo.model.Action;
import com.example.rbacdemo.repo.ResourceRepository;
import com.example.rbacdemo.repo.UserRepository;
import com.example.rbacdemo.security.ActionDispatcher;
import com.example.rbacdemo.security.AuthService;
import com.example.rbacdemo.security.RBACActionListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class WebController {
    private final UserRepository userRepo;
    private final ResourceRepository resourceRepo;
    private final AuthService authService;
    private final ActionDispatcher dispatcher;
    private final RBACActionListener rbacListener;

    public WebController(UserRepository userRepo, ResourceRepository resourceRepo, AuthService authService, ActionDispatcher dispatcher, RBACActionListener rbacListener) {
        this.userRepo = userRepo;
        this.resourceRepo = resourceRepo;
        this.authService = authService;
        this.dispatcher = dispatcher;
        this.rbacListener = rbacListener;
        this.dispatcher.register(this.rbacListener);
    }

    @GetMapping("/")
    public String home(HttpSession session) {
        if (session.getAttribute("username") == null) return "redirect:/login";
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, HttpSession session, Model model) {
        Optional<UserEntity> u = authService.login(username);
        if (u.isEmpty()) {
            model.addAttribute("error","未知用户，请使用示例用户名登录");
            return "login";
        }
        UserEntity user = u.get();
        session.setAttribute("username", user.getUsername());
        String defaultRole = user.getRoles().stream().findFirst().map(r->r.getName()).orElse(null);
        authService.setCurrentRole(session, defaultRole);
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String username = authService.getCurrentUsername(session);
        if (username == null) return "redirect:/login";
        UserEntity user = userRepo.findByUsername(username).get();
        String currentRole = authService.getCurrentRole(session);
        model.addAttribute("user", user);
        model.addAttribute("roles", user.getRoles());
        model.addAttribute("currentRole", currentRole);
        model.addAttribute("resources", resourceRepo.findAll());
        return "dashboard";
    }

    @PostMapping("/selectRole")
    public String selectRole(@RequestParam String role, HttpSession session) {
        authService.setCurrentRole(session, role);
        return "redirect:/dashboard";
    }

    @PostMapping("/action")
    @ResponseBody
    public java.util.Map<String,Object> performAction(@RequestParam String source,
                                            @RequestParam String target,
                                            @RequestParam String operation,
                                            HttpSession session) {
        String username = authService.getCurrentUsername(session);
        String role = authService.getCurrentRole(session);
        Action a = new Action(username, source, target, operation, "");
        boolean allowed = dispatcher.dispatch(a, role);
        java.util.Map<String,Object> r = new java.util.HashMap<>();
        r.put("action", operation + " on " + target);
        r.put("allowed", allowed);
        if (allowed) {
            if ("share".equals(operation)) {
                resourceRepo.findById(target).ifPresent(res -> { res.setShared(!res.isShared()); resourceRepo.save(res); });
                r.put("message","共享状态已切换");
            } else if ("send".equals(operation)) {
                r.put("message","消息已发送（演示）");
            } else if ("read".equals(operation)) {
                r.put("message","资源已读取（演示）");
            } else {
                r.put("message","操作已执行");
            }
        } else {
            r.put("message","权限拒绝");
        }
        return r;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) { session.invalidate(); return "redirect:/login"; }
}
