package com.bibliotech.repository.specifications;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class EspecificationFilter {
    private String field;
    private QueryOperator operator;
    private String value;
    private List<String> values;//Used in case of IN operator
}
