package org.atlas.framework.context;

import javax.annotation.Nullable;
import org.atlas.domain.user.shared.enums.Role;
import org.atlas.framework.error.AppError;
import org.atlas.framework.exception.BusinessException;

/**
 * Manages user info context for the current thread.
 */
public class UserContext {

  private static final ThreadLocal<UserInfo> userInfoHolder = new ThreadLocal<>();

  /**
   * Retrieves the user info for this thread.
   */
  @Nullable
  public static UserInfo get() {
    return userInfoHolder.get();
  }

  public static Integer getUserId() {
    UserInfo userInfo = get();
    if (userInfo == null) {
      throw new BusinessException(AppError.UNAUTHORIZED);
    }
    return userInfo.getUserId();
  }

  public static Role getRole() {
    UserInfo userInfo = get();
    if (userInfo == null) {
      throw new BusinessException(AppError.UNAUTHORIZED);
    }
    return userInfo.getRole();
  }

  public static String getSessionId() {
    UserInfo userInfo = get();
    if (userInfo == null) {
      throw new BusinessException(AppError.UNAUTHORIZED);
    }
    return userInfo.getSessionId();
  }

  /**
   * Sets the user info for this thread.
   */
  public static void set(UserInfo userInfo) {
    userInfoHolder.set(userInfo);
  }

  /**
   * Clears the user info context for this thread. This should be called when the context is no
   * longer needed to prevent memory leaks.
   */
  public static void clear() {
    userInfoHolder.remove();
  }
}
