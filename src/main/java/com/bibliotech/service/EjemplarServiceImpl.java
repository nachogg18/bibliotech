package com.bibliotech.service;

import com.bibliotech.entity.Ejemplar;
import com.bibliotech.repository.BaseRepository;
import com.bibliotech.repository.EjemplarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EjemplarServiceImpl  extends BaseServiceImpl<Ejemplar, Long> implements EjemplarService{
    @Autowired
    private EjemplarRepository ejemplarRepository;

    public EjemplarServiceImpl(BaseRepository<Ejemplar, Long> baseRepository) {
        super(baseRepository);
    }


}
