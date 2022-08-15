package dev.dl.authorizationservice.entity;

import dev.dl.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@SuppressWarnings({"com.haulmont.jpb.LombokDataInspection", "JpaDataSourceORMInspection", "Lombok"})
@Entity
@Table(name = "role", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role extends BaseEntity {

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Column(name = "role_description")
    private String roleDescription;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RoleUser> roleUsers;

}
