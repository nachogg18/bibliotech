package com.bibliotech.dto;

import jakarta.validation.ValidationException;
import org.apache.commons.lang3.StringUtils;

public record PublicationSearchRequestDTO (
        String name,
        String id,
        String startDate,
        String endDate,
        String publicationDate,
        String isbn
        )
{

    public void validateRequest() {
        if (StringUtils.isBlank(this.id)
                && StringUtils.isBlank(this.isbn)
                && StringUtils.isBlank(this.name)
                && StringUtils.isBlank(this.startDate)
                && StringUtils.isBlank(this.endDate)
                && StringUtils.isBlank(this.publicationDate)
        ) {
            String msg = "request_is_empty";
            throw new ValidationException(msg);
        }
    }

}


