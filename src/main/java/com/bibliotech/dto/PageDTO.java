package com.bibliotech.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PageDTO<T> {
    private List<T> content = new ArrayList<>();
    private Integer totalPages;
    private String previousUrl;
    private String nextUrl;
}
