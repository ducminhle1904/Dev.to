package dev.dl.blogservice.domain.graphql;

import dev.dl.blogservice.domain.graphql.valueobject.UserGql;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogGql {
    private Long id;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String title;
    private String body;
    private UserGql user;
}
