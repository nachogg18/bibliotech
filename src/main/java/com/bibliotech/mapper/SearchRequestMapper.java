package com.bibliotech.mapper;

import com.bibliotech.dto.PublicationSearchRequestDTO;
import com.bibliotech.repository.specifications.EspecificationFilter;
import com.bibliotech.repository.specifications.Publication_;
import com.bibliotech.repository.specifications.QueryOperator;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
public class SearchRequestMapper {
    public static List<EspecificationFilter> toEspecificationFilter(PublicationSearchRequestDTO publicationSearchRequestDTO) {
    List<EspecificationFilter> filterList = new ArrayList<>();

        if (StringUtils.isNotBlank(publicationSearchRequestDTO.id())) {
            EspecificationFilter idEspFilter = EspecificationFilter.builder()
                    .field(Publication_.ID)
                    .operator(QueryOperator.EQUALS_STRING)
                    .value(publicationSearchRequestDTO.id())
                    .build();

            filterList.add(idEspFilter);
        }

        if (StringUtils.isNotBlank(publicationSearchRequestDTO.name())) {
            EspecificationFilter nameEspFilter = EspecificationFilter.builder()
                    .field(Publication_.NAME)
                    .operator(QueryOperator.EQUALS_STRING)
                    .value(publicationSearchRequestDTO.name())
                    .build();

            filterList.add(nameEspFilter);
        }

        if (StringUtils.isNotBlank(publicationSearchRequestDTO.isbn())) {
            EspecificationFilter isbnEspFilter = EspecificationFilter.builder()
                    .field(Publication_.ISBN)
                    .operator(QueryOperator.EQUALS_STRING)
                    .value(publicationSearchRequestDTO.isbn())
                    .build();

            filterList.add(isbnEspFilter);
        }

        if (StringUtils.isNotBlank(publicationSearchRequestDTO.isbn())) {
            EspecificationFilter isbnEspFilter = EspecificationFilter.builder()
                    .field(Publication_.ISBN)
                    .operator(QueryOperator.EQUALS_STRING)
                    .value(publicationSearchRequestDTO.isbn())
                    .build();

            filterList.add(isbnEspFilter);
        }

        return filterList;
    }



}
