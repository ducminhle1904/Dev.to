package dev.dl.blogservice.application.mapper;

import dev.dl.blogservice.domain.dto.BlogDto;
import dev.dl.blogservice.domain.entity.Blog;
import dev.dl.common.helper.ObjectHelper;

import java.util.Optional;

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
    public Blog dtoToEntity(BlogDto dto) throws Exception {
        return ObjectHelper.mapObjects(dto, Blog.class);
    }

    @Override
    public BlogDto entityToDto(Blog entity) throws Exception {
        return ObjectHelper.mapObjects(entity, BlogDto.class);
    }
}
