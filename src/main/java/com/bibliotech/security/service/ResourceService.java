package com.bibliotech.security.service;


import com.bibliotech.security.entity.Resource;
import java.util.List;
import java.util.Optional;

public interface ResourceService {

    List<Resource> getAllResources();

    Optional<Resource> getResourcesById(Long id);

    Optional<Resource> getResourceByName(String name);

    Resource saveResource(Resource resource);
}