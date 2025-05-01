package org.atlas.framework.security.session;

import java.util.Date;
import javax.annotation.Nullable;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.framework.error.AppError;
import org.atlas.framework.exception.BusinessException;

/**
 * Manages session info context for the current thread.
 */
public class SessionContext {

  private static final ThreadLocal<SessionInfo> sessionInfoHolder = new ThreadLocal<>();

  /**
   * Retrieves the session info for this thread.
   */
  @Nullable
  public static SessionInfo get() {
    return sessionInfoHolder.get();
  }

  public static String getSessionId() {
    SessionInfo sessionInfo = get();
    if (sessionInfo == null) {
      throw new BusinessException(AppError.UNAUTHORIZED);
    }
    return sessionInfo.getSessionId();
  }

  public static Integer getUserId() {
    SessionInfo sessionInfo = get();
    if (sessionInfo == null) {
      throw new BusinessException(AppError.UNAUTHORIZED);
    }
    return sessionInfo.getUserId();
  }

  public static Role getUserRole() {
    SessionInfo sessionInfo = get();
    if (sessionInfo == null) {
      throw new BusinessException(AppError.UNAUTHORIZED);
    }
    return sessionInfo.getUserRole();
  }

  public static Date getExpiresAt() {
    SessionInfo sessionInfo = get();
    if (sessionInfo == null) {
      throw new BusinessException(AppError.UNAUTHORIZED);
    }
    return sessionInfo.getExpiresAt();
  }

  /**
   * Sets the session info for this thread.
   */
  public static void set(SessionInfo sessionInfo) {
    sessionInfoHolder.set(sessionInfo);
  }

  /**
   * Clears the session info context for this thread. This should be called when the context is no
   * longer needed to prevent memory leaks.
   */
  public static void clear() {
    sessionInfoHolder.remove();
  }
}
