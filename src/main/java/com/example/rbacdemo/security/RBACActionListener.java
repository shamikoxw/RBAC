package com.example.rbacdemo.security;

import com.example.rbacdemo.model.Action;
import org.springframework.stereotype.Component;

@Component
public class RBACActionListener implements ActionListener {
    private final RBACService rbacService;
    public RBACActionListener(RBACService rbacService) { this.rbacService = rbacService; }

    @Override
    public boolean handle(Action action, String activeRole) {
        if (!rbacService.macAllows(action, activeRole)) {
            System.out.println("[RBACListener] MAC denies action " + action.getOperation() + " on " + action.getTarget());
            return false;
        }
        if (rbacService.roleHasPermission(activeRole, action)) {
            return true;
        }
        if ("share".equals(action.getOperation()) && rbacService.dacAllowsShare(action)) {
            return true;
        }
        System.out.println("[RBACListener] Role " + activeRole + " has no permission for action " + action.getOperation());
        return false;
    }
}
