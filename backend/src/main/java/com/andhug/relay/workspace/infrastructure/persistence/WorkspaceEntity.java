package com.andhug.relay.workspace.infrastructure.persistence;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workspace")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    private UUID ownerId;

    @CreatedDate
    private LocalDateTime createdAt;
}
