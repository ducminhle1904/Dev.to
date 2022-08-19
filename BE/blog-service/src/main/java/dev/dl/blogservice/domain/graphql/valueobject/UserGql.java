package dev.dl.blogservice.domain.graphql.valueobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserGql {
    private String id;
    private String firstName;
    private String lastName;
}
