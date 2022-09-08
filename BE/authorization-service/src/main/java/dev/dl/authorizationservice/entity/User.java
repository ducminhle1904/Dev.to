package dev.dl.authorizationservice.entity;

import dev.dl.authorizationservice.dto.UserRoleDto;
import dev.dl.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"com.haulmont.jpb.LombokDataInspection", "JpaDataSourceORMInspection", "Lombok"})
@Entity
@Table(name = "user", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = "userRoleDto",
                        classes = {
                                @ConstructorResult(
                                        targetClass = UserRoleDto.class,
                                        columns = {
                                                @ColumnResult(name = "userid", type = UUID.class),
                                                @ColumnResult(name = "name", type = String.class),
                                                @ColumnResult(name = "active", type = Boolean.class),
                                                @ColumnResult(name = "role", type = String.class)
                                        }
                                )
                        }
                )
        }
)
@NamedNativeQueries(
        value = {
                @NamedNativeQuery(
                        name = "getUserRoleDto",
                        query = "SELECT u.user_id as userid, " +
                                "CONCAT(u.first_name, ' ', u.last_name) as name, " +
                                "u.active as active, " +
                                "r.role_name as role " +
                                "FROM \"public\".\"user\" u LEFT JOIN \"public\".\"role_user\" ru ON ru.\"user_id\" = u.\"id\" RIGHT JOIN \"public\".\"role\" r ON r.\"id\" = ru.\"role_id\" WHERE u.\"user_id\" = :userId",
                        resultSetMapping = "userRoleDto"
                )
        }
)
public class User extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RoleUser> roleUsers;

}
