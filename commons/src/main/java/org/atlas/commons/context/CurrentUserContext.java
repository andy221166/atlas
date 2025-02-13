package org.atlas.commons.context;

import javax.annotation.Nullable;
import org.atlas.commons.enums.Role;
import org.atlas.commons.exception.AppError;
import org.atlas.commons.exception.BusinessException;

/**
 * Manages user context for the current thread.
 */
public class CurrentUserContext {

  private static final ThreadLocal<CurrentUser> holder = new ThreadLocal<>();

  /**
   * Retrieves the current user for this thread.
   *
   * @return The current user.
   */
  @Nullable
  public static CurrentUser getCurrentUser() {
    return holder.get();
  }

  public static Integer getUserId() {
    CurrentUser currentUser = getCurrentUser();
    if (currentUser == null) {
      throw new BusinessException(AppError.UNAUTHORIZED);
    }
    return currentUser.getUserId();
  }

  public static Role getRole() {
    CurrentUser currentUser = getCurrentUser();
    if (currentUser == null) {
      throw new BusinessException(AppError.UNAUTHORIZED);
    }
    return currentUser.getRole();
  }

  /**
   * Sets the current user for this thread.
   *
   * @param context The CurrentUser to set.
   */
  public static void setCurrentUser(CurrentUser context) {
    holder.set(context);
  }

  /**
   * Clears the current user context for this thread. This should be called when the context is no
   * longer needed to prevent memory leaks.
   */
  public static void clear() {
    holder.remove();
  }
}
