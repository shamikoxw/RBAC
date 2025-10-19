package com.example.rbacdemo.model;

public class Permission {
    // fine-grained: source + target + operation
    private final String source; // event/source object type (e.g., "UI", "SYSTEM", "USER")
    private final String target; // resource id or resource type (e.g., "message", "report", "resource:res1")
    private final String operation; // e.g., "create","read","update","delete","share","send"

    public Permission(String source, String target, String operation) {
        this.source = source; this.target = target; this.operation = operation;
    }
    public String getSource() { return source; }
    public String getTarget() { return target; }
    public String getOperation() { return operation; }

    // convenience match: supports wildcard "*"
    public boolean matches(Action a) {
        boolean s = "*".equals(source) || source.equals(a.getSource());
        boolean t = "*".equals(target) || target.equals(a.getTarget());
        boolean o = "*".equals(operation) || operation.equals(a.getOperation());
        return s && t && o;
    }

    @Override
    public String toString() {
        return source + ":" + target + ":" + operation;
    }
}
