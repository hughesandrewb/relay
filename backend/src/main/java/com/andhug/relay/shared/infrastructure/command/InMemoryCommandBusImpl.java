package com.andhug.relay.shared.infrastructure.command;

import com.andhug.relay.shared.application.command.AsyncCommand;
import com.andhug.relay.shared.application.command.CommandBus;
import com.andhug.relay.shared.application.command.SyncCommand;
import com.andhug.relay.shared.application.handler.AsyncCommandHandler;
import com.andhug.relay.shared.application.handler.AsyncCommandValidator;
import com.andhug.relay.shared.application.handler.SyncCommandHandler;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "relay.bus.type", havingValue = "memory", matchIfMissing = true)
public class InMemoryCommandBusImpl implements CommandBus {

  private final Map<Class<?>, SyncCommandHandler<SyncCommand<Object>, Object>> syncHandlers;
  private final Map<Class<?>, AsyncCommandHandler<AsyncCommand>> asyncHandlers;
  private final Map<Class<?>, AsyncCommandValidator<AsyncCommand>> asyncValidators;

  @SuppressWarnings("unchecked")
  public InMemoryCommandBusImpl(
      List<SyncCommandHandler<?, ?>> syncHandlers,
      List<AsyncCommandHandler<?>> asyncHandlers,
      List<AsyncCommandValidator<?>> asyncValidators) {
    this.syncHandlers =
        syncHandlers.stream()
            .collect(
                Collectors.toMap(
                    h -> resolveSyncCommandType(h),
                    h -> (SyncCommandHandler<SyncCommand<Object>, Object>) h));
    this.asyncHandlers =
        asyncHandlers.stream()
            .collect(
                Collectors.toMap(
                    h -> resolveAsyncCommandType(h), h -> (AsyncCommandHandler<AsyncCommand>) h));
    this.asyncValidators =
        asyncValidators.stream()
            .collect(
                Collectors.toMap(
                    v -> resolveAsyncCommandType(v), v -> (AsyncCommandValidator<AsyncCommand>) v));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <R> R dispatch(SyncCommand<R> command) {
    SyncCommandHandler<SyncCommand<Object>, Object> handler = syncHandlers.get(command.getClass());
    if (handler == null) {
      throw new IllegalStateException("No sync handler for " + command.getClass());
    }
    return (R) handler.handle((SyncCommand<Object>) command);
  }

  @Override
  public void dispatch(AsyncCommand command) {
    AsyncCommandHandler<AsyncCommand> handler = asyncHandlers.get(command.getClass());
    AsyncCommandValidator<AsyncCommand> validator = asyncValidators.get(command.getClass());
    if (handler == null) {
      throw new IllegalStateException("No async handler for " + command.getClass());
    }
    if (validator != null) {
      validator.validate(command);
    }
    handler.handle(command);
  }

  private Class<?> resolveSyncCommandType(SyncCommandHandler<?, ?> handler) {
    return ResolvableType.forClass(handler.getClass())
        .as(SyncCommandHandler.class)
        .getGeneric(0) // first type arg — the command type
        .resolve();
  }

  private Class<?> resolveAsyncCommandType(AsyncCommandHandler<?> handler) {
    return ResolvableType.forClass(handler.getClass())
        .as(AsyncCommandHandler.class)
        .getGeneric(0)
        .resolve();
  }

  private Class<?> resolveAsyncCommandType(AsyncCommandValidator<?> validator) {
    return ResolvableType.forClass(validator.getClass())
        .as(AsyncCommandValidator.class)
        .getGeneric(0)
        .resolve();
  }
}
