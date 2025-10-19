package com.example.rbacdemo.model;

public class ResourceItem {
    private final String id;
    private final String ownerUsername;
    private boolean shared = false;
    private boolean confidential = false; // MAC flag: if confidential, only admin can share

    public ResourceItem(String id, String ownerUsername) {
        this.id = id; this.ownerUsername = ownerUsername;
    }
    public String getId() { return id; }
    public String getOwnerUsername() { return ownerUsername; }
    public boolean isShared() { return shared; }
    public void setShared(boolean shared) { this.shared = shared; }
    public boolean isConfidential() { return confidential; }
    public void setConfidential(boolean confidential) { this.confidential = confidential; }
}
