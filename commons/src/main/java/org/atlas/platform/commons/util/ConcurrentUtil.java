package org.atlas.platform.commons.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConcurrentUtil {

  public static void sleep(long seconds) {
    try {
      TimeUnit.SECONDS.sleep(seconds);
    } catch (InterruptedException ignored) {
      Thread.currentThread().interrupt();
    }
  }

  public static void sleep(int minSeconds, int maxSeconds) {
    // Generate a random sleep duration between minSeconds and maxSeconds seconds
    int randomSleepDuration = ThreadLocalRandom.current().nextInt(minSeconds, maxSeconds + 1);
    try {
      TimeUnit.SECONDS.sleep(randomSleepDuration);
    } catch (InterruptedException ignored) {
      Thread.currentThread().interrupt();
    }
  }

  public static void shutdown(ExecutorService executorService) {
    // Initiates an orderly shutdown of the executor service.
    // It prevents new tasks from being submitted, but allows previously submitted tasks to finish.
    executorService.shutdown();

    try {
      // Waits for the tasks to complete, with a timeout of 30 seconds.
      // If the tasks do not finish in the given time, it returns false.
      if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
        // If tasks did not finish within the timeout, force a shutdown immediately.
        // It attempts to stop all running tasks and halts the executor service.
        executorService.shutdownNow();
      }
    } catch (InterruptedException e) {
      // If the current thread is interrupted while waiting for tasks to finish,
      // it catches the InterruptedException, restores the interrupt status,
      // and forces the shutdown of the executor service.
      Thread.currentThread().interrupt();
      executorService.shutdownNow();
    }
  }
}
