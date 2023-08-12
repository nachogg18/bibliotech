package com.bibliotech.service;

import static com.bibliotech.repository.specifications.PublicationSpecifications.createSpecification;
import static org.springframework.data.jpa.domain.Specification.where;

import com.bibliotech.entity.Publication;
import com.bibliotech.repository.BaseRepository;
import com.bibliotech.repository.PublicationRepository;
import com.bibliotech.repository.specifications.EspecificationFilter;
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

    public List<Publication> findByParams(List<EspecificationFilter> especificationFilterList) {
        return this.publicationRepository.findAll(getSpecificationFromFilters(especificationFilterList));
    }

    private Specification<Publication> getSpecificationFromFilters(List<EspecificationFilter> especificationFilter) {
        Specification<Publication> specification =
                where(createSpecification(especificationFilter.remove(0)));
        for (EspecificationFilter input : especificationFilter) {
            specification = specification.and(createSpecification(input));
        }
        return specification;
    }
}
