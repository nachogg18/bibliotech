package com.bibliotech.mapper;

import com.bibliotech.dto.PublicationSearchRequestDTO;
import com.bibliotech.repository.specifications.EspecificationFilter;
import com.bibliotech.repository.specifications.Publication_;
import com.bibliotech.repository.specifications.QueryOperator;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class SearchRequestMapper {
    public static List<EspecificationFilter> toPublicationEspecificationFilter(PublicationSearchRequestDTO publicationSearchRequestDTO) {
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

        if (StringUtils.isNotBlank(publicationSearchRequestDTO.startDateAfter())) {
            EspecificationFilter startDateEspFilter = EspecificationFilter.builder()
                    .field(Publication_.START_DATE)
                    .operator(QueryOperator.GREATER_OR_EQUAL_THAN_DATETIME)
                    .value(publicationSearchRequestDTO.startDateAfter())
                    .build();

            filterList.add(startDateEspFilter);
        }

        if (StringUtils.isNotBlank(publicationSearchRequestDTO.startDateBefore())) {
            EspecificationFilter startDateEspFilter = EspecificationFilter.builder()
                    .field(Publication_.START_DATE)
                    .operator(QueryOperator.LESS_OR_EQUAL_THAN_DATETIME)
                    .value(publicationSearchRequestDTO.startDateBefore())
                    .build();

            filterList.add(startDateEspFilter);
        }

        if (StringUtils.isNotBlank(publicationSearchRequestDTO.endDateAfter())) {
            EspecificationFilter startDateEspFilter = EspecificationFilter.builder()
                    .field(Publication_.START_DATE)
                    .operator(QueryOperator.GREATER_OR_EQUAL_THAN_DATETIME)
                    .value(publicationSearchRequestDTO.endDateAfter())
                    .build();

            filterList.add(startDateEspFilter);
        }

        if (StringUtils.isNotBlank(publicationSearchRequestDTO.endDateBefore())) {
            EspecificationFilter startDateEspFilter = EspecificationFilter.builder()
                    .field(Publication_.START_DATE)
                    .operator(QueryOperator.LESS_OR_EQUAL_THAN_DATETIME)
                    .value(publicationSearchRequestDTO.endDateBefore())
                    .build();

            filterList.add(startDateEspFilter);
        }

        if (StringUtils.isNotBlank(publicationSearchRequestDTO.publicationDateAfter())) {
            EspecificationFilter startDateEspFilter = EspecificationFilter.builder()
                    .field(Publication_.START_DATE)
                    .operator(QueryOperator.GREATER_OR_EQUAL_THAN_DATETIME)
                    .value(publicationSearchRequestDTO.publicationDateAfter())
                    .build();

            filterList.add(startDateEspFilter);
        }

        if (StringUtils.isNotBlank(publicationSearchRequestDTO.publicationDateBefore())) {
            EspecificationFilter startDateEspFilter = EspecificationFilter.builder()
                    .field(Publication_.START_DATE)
                    .operator(QueryOperator.LESS_OR_EQUAL_THAN_DATETIME)
                    .value(publicationSearchRequestDTO.publicationDateBefore())
                    .build();

            filterList.add(startDateEspFilter);
        }

        return filterList;
    }



}
