package com.example.rbacdemo.repo;

import com.example.rbacdemo.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {}
