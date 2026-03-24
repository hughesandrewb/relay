package com.andhug.relay.room.api;

import com.andhug.relay.profile.Profile;
import com.andhug.relay.profile.ProfileService;
import com.andhug.relay.profile.internal.ProfileEntity;
import com.andhug.relay.profile.internal.ProfileRepository;
import com.andhug.relay.room.api.events.UpdateRoom;
import com.andhug.relay.room.internal.*;
import com.andhug.relay.workspace.domain.event.WorkspaceCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final ProfileService profileService;

    private final RoomRepository roomRepository;

    private final RoomProfileRepository roomProfileRepository;

    private final ProfileRepository profileRepository;

    private final RoomMapper roomMapper;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Room updateRoom(UpdateRoomCommand command) {

        RoomEntity roomEntity = roomRepository.getReferenceById(command.roomId());

        roomEntity.setName(command.name());

        RoomEntity updated = roomRepository.save(roomEntity);

        Room saved = roomMapper.toDomain(updated);

        eventPublisher.publishEvent(UpdateRoom.builder()
            .roomId(saved.getId())
            .room(saved)
            .build());

        return saved;
    }

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

            domainRoom.setParticipants(new ArrayList<>(participants.values()));

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

        return roomRepository.findByWorkspaceId(workspaceId)
            .stream()
            .map(roomMapper::toDomain)
            .toList();
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
