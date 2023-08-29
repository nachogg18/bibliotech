package com.bibliotech.mapper;

import com.bibliotech.dto.PageDTO;
import com.bibliotech.utils.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListToPageDTOMapper<Y, T> {
    private final PageUtil pageUtil;

    public ListToPageDTOMapper(PageUtil pageUtil) {
        this.pageUtil = pageUtil;
    }

    public PageDTO<T> toPageDTO(int page, Page<Y> entitiesPage, List<T> entitiesDTO) {
        PageDTO<T> pageDTO = new PageDTO<>();
        pageDTO.setTotalPages(entitiesPage.getTotalPages());
        pageDTO.setContent(entitiesDTO);
        if (entitiesPage.hasPrevious())
            pageDTO.setPreviousUrl(pageUtil.getPreviousUrl(page));
        if (entitiesPage.hasNext())
            pageDTO.setNextUrl(pageUtil.getNextUrl(page));
        return pageDTO;
    }
}
