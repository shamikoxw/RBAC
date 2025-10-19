package com.example.rbacdemo.security;

import com.example.rbacdemo.model.Action;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActionDispatcher {
    private final List<ActionListener> listeners = new ArrayList<>();
    public void register(ActionListener l) { listeners.add(l); }

    public boolean dispatch(Action action, String activeRole) {
        for (ActionListener l: listeners) {
            boolean ok = l.handle(action, activeRole);
            if (!ok) return false;
        }
        return true;
    }
}
