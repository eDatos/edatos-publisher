package es.gobcan.istac.edatos.publisher.core.config;

import org.siemac.edatos.core.common.constants.shared.ConfigurationConstants;

public final class DataConfigurationConstants extends ConfigurationConstants {

    private DataConfigurationConstants() {
    }

    // Datasource
    public static final String DB_URL                     = "edatos.publisher.db.url";
    public static final String DB_USERNAME                = "edatos.publisher.db.username";
    public static final String DB_PASSWORD                = "edatos.publisher.db.password";
    public static final String DB_DRIVER_NAME             = "edatos.publisher.db.driver_name";
    public static final String CKAN_LICENCE_ID            = "edatos.publisher.ckan.licence.id";
    public static final String CKAN_API_KEY               = "edatos.publisher.ckan.api.key";
    public static final String CKAN_PUBLISHER_URI         = "edatos.publisher.ckan.publisher.uri";
    public static final String CKAN_SPATIAL_URI           = "edatos.publisher.ckan.spatial.uri";
    public static final String CKAN_ENI_URL_BASE          = "edatos.publisher.ckan.eni.url.base";
    public static final String CKAN_DEFAULT_URN_CATEGORY  = "edatos.publisher.ckan.default.urn.category";
    public static final String CKAN_DEFAULT_ORGANISATION  = "edatos.publisher.ckan.default.organisation";
    public static final String CKAN_DEFAULT_CONTACT_EMAIL = "edatos.publisher.ckan.default.contact_email";
    public static final String CKAN_DEFAULT_CREATOR       = "edatos.publisher.ckan.default.creator";
}
