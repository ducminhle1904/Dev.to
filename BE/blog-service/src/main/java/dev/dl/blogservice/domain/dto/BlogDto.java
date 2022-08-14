package dev.dl.blogservice.domain.dto;

import dev.dl.blogservice.domain.entity.Comment;
import dev.dl.common.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("Lombok")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogDto extends BaseDto {
    private UUID userId;
    private String title;
    private String body;
}
