package com.example.rbacdemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class PermissionEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String source;
    private String target;
    private String operation;

    public PermissionEntity() {}
    public PermissionEntity(String source, String target, String operation) {
        this.source = source; this.target = target; this.operation = operation;
    }

    public Long getId() { return id; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }

    public boolean matches(String actionSource, String actionTarget, String actionOperation) {
        boolean s = "*".equals(source) || source.equals(actionSource);
        boolean t = "*".equals(target) || target.equals(actionTarget) || 
                 (target.endsWith("*") && actionTarget.startsWith(target.substring(0, target.length() - 1)));
        boolean o = "*".equals(operation) || operation.equals(actionOperation);
        return s && t && o;
    }

    @Override
    public String toString() {
        return source + ":" + target + ":" + operation;
    }
}
