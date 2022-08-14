package dev.dl.blogservice.application.mapper;

import dev.dl.blogservice.domain.dto.CommentDto;
import dev.dl.blogservice.domain.entity.Comment;
import dev.dl.common.helper.ObjectHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class CommentMapper implements BaseMapper<Comment, CommentDto> {

    private static volatile CommentMapper INSTANCE;

    private CommentMapper() {
    }

    public static synchronized CommentMapper getInstance() {
        if (Optional.ofNullable(INSTANCE).isEmpty()) {
            INSTANCE = new CommentMapper();
        }
        return INSTANCE;
    }

    @Override
    public Comment dtoToEntity(CommentDto dto) {
        if (Optional.ofNullable(dto).isEmpty()) {
            return null;
        }
        try {
            return ObjectHelper.mapObjects(dto, Comment.class);
        } catch (Exception e) {
            log.warn("EXCEPTION OCCUR WHEN MAPPING {}", e.getMessage());
            return null;
        }
    }

    @Override
    public CommentDto entityToDto(Comment entity) {
        if (Optional.ofNullable(entity).isEmpty()) {
            return null;
        }
        try {
            return ObjectHelper.mapObjects(entity, CommentDto.class);
        } catch (Exception e) {
            log.warn("EXCEPTION OCCUR WHEN MAPPING {}", e.getMessage());
            return null;
        }
    }
}
