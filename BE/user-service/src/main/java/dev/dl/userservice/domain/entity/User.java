package dev.dl.userservice.domain.entity;

import dev.dl.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@SuppressWarnings({"com.haulmont.jpb.LombokDataInspection", "JpaDataSourceORMInspection", "Lombok"})
@Entity
@Table(name = "user", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

}
