package es.gobcan.istac.edatos.publisher.core.util;

public class CkanFormatUtil {

    private static final String CKAN_SPECIAL_CHARACTER_ALLOWED = "-";
    private static final String URN_EXCLUDED_SPECIAL_CHARACTERS_REGEXP = "[:,=,.,(,)]";

    private CkanFormatUtil() {
        // without impl
    }

    private static final String URN_VERSION_REGEX = "\\([^)]*\\)$";

    public static String getUrnWithoutVersion(String versionUrn) {
        return versionUrn.replaceFirst(URN_VERSION_REGEX, "");
    }

    public static String formatCkanNameFromDatasetUrn(String urn) {
        // only allowed "-" and "_" special characters
        return getUrnWithoutVersion(urn).replaceAll(URN_EXCLUDED_SPECIAL_CHARACTERS_REGEXP, CKAN_SPECIAL_CHARACTER_ALLOWED).toLowerCase();
    }

}
