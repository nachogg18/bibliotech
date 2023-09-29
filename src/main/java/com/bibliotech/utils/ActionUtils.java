package com.bibliotech.utils;

import com.bibliotech.security.entity.Action;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ActionUtils {
    public static Set<String> getActionNames() {
        return Arrays.stream(Action.values())
                .map(action -> action.name())
                .collect(Collectors.toSet());
    }
    
}
