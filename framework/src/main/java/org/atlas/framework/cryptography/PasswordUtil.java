package org.atlas.framework.cryptography;

import java.security.SecureRandom;
import lombok.experimental.UtilityClass;
import org.atlas.framework.util.StringUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@UtilityClass
public class PasswordUtil {

  private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private static final SecureRandom random = new SecureRandom();
  private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
  private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String DIGITS = "0123456789";
  private static final String SPECIAL_CHARS = "!@#$%^&*()-_+=<>?/{}~|";

  public static String hashPassword(String plainPassword) {
    return passwordEncoder.encode(plainPassword);
  }

  public static String randomPassword(int length, boolean useUppercase, boolean useDigits,
      boolean useSpecialChars) {
    StringBuilder password = new StringBuilder(length);
    String charCategories = LOWERCASE;
    if (useUppercase) {
      charCategories += UPPERCASE;
    }
    if (useDigits) {
      charCategories += DIGITS;
    }
    if (useSpecialChars) {
      charCategories += SPECIAL_CHARS;
    }
    for (int i = 0; i < length; i++) {
      password.append(charCategories.charAt(random.nextInt(charCategories.length())));
    }
    return StringUtil.shuffle(password.toString());
  }
}
