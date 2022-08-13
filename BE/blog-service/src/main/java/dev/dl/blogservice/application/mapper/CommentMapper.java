package dev.dl.blogservice.application.mapper;

import dev.dl.blogservice.domain.dto.CommentDto;
import dev.dl.blogservice.domain.entity.Comment;
import dev.dl.common.helper.ObjectHelper;

import java.util.Optional;

public class CommentMapper implements BaseMapper<Comment, CommentDto> {

    private static volatile CommentMapper INSTANCE;

    private CommentMapper() {
    }

    private static synchronized CommentMapper getInstance() {
        if (Optional.ofNullable(INSTANCE).isEmpty()) {
            INSTANCE = new CommentMapper();
        }
        return INSTANCE;
    }

    @Override
    public Comment dtoToEntity(CommentDto dto) throws Exception {
        return ObjectHelper.mapObjects(dto, Comment.class);
    }

    @Override
    public CommentDto entityToDto(Comment entity) throws Exception {
        return ObjectHelper.mapObjects(entity, CommentDto.class);
    }
}
