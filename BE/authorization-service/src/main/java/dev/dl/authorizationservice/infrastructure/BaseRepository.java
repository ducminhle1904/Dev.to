package dev.dl.authorizationservice.infrastructure;

import dev.dl.common.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository<E extends BaseEntity> extends JpaRepository<E, Long> {
}
