package dev.dl.blogservice.application.mapper;

import dev.dl.blogservice.domain.dto.BlogDto;
import dev.dl.blogservice.domain.entity.Blog;
import dev.dl.common.helper.ObjectHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class BlogMapper implements BaseMapper<Blog, BlogDto> {

    private static volatile BlogMapper INSTANCE;

    private BlogMapper() {
    }

    public static synchronized BlogMapper getInstance() {
        if (Optional.ofNullable(INSTANCE).isEmpty()) {
            INSTANCE = new BlogMapper();
        }
        return INSTANCE;
    }

    @Override
    public Blog dtoToEntity(BlogDto dto) {
        if (Optional.ofNullable(dto).isEmpty()) {
            return null;
        }
        try {
            return ObjectHelper.mapObjects(dto, Blog.class);
        } catch (Exception e) {
            log.warn("EXCEPTION OCCUR WHEN MAPPING {}", e.getMessage());
            return null;
        }
    }

    @Override
    public BlogDto entityToDto(Blog entity) {
        if (Optional.ofNullable(entity).isEmpty()) {
            return null;
        }
        try {
            return ObjectHelper.mapObjects(entity, BlogDto.class);
        } catch (Exception e) {
            log.warn("EXCEPTION OCCUR WHEN MAPPING {}", e.getMessage());
            return null;
        }
    }
}
