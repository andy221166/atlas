package org.atlas.commons.util.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

  public static final String EMPTY = "";

  public static String limit(String input, int maxLength) {
    if (input == null) {
      return null;
    }

    return input.length() > maxLength ? input.substring(0, maxLength) : input;
  }

  public static String mask(String input, int firstChars, char maskChar) {
    if (input == null) {
      return null;
    }

    int strLength = input.length();
    if (firstChars <= 0) {
      char[] maskedArray = new char[strLength];
      Arrays.fill(maskedArray, maskChar);
      return new String(maskedArray);
    }

    if (strLength <= firstChars) {
      return input;
    }

    return input.substring(0, firstChars) + String.valueOf(maskChar).repeat(strLength - firstChars);
  }

  public static String shuffle(String input) {
    List<Character> characters = new ArrayList<>();
    for (char character : input.toCharArray()) {
      characters.add(character);
    }
    Collections.shuffle(characters);

    StringBuilder shuffledString = new StringBuilder();
    for (char character : characters) {
      shuffledString.append(character);
    }
    return shuffledString.toString();
  }
}
