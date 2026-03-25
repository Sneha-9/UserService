package com.sneha.model;

import com.sneha.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity

@Table(name = Constants.USER_DAO_TABLE_NAME)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(exclude = {"createdAt", "updatedAt"})
public class UserDao {

    private String name;
    private String email;

    @Id
    @UuidGenerator
    private String id;

    @CreationTimestamp

    @Column(name = Constants.USER_DAO_COLUMN_NAME_CREATED_AT, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp

    @Column(name = Constants.USER_DAO_COLUMN_NAME_UPDATED_AT)
    private LocalDateTime updatedAt;

}
