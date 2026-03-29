package com.andhug.relay.room.infrastructure.web;

import com.andhug.relay.message.application.command.CreateMessageCommand;
import com.andhug.relay.message.application.handler.CreateMessageHandler;
import com.andhug.relay.message.application.query.GetMessagesQuery;
import com.andhug.relay.message.application.service.MessageQueryService;
import com.andhug.relay.message.domain.model.MessageId;
import com.andhug.relay.message.infrastructure.web.dto.CreateMessageHttpRequest;
import com.andhug.relay.message.infrastructure.web.dto.MessageDto;
import com.andhug.relay.profile.domain.model.Profile;
import com.andhug.relay.room.application.command.UpdateRoomCommand;
import com.andhug.relay.room.application.service.RoomCommandService;
import com.andhug.relay.room.application.service.RoomQueryService;
import com.andhug.relay.room.infrastructure.web.dto.RoomDto;
import com.andhug.relay.room.infrastructure.web.dto.request.UpdateRoomRequest;
import com.andhug.relay.shared.domain.model.RoomId;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Room Controller", description = "APIs for managing rooms")
public class RoomController {

  private final RoomCommandService roomCommandService;

  private final RoomQueryService roomQueryService;

  private final CreateMessageHandler createMessageHandler;

  private final MessageQueryService messageQueryService;

  @GetMapping(value = "/{room-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getRoom(
      @Parameter(
              in = ParameterIn.PATH,
              required = true,
              name = "room-id",
              schema = @Schema(type = "string"))
          @PathVariable("room-id")
          UUID roomId) {

    throw new UnsupportedOperationException("Not supported yet.");
  }

  @DeleteMapping(value = "/{room-id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> deleteRoom(
      @Parameter(
              in = ParameterIn.PATH,
              required = true,
              name = "room-id",
              schema = @Schema(type = "string"))
          @PathVariable("room-id")
          UUID roomId) {

    throw new UnsupportedOperationException("Not supported yet.");
  }

  @PatchMapping(
      value = "/{room-id}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RoomDto> updateRoom(
      @Parameter(
              in = ParameterIn.PATH,
              required = true,
              name = "room-id",
              schema = @Schema(type = "string"))
          @PathVariable("room-id")
          UUID roomId,
      @RequestBody UpdateRoomRequest updateRequest) {

    var command =
        UpdateRoomCommand.builder()
            .roomId(RoomId.of(roomId))
            .name(Optional.ofNullable(updateRequest.name()))
            .build();

    RoomId updatedRoomId = roomCommandService.updateRoom(command);

    RoomDto updatedRoom = roomQueryService.getRoom(updatedRoomId);

    return ResponseEntity.ok(updatedRoom);
  }

  @GetMapping(value = "/{room-id}/messages", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MessageDto>> getMessages(
      @Parameter(
              in = ParameterIn.PATH,
              required = true,
              name = "room-id",
              schema = @Schema(type = "string"))
          @PathVariable("room-id")
          UUID roomId,
      @RequestParam(required = false, defaultValue = "50") int limit,
      @RequestParam(required = false, defaultValue = "0") int offset) {

    var query = GetMessagesQuery.latest(roomId, limit);

    List<MessageDto> messages = messageQueryService.getMessages(query);

    return ResponseEntity.ok(messages);
  }

  @PostMapping(
      value = "/{room-id}/messages",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MessageDto> createMessage(
      @AuthenticationPrincipal Profile profile,
      @Parameter(
              in = ParameterIn.PATH,
              required = true,
              name = "room-id",
              schema = @Schema(type = "string"))
          @PathVariable("room-id")
          UUID roomId,
      @RequestBody CreateMessageHttpRequest request) {

    var createMessageCommand =
        CreateMessageCommand.builder()
            .authorId(profile.getId())
            .roomId(RoomId.of(roomId))
            .content(request.content())
            .build();

    MessageId messageId = createMessageHandler.handle(createMessageCommand);

    return ResponseEntity.ok(messageQueryService.getMessage(messageId));
  }
}
