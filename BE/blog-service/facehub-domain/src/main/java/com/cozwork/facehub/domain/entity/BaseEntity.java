package com.cozwork.facehub.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.MappedSuperclass;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@SuppressWarnings("com.haulmont.jpb.LombokDataInspection")
@Inheritance
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class BaseEntity {

    protected static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "updatedBy")
    private String updatedBy;

}
