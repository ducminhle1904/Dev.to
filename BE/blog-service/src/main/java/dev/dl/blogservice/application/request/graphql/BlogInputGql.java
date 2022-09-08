package dev.dl.blogservice.application.request.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogInputGql {
    private Long id;
    private Boolean active;
}
