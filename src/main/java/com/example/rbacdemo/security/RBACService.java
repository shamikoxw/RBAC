package com.example.rbacdemo.security;

import com.example.rbacdemo.entity.RoleEntity;
import com.example.rbacdemo.entity.PermissionEntity;
import com.example.rbacdemo.entity.ResourceItemEntity;
import com.example.rbacdemo.model.Action;
import com.example.rbacdemo.repo.RoleRepository;
import com.example.rbacdemo.repo.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RBACService {
    private final RoleRepository roleRepo;
    private final ResourceRepository resourceRepo;
    public RBACService(RoleRepository roleRepo, ResourceRepository resourceRepo) {
        this.roleRepo = roleRepo;
        this.resourceRepo = resourceRepo;
    }

    public boolean roleHasPermission(String roleName, Action action) {
        Optional<RoleEntity> r = roleRepo.findByName(roleName);
        if (r.isEmpty()) return false;
        for (PermissionEntity p: r.get().getPermissions()) {
            if (p.matches(action.getSource(), action.getTarget(), action.getOperation())) return true;
        }
        return false;
    }

    public boolean dacAllowsShare(Action a) {
        if (!"share".equals(a.getOperation())) return false;
        Optional<ResourceItemEntity> res = resourceRepo.findById(a.getTarget());
        if (res.isEmpty()) return false;
        return res.get().getOwnerUsername().equals(a.getActorUsername());
    }

    public boolean macAllows(Action a, String roleName) {
        Optional<ResourceItemEntity> res = resourceRepo.findById(a.getTarget());
        if (res.isPresent() && res.get().isConfidential()) {
            return "ADMIN".equals(roleName);
        }
        return true;
    }
}
