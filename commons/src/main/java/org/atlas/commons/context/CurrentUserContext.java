package org.atlas.commons.context;

import javax.annotation.Nullable;

/**
 * Manages user context for the current thread.
 */
public class CurrentUserContext {

  private static final ThreadLocal<CurrentUser> userContext = new ThreadLocal<>();

  /**
   * Retrieves the current user for this thread.
   *
   * @return The current user.
   */
  @Nullable
  public static CurrentUser getCurrentUser() {
    return userContext.get();
  }

  @Nullable
  public static Integer getCurrentUserId() {
    CurrentUser currentUser = getCurrentUser();
    if (currentUser == null) {
      return null;
    }
    return currentUser.getUserId();
  }

  /**
   * Sets the current user for this thread.
   *
   * @param context The CurrentUser to set.
   */
  public static void setCurrentUser(CurrentUser context) {
    userContext.set(context);
  }

  /**
   * Clears the current user context for this thread. This should be called when the context is no
   * longer needed to prevent memory leaks.
   */
  public static void clear() {
    userContext.remove();
  }
}
