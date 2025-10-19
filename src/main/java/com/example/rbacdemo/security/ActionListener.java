package com.example.rbacdemo.security;

import com.example.rbacdemo.model.Action;

public interface ActionListener {
    /**
     * handle action; return true to allow, false to deny
     */
    boolean handle(Action action, String activeRole);
}
