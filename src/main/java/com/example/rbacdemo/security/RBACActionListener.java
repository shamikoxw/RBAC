package com.example.rbacdemo.security;

import com.example.rbacdemo.model.Action;
import org.springframework.stereotype.Component;

@Component
public class RBACActionListener implements ActionListener {
    private final RBACService rbacService;
    public RBACActionListener(RBACService rbacService) { this.rbacService = rbacService; }

    @Override
    public boolean handle(Action action, String activeRole) {
        // 1) MAC enforced first
        if (!rbacService.macAllows(action, activeRole)) {
            System.out.println("[RBACListener] MAC denies action " + action.getOperation() + " on " + action.getTarget());
            return false;
        }
        // 2) Role-based permission
        if (rbacService.roleHasPermission(activeRole, action)) {
            return true;
        }
        // 3) DAC: allow owner-specific share when role has no permission but owner exists
        if ("share".equals(action.getOperation()) && rbacService.dacAllowsShare(action)) {
            // owner allowed to share (DAC)
            return true;
        }
        System.out.println("[RBACListener] Role " + activeRole + " has no permission for action " + action.getOperation());
        return false;
    }
}
