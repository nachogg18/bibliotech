package com.bibliotech.service;

import static com.bibliotech.repository.specifications.PublicationSpecifications.createSpecification;
import static org.springframework.data.jpa.domain.Specification.where;

import com.bibliotech.entity.Publication;
import com.bibliotech.repository.BaseRepository;
import com.bibliotech.repository.PublicationRepository;
import com.bibliotech.repository.specifications.Filter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PublicationServiceImpl extends BaseServiceImpl<Publication, Long> implements PublicationService {
    @Autowired
    private PublicationRepository publicationRepository;


    public PublicationServiceImpl(BaseRepository<Publication, Long> baseRepository) {
        super(baseRepository);
    }

    public List<Publication> findByParams(List<Filter> filterList) {
        return this.publicationRepository.findAll(getSpecificationFromFilters(filterList));
    }

    private Specification<Publication> getSpecificationFromFilters(List<Filter> filter) {
        Specification<Publication> specification =
                where(createSpecification(filter.remove(0)));
        for (Filter input : filter) {
            specification = specification.and(createSpecification(input));
        }
        return specification;
    }
}
