package com.andhug.relay.room.domain.service;

import com.andhug.relay.room.application.mapper.RoomMapper;
import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.infrastructure.persistence.RoomEntity;
import com.andhug.relay.room.infrastructure.persistence.RoomJpaRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomDomainService {

  private final RoomJpaRepository roomRepository;

  private final RoomMapper roomMapper;

  @Transactional(readOnly = true)
  public List<Room> getDirectMessages(UUID profileId) {

    return null;

    // List<RoomEntity> dbRooms = roomRepository.findByParticipants_ProfileIdAndType(profileId,
    // RoomType.TEXT);

    // List<Room> rooms = new ArrayList<>();

    // for  (RoomEntity room : dbRooms) {
    //     Room domainRoom = roomMapper.toDomain(room);

    //     rooms.add(domainRoom);
    // }

    // return rooms;
  }

  @Transactional(readOnly = true)
  public List<Room> getRoomsByWorkspaceId(UUID workspaceId) {

    List<RoomEntity> roomEntities = roomRepository.findByWorkspaceId(workspaceId);

    List<Room> rooms = roomEntities.stream().map(roomMapper::toDomain).toList();

    return rooms;
  }
}
