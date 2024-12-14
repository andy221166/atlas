package org.atlas.platform.cqrs.gateway;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.atlas.commons.function.Callback;
import org.atlas.platform.cqrs.exception.CommandExecutorNotFoundException;
import org.atlas.platform.cqrs.exception.QueryExecutorNotFoundException;
import org.atlas.platform.cqrs.handler.CommandHandler;
import org.atlas.platform.cqrs.interceptor.CommandInterceptor;
import org.atlas.platform.cqrs.model.Command;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@Slf4j
public class CommandGateway {

  private final Map<String, CommandHandler<?, ?>> executorMap;
  private final List<CommandInterceptor> interceptors;
  private final TaskExecutor asyncCommandTaskExecutor;

  public CommandGateway(Map<String, CommandHandler<?, ?>> executorMap,
      List<CommandInterceptor> interceptors,
      @Qualifier("asyncCommandExecutor") TaskExecutor asyncCommandTaskExecutor) {
    this.executorMap = executorMap;
    this.interceptors = interceptors;
    this.asyncCommandTaskExecutor = asyncCommandTaskExecutor;
  }

  public <C extends Command<R>, R> R send(C command) throws Exception {
    CommandHandler<C, R> executor = obtainExecutor(command);
    interceptors.forEach(interceptor -> interceptor.preHandle(command));
    R result = executor.handle(command);
    interceptors.forEach(interceptor -> interceptor.postHandle(command));
    return result;
  }

  public <C extends Command<R>, R> CompletableFuture<Void> sendAsync(C command) {
    return CompletableFuture.runAsync(() -> {
      try {
        send(command);
      } catch (Exception e) {
        throw new CompletionException(e);
      }
    }, asyncCommandTaskExecutor);
  }

  @Async("asyncCommandExecutor")
  public <C extends Command<R>, R> void sendAsync(C command, Callback<R> callback) {
    try {
      R result = send(command);
      callback.onSuccess(result);
    } catch (Exception e) {
      callback.onFailure(e);
    }
  }

  @SuppressWarnings("unchecked")
  private <C extends Command<R>, R> CommandHandler<C, R> obtainExecutor(Command<R> command) {
    if (CollectionUtils.isEmpty(executorMap)) {
      throw new QueryExecutorNotFoundException(
          "Not found executor for command " + command.getClass());
    }
    String executorBeanName =
        StringUtils.uncapitalize(command.getClass().getSimpleName()) + "Handler";
    CommandHandler<?, ?> executor = executorMap.get(executorBeanName);
    if (executor == null) {
      throw new CommandExecutorNotFoundException(
          "Not found executor for command " + command.getClass());
    }
    return (CommandHandler<C, R>) executor;
  }
}
