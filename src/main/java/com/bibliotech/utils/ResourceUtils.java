package com.bibliotech.utils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ResourceUtils {

    public static Set<String> getResourceNames() {
        return Arrays.stream(ResourceNames.values())
                .map(resourceName -> resourceName.name())
                .collect(Collectors.toSet());
    }

    
}
