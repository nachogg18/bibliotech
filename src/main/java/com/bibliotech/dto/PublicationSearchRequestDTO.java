package com.bibliotech.dto;

import com.bibliotech.utils.DateTimeUtils;
import jakarta.validation.ValidationException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

public record PublicationSearchRequestDTO (
        String name,
        String id,
        String startDateAfter,
        String startDateBefore,
        String endDateAfter,
        String endDateBefore,
        String publicationDateAfter,
        String publicationDateBefore,
        String isbn
        )
{

    public void validateRequest() {
        if (StringUtils.isBlank(this.id)
                && StringUtils.isBlank(this.isbn)
                && StringUtils.isBlank(this.name)
                && StringUtils.isBlank(this.startDateBefore)
                && StringUtils.isBlank(this.startDateAfter)
                && StringUtils.isBlank(this.endDateBefore)
                && StringUtils.isBlank(this.endDateAfter)
                && StringUtils.isBlank(this.publicationDateBefore)
                && StringUtils.isBlank(this.publicationDateAfter)
        ) {
            String msg = "request_is_empty";
            throw new ValidationException(msg);
        }

        validateDates();

    }

    private Optional<LocalDateTime> validateDate(String stringDate) throws DateTimeException {
        if (StringUtils.isNotBlank(stringDate)) {
            return Optional.of(DateTimeUtils.fromDate(stringDate));
        }
        return Optional.empty();
    }

    private void validateDates() {
        try {
            validateDate(this.startDateAfter);
            validateDate(this.startDateBefore);
            validateDate(this.endDateAfter);
            validateDate(this.endDateBefore);
            validateDate(this.publicationDateAfter);
            validateDate(this.publicationDateBefore);
        } catch (DateTimeParseException e) {
            String msg = "invalid_format_datetimes";
            throw new ValidationException(msg);
        }
    }


}


