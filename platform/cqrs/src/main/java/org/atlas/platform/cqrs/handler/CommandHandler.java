package org.atlas.platform.cqrs.handler;

import org.atlas.platform.cqrs.model.Command;

public interface CommandHandler<C extends Command<R>, R> {

  R handle(C command) throws Exception;
}
