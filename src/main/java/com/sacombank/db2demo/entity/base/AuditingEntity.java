package com.sacombank.db2demo.entity.base;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
public class AuditingEntity {
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private Timestamp createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private Timestamp modifiedDate;
}
