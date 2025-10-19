package com.example.rbacdemo.security;

import com.example.rbacdemo.model.Action;

public interface ActionListener {
    boolean handle(Action action, String activeRole);
}
