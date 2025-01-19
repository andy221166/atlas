package org.atlas.platform.qrgenerator;

public interface QrGenerator {

  byte[] generate(String text) throws Exception;

  byte[] generate(String text, int width, int height) throws Exception;
}
