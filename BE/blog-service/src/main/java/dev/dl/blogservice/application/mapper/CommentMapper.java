package dev.dl.blogservice.application.mapper;

import dev.dl.blogservice.domain.dto.CommentDto;
import dev.dl.blogservice.domain.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper extends BaseMapper<Comment, CommentDto> {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

}
