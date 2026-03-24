package com.andhug.relay.room.domain.service;

import com.andhug.relay.profile.Profile;
import com.andhug.relay.profile.ProfileService;
import com.andhug.relay.profile.internal.ProfileEntity;
import com.andhug.relay.profile.internal.ProfileRepository;
import com.andhug.relay.room.application.mapper.RoomMapper;
import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.domain.model.RoomType;
import com.andhug.relay.room.infrastructure.persistence.RoomEntity;
import com.andhug.relay.room.infrastructure.persistence.RoomProfileEntity;
import com.andhug.relay.room.infrastructure.persistence.RoomProfileKey;
import com.andhug.relay.room.infrastructure.persistence.RoomProfileJpaRepository;
import com.andhug.relay.room.infrastructure.persistence.RoomJpaRepository;
import com.andhug.relay.workspace.domain.event.WorkspaceCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomDomainService {

    private final ProfileService profileService;

    private final RoomJpaRepository roomRepository;

    private final RoomProfileJpaRepository roomProfileRepository;

    private final ProfileRepository profileRepository;

    private final RoomMapper roomMapper;

    @Transactional(readOnly = true)
    public List<Room> getDirectMessages(UUID profileId) {

        List<RoomEntity> dbRooms = roomRepository.findByParticipants_ProfileIdAndType(profileId, RoomType.DM);

        List<Room> rooms = new ArrayList<>();

        for  (RoomEntity room : dbRooms) {
            Room domainRoom = roomMapper.toDomain(room);

            List<UUID> participantIds = room.getParticipants().stream()
                .map(rp -> rp.getId().getProfileId())
                .toList();

            Map<UUID, Profile> participants = profileService.getProfiles(participantIds);

            for (Profile participant : participants.values()) {
                domainRoom.addParticipant(participant);
            }

            rooms.add(domainRoom);
        }

        return rooms;
    }

    @Transactional
    public Room createDirectMessage(Set<UUID> participantIds) {

        var roomEntity = RoomEntity.builder()
            .type(RoomType.DM)
            .build();

        RoomEntity saved = roomRepository.save(roomEntity);

        participantIds.forEach(participantId -> joinDirectMessage(participantId, saved.getId()));

        return roomMapper.toDomain(saved);
    }

    @Transactional
    public void joinDirectMessage(UUID profileId, UUID roomId) {

        RoomEntity roomEntity = roomRepository.getReferenceById(roomId);

        ProfileEntity profileEntity = profileRepository.getReferenceById(profileId);

        var roomProfileEntity = RoomProfileEntity.builder()
            .id(RoomProfileKey.builder()
                .roomId(roomId)
                .profileId(profileId)
                .build())
            .room(roomEntity)
            .profile(profileEntity)
            .build();

        roomProfileRepository.save(roomProfileEntity);
    }

    @Transactional(readOnly = true)
    public List<Room> getRoomsByWorkspaceId(UUID workspaceId) {

        List<RoomEntity> roomEntities = roomRepository.findByWorkspaceId(workspaceId);

        log.info("before mapping: {}", roomEntities.get(0).getId().toString());

        List<Room> rooms = roomEntities.stream()
            .map(roomMapper::toDomain)
            .toList();

        log.info("after mapping: {}", rooms.get(0).getId().toString());

        return rooms;
    }

    @Async
    @EventListener
    void onWorkspaceCreated(WorkspaceCreatedEvent event) {

        var roomEntity = RoomEntity.builder()
            .name("general")
            .workspaceId(event.workspaceId())
            .build();

        roomRepository.save(roomEntity);

        log.info("Room created: {}", roomEntity);
    }
}
