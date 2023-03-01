package es.gobcan.istac.edatos.publisher.core.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CkanDateUtil {

    private CkanDateUtil() {
        // without impl
    }

    public static String getNowInIso() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
