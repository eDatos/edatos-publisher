package es.gobcan.istac.edatos.publisher.core.util;

public class CkanFormatUtil {

    private static final String CKAN_SPECIAL_CHARACTER_ALLOWED = "-";
    private static final String URN_EXCLUDED_SPECIAL_CHARACTERS_REGEXP = "[:,=,.,(,)]";

    private CkanFormatUtil() {
        // without impl
    }
    public static String formatCkanNameFromDatasetUrn(String name) {
        // only allowed "-" and "_" special characters
        return name.replaceAll(URN_EXCLUDED_SPECIAL_CHARACTERS_REGEXP, CKAN_SPECIAL_CHARACTER_ALLOWED).toLowerCase();
    }

}
