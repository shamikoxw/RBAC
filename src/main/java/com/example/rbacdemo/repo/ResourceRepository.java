package com.example.rbacdemo.repo;

import com.example.rbacdemo.entity.ResourceItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<ResourceItemEntity, String> {}
