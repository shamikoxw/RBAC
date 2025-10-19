package com.example.rbacdemo.model;

public class Action {
    private final String actorUsername; // who triggered
    private final String source; // event source
    private final String target; // what resource or object
    private final String operation; // operation name
    private final String metadata; // optional

    public Action(String actorUsername, String source, String target, String operation, String metadata) {
        this.actorUsername = actorUsername;
        this.source = source;
        this.target = target;
        this.operation = operation;
        this.metadata = metadata;
    }
    public String getActorUsername() { return actorUsername; }
    public String getSource() { return source; }
    public String getTarget() { return target; }
    public String getOperation() { return operation; }
    public String getMetadata() { return metadata; }
}
