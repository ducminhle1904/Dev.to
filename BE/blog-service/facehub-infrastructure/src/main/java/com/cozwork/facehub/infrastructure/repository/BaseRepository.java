package com.cozwork.facehub.infrastructure.repository;

import com.cozwork.facehub.domain.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository<E extends BaseEntity> extends JpaRepository<E, Long> {
}
