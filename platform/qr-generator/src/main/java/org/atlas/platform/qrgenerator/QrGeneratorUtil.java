package org.atlas.platform.qrgenerator;

import org.atlas.platform.qrgenerator.zxing.ZxingAdapter;

/**
 * Implement Singleton pattern with Bill Pugh solution
 */
public class QrGeneratorUtil {

  private static class JsonHolder {
    private static final QrGenerator INSTANCE = new ZxingAdapter();
  }

  public static QrGenerator getInstance() {
    return JsonHolder.INSTANCE;
  }
}
