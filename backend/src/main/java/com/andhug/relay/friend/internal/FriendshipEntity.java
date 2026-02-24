package com.andhug.relay.friend.internal;

import com.andhug.relay.friend.api.models.FriendshipStatus;
import com.andhug.relay.profile.internal.ProfileEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "friendship")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendshipEntity {

    @EmbeddedId
    private FriendshipKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("requesterId")
    @JoinColumn(name = "requester_id")
    private ProfileEntity requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("addresseeId")
    @JoinColumn(name = "addressee_id")
    private ProfileEntity addressee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private FriendshipStatus status = FriendshipStatus.PENDING;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
