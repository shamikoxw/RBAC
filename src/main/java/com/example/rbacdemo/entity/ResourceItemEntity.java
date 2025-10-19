package com.example.rbacdemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resources")
public class ResourceItemEntity {
    @Id
    private String id;

    private String ownerUsername;

    private boolean shared = false;

    private boolean confidential = false;

    public ResourceItemEntity() {}
    public ResourceItemEntity(String id, String ownerUsername) {
        this.id = id; this.ownerUsername = ownerUsername;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }
    public boolean isShared() { return shared; }
    public void setShared(boolean shared) { this.shared = shared; }
    public boolean isConfidential() { return confidential; }
    public void setConfidential(boolean confidential) { this.confidential = confidential; }
}
