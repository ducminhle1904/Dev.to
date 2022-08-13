package dev.dl.blogservice.domain.dto;

import dev.dl.blogservice.domain.entity.Blog;
import dev.dl.blogservice.domain.entity.Comment;
import dev.dl.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@SuppressWarnings("Lombok")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDto extends BaseDto {
    private UUID userId;
    private String comment;
    private Comment parentComment;
    private Blog blog;
}
