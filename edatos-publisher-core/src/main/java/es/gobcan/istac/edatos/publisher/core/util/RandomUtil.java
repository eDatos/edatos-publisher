package es.gobcan.istac.edatos.publisher.core.util;

import org.apache.commons.lang3.RandomStringUtils;

public final class RandomUtil {

    private static final int DEF_COUNT = 20;

    private RandomUtil() {
    }

    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }
}
