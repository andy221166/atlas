package org.atlas.commons.util.sys;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SysUtil {

    /**
     * @return environment variable value
     */
    public static Optional<String> getEnv(String name) {
        return Optional.ofNullable(System.getenv(name));
    }
}
