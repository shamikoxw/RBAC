package com.example.rbacdemo.security;

import com.example.rbacdemo.model.*;
import com.example.rbacdemo.repo.InMemoryRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RBACService {
    private final InMemoryRepo repo;
    public RBACService(InMemoryRepo repo) { this.repo = repo; }

    /** 判断某个角色是否有权限执行 action（role-based check） */
    public boolean roleHasPermission(String roleName, Action action) {
        Optional<Role> r = repo.findRole(roleName);
        if (r.isEmpty()) return false;
        for (Permission p: r.get().getPermissions()) {
            if (p.matches(action)) return true;
        }
        return false;
    }

    /** DAC check: owner may share unless MAC forbids it. Return true if action allowed by DAC. */
    public boolean dacAllowsShare(Action a) {
        if (!"share".equals(a.getOperation())) return false;
        Optional<ResourceItem> resource = repo.findResource(a.getTarget());
        if (resource.isEmpty()) return false;
        // owner can share
        return resource.get().getOwnerUsername().equals(a.getActorUsername());
    }

    /** MAC check: system policy disallow share for confidential resources except ADMIN */
    public boolean macAllows(Action a, String roleName) {
        Optional<ResourceItem> resource = repo.findResource(a.getTarget());
        if (resource.isPresent() && resource.get().isConfidential()) {
            // only ADMIN role can do anything on confidential resource
            return "ADMIN".equals(roleName);
        }
        return true;
    }
}
