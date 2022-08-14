package dev.dl.blogservice.domain.entity;

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
import java.util.UUID;

@SuppressWarnings({"com.haulmont.jpb.LombokDataInspection", "JpaDataSourceORMInspection", "Lombok"})
@Entity
@Table(name = "blog")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Blog extends BaseEntity {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", columnDefinition = "TEXT", nullable = false)
    private String body;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<Comment> comments;

}
