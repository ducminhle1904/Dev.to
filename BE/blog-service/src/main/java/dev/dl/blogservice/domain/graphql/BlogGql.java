package dev.dl.blogservice.domain.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogGql {
    private String id;
    private String active;
    private String createdAt;
    private String updatedAt;
    private String createdBy;
    private String updatedBy;
    private String userId;
    private String title;
    private String body;
}
