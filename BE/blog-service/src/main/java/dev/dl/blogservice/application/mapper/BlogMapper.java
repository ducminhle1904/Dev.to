package dev.dl.blogservice.application.mapper;

import dev.dl.blogservice.domain.dto.BlogDto;
import dev.dl.blogservice.domain.entity.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BlogMapper extends BaseMapper<Blog, BlogDto> {

    BlogMapper INSTANCE = Mappers.getMapper(BlogMapper.class);

}
