package es.gobcan.istac.edatos.publisher.core.domain.constants;

public class Constants {

    public static final String MIME_TYPE_TSV = "text/tab-separated-values";
    public static final String MIME_TYPE_CSV = "text/csv";
    public static final String MIME_TYPE_XLS = "application/vnd.ms-excel";
    public static final String URL_RESOURCES_CODELIST_CODES = "codes";
    public static final String URL_RESOURCES_CATEGORIES = "categories";
    public static final String URL_RESOURCES_CONCEPTS = "concepts";
    public static final String URL_RESOURCES_ORGANISATIONS = "organisations";

    /*
     * Currently, Ckan is only available in ISTAC. Ckan API only allows string for texts. InternationalStrings are not allowed. Text in Ckan for ISTAC are only in Spanish.
     */
    public static final String CKAN_LANGUAGE = "es";

    // Datasets
    public static final String BASE_DATASET_RESOURCE = "constant.publisher.dataset.base.resource";

    // Categories
    public static final String BASE_CATEGORY_RESOURCE = "constant.publisher.category.scheme.resource";
    public static final String CATEGORY_TAG = "constant.publisher.category.scheme.tag";

    // Codelists
    public static final String BASE_CODELIST_RESOURCE = "constant.publisher.codelist.resource";
    public static final String CODELIST_TAG = "constant.publisher.codelist.tag";

    // Concepts scheme
    public static final String BASE_CONCEPT_RESOURCE = "constant.publisher.concept.scheme.resource";
    public static final String CONCEPT_TAG = "constant.publisher.concept.scheme.tag";

    // Organisations scheme
    public static final String BASE_ORGANISATION_RESOURCE = "constant.publisher.organisation.scheme.resource";
    public static final String ORGANISATION_TAG = "constant.publisher.organisation.scheme.tag";
    public static final String ORGANISATION_UNIT_TAG = "constant.publisher.organisation.scheme.unit.tag";
    public static final String ORGANISATION_AGENCY_TAG = "constant.publisher.organisation.scheme.agency.tag";
    public static final String ORGANISATION_DATA_PROVIDER_TAG = "constant.publisher.organisation.scheme.data.provider.tag";
    public static final String ORGANISATION_DATA_CONSUMER_TAG = "constant.publisher.organisation.scheme.data.consumer.tag";
}
