package com.example.rbacdemo.web;

import com.example.rbacdemo.model.*;
import com.example.rbacdemo.repo.InMemoryRepo;
import com.example.rbacdemo.security.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.*;

@Controller
public class WebController {
    private final InMemoryRepo repo;
    private final AuthService authService;
    private final ActionDispatcher dispatcher;
    private final RBACActionListener rbacListener;

    public WebController(InMemoryRepo repo, AuthService authService, ActionDispatcher dispatcher, RBACActionListener rbacListener) {
        this.repo = repo; this.authService = authService; this.dispatcher = dispatcher; this.rbacListener = rbacListener;
        // register listener
        this.dispatcher.register(this.rbacListener);
    }

    @GetMapping("/")
    public String home(HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        } else {
            return "redirect:/dashboard";
        }
    }

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, HttpSession session, Model model) {
        Optional<User> u = authService.login(username);
        if (u.isEmpty()) {
            model.addAttribute("error", "unknown user");
            return "login";
        }
        User user = u.get();
        session.setAttribute("username", user.getUsername());
        // default current role = first role
        String defaultRole = user.getRoles().stream().findFirst().map(Role::getName).orElse(null);
        authService.setCurrentRole(session, defaultRole);
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String username = authService.getCurrentUsername(session);
        if (username == null) return "redirect:/login";
        User user = repo.findUser(username).get();
        String currentRole = authService.getCurrentRole(session);

        model.addAttribute("user", user);
        model.addAttribute("roles", user.getRoles());
        model.addAttribute("currentRole", currentRole);
        model.addAttribute("resources", repo.resources.values());
        model.addAttribute("messages", List.of("Announcement: Exam next week."));
        return "dashboard";
    }

    @PostMapping("/selectRole")
    public String selectRole(@RequestParam String role, HttpSession session) {
        authService.setCurrentRole(session, role);
        return "redirect:/dashboard";
    }

    // perform an action (trigger event)
    @PostMapping("/action")
    @ResponseBody
    public Map<String,Object> performAction(@RequestParam String source,
                                            @RequestParam String target,
                                            @RequestParam String operation,
                                            HttpSession session) {
        String username = authService.getCurrentUsername(session);
        String role = authService.getCurrentRole(session);
        Action a = new Action(username, source, target, operation, "");
        boolean allowed = dispatcher.dispatch(a, role);
        Map<String,Object> r = new HashMap<>();
        r.put("action", operation + " on " + target);
        r.put("allowed", allowed);
        // if allowed, apply side effects for demo: toggling share
        if (allowed) {
            if ("share".equals(operation)) {
                repo.findResource(target).ifPresent(res -> res.setShared(!res.isShared()));
                r.put("message", "share toggled");
            } else if ("send".equals(operation)) {
                r.put("message", "message sent");
            } else if ("read".equals(operation)) {
                r.put("message", "resource read");
            } else {
                r.put("message", "operation executed");
            }
        } else {
            r.put("message", "permission denied");
        }
        return r;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
