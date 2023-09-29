package com.bibliotech.security.service.impl;

import com.bibliotech.security.entity.Resource;
import com.bibliotech.security.repository.ResourceRepository;
import com.bibliotech.security.service.ResourceService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;


    @Override
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    @Override
    public Optional<Resource> getResourcesById(Long id) {
        return resourceRepository.findById(id);
    }

    @Override
    public Optional<Resource> getResourceByName(String name) {
        return resourceRepository.findByName(name);
    }

    @Override
    public Resource saveResource(Resource resource) {
        return resourceRepository.save(resource);
    }
}
