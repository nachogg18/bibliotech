package com.bibliotech.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LinkMobileDto {
    private String url;
    private String instrucciones;
}
