package com.andhug.relay.room.infrastructure.web;

import com.andhug.relay.message.api.dto.CreateMessageHttpRequest;
import com.andhug.relay.message.api.dto.CreateMessageRequest;
import com.andhug.relay.message.api.dto.MessageDto;
import com.andhug.relay.message.api.MessageService;
import com.andhug.relay.profile.Profile;
import com.andhug.relay.room.application.RoomApplicationService;
import com.andhug.relay.room.application.command.UpdateRoomCommand;
import com.andhug.relay.room.application.mapper.RoomMapper;
import com.andhug.relay.room.domain.model.Room;
import com.andhug.relay.room.infrastructure.web.dto.RoomDto;
import com.andhug.relay.room.infrastructure.web.dto.request.UpdateRoomRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Room Controller", description = "APIs for managing rooms")
public class RoomController {

    private final RoomApplicationService roomApplicationService;

    private final MessageService messageService;

    private final RoomMapper roomMapper;

    @GetMapping(value = "/{room-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRoom(
            @Parameter(in = ParameterIn.PATH, required = true, name = "room-id", schema = @Schema(type = "string")) @PathVariable("room-id") UUID roomId) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @DeleteMapping(value = "/{room-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteRoom(
            @Parameter(in = ParameterIn.PATH, required = true, name = "room-id", schema = @Schema(type = "string")) @PathVariable("room-id") UUID roomId) {

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @PatchMapping(value = "/{room-id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomDto> updateRoom(
            @Parameter(in = ParameterIn.PATH, required = true, name = "room-id", schema = @Schema(type = "string")) @PathVariable("room-id") UUID roomId,
            @RequestBody UpdateRoomRequest updateRequest) {

        var command = UpdateRoomCommand.builder()
            .roomId(roomId)
            .name(Optional.ofNullable(updateRequest.name()))
            .build();

        Room updatedRoom = roomApplicationService.updateRoom(command);

        return ResponseEntity.ok(roomMapper.toDto(updatedRoom));
    }

    @GetMapping(value = "/{room-id}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MessageDto>> getMessages(
            @Parameter(in = ParameterIn.PATH, required = true, name = "room-id", schema = @Schema(type = "string")) @PathVariable("room-id") UUID roomId,
            @RequestParam(required = false, defaultValue = "50") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset) {

        return ResponseEntity.ok(messageService.getMessages(roomId, offset, limit));
    }

    @PostMapping(value = "/{room-id}/messages", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> createMessage(
            @AuthenticationPrincipal Profile profile,
            @Parameter(in = ParameterIn.PATH, required = true, name = "room-id", schema = @Schema(type = "string")) @PathVariable("room-id") UUID roomId,
            @RequestBody CreateMessageHttpRequest httpRequest) {

        var serviceRequest = new CreateMessageRequest(UUID.randomUUID(), roomId, httpRequest.content());

        return ResponseEntity.ok(messageService.createMessage(serviceRequest, profile));
    }
}
