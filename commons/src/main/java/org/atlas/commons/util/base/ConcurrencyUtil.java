package org.atlas.commons.util.base;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConcurrencyUtil {

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
}
