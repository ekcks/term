package com.bips.reserve.domain.entity;

import lombok.Getter;
        import lombok.Setter;
        import org.springframework.data.annotation.CreatedDate;
        import org.springframework.data.annotation.LastModifiedDate;
        import org.springframework.data.jpa.domain.support.AuditingEntityListener;

        import javax.persistence.Column;
        import javax.persistence.EntityListeners;
        import javax.persistence.MappedSuperclass;
        import java.time.LocalDateTime;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createAt;
}