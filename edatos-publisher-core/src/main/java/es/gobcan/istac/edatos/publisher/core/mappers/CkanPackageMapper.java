package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategorySchemes;
import org.siemac.metamac.srm.core.stream.message.LocalisedStringAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringItemAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetResourcesFormat;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanDateUtil;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;

public abstract class CkanPackageMapper {

    protected CkanPackageMapper() {

    }

    private static final String DEFAULT_LANGUAGE = "es";

    protected static List<CkanExtra> getExtraFields(String urlBaseIdentifier, MetadataConfigurationService metadataConfigurationService, String packageName) {
        List<CkanExtra> extraFields = new ArrayList<>();

        extraFields.add(new CkanExtra("identifier", getIdentifierCkan(urlBaseIdentifier, metadataConfigurationService, packageName)));

        extraFields.add(new CkanExtra("language", DEFAULT_LANGUAGE));

        extraFields.add(new CkanExtra("publisher_uri", metadataConfigurationService.retrieveCkanPublisherUri()));

        extraFields.add(new CkanExtra("spatial_uri", metadataConfigurationService.retrieveCkanSpatialUri()));

        return extraFields;
    }

    protected static String getCkanPackageName(String urn) {
        return CkanFormatUtil.formatCkanNameFromDatasetUrn(urn);
    }

    protected static String getDescriptionFromInternationalStringItemAvro(List<InternationalStringItemAvro> description) {
        if (description != null) {
            return description.stream().filter(locString -> DEFAULT_LANGUAGE.equals(locString.getLocale())).findFirst().map(InternationalStringItemAvro::getLabel).orElse("");
        }
        return null;
    }

    protected static String getDescriptionFromLocalisedStringAvro(List<LocalisedStringAvro> description) {
        if (description != null) {
            return description.stream().filter(locString -> DEFAULT_LANGUAGE.equals(locString.getLocale())).findFirst().map(LocalisedStringAvro::getLabel).orElse("");
        }
        return null;
    }

    protected static String getDefaultLocalisedStringFromInternationalStringItemAvro(List<InternationalStringItemAvro> localisedString) {

        if (localisedString.stream().anyMatch(x -> x.getLocale().equals(DEFAULT_LANGUAGE))) {
            return localisedString.stream().filter(x -> x.getLocale().equals(DEFAULT_LANGUAGE)).findFirst().get().getLabel();
        }
        return null;
    }

    protected static String getDefaultLocalisedStringFromLocalisedStringAvro(List<LocalisedStringAvro> localisedString) {

        if (localisedString.stream().anyMatch(x -> x.getLocale().equals(DEFAULT_LANGUAGE))) {
            return localisedString.stream().filter(x -> x.getLocale().equals(DEFAULT_LANGUAGE)).findFirst().get().getLabel();
        }
        return null;
    }

    protected static String getIdentifierCkan(String urlBaseIdentifier, MetadataConfigurationService metadataConfigurationService, String packageName) {
        String url = metadataConfigurationService.retrieveCkanUrlBase();
        if (url != null) {
            url += "/" + urlBaseIdentifier + "/" + packageName;
        }
        return url;
    }

    public static String addTheme(ExternalCategoryService externalCategoryService, String eniUrlBase, String urnCategory) {
        String[] params = UrnUtils.splitUrnItem(urnCategory);

        return addTheme(externalCategoryService, eniUrlBase, params[0], params[1], params[2], params[3]);
    }

    public static String addTheme(ExternalCategoryService externalCategoryService, String eniUrlBase, String agency, String resourceId, String version, String categoryId) {
        Category category = externalCategoryService.getCategory(agency, resourceId, version, categoryId);
        if (category != null && category.getEniSchemeType() != null) {
            return getUrlEniTheme(eniUrlBase, category.getEniSchemeType().getId());
        }
        return null;
    }

    public static String getDefaultTheme(ExternalCategoryService externalCategoryService, MetadataConfigurationService metadataConfigurationService, String eniUrlBase) {
        String[] urnCategoryWithoutVersion = UrnUtils.splitUrnItem(metadataConfigurationService.retrieveCkanDefaultUrnCategory());

        String urnLastVersionDefaultCategoryScheme = getLastVersionDefaultUrnCategory(externalCategoryService, urnCategoryWithoutVersion[0], urnCategoryWithoutVersion[1]);
        if (urnLastVersionDefaultCategoryScheme != null) {
            String[] params = UrnUtils.splitUrnItem(urnLastVersionDefaultCategoryScheme);
            return addTheme(externalCategoryService, eniUrlBase, params[0], params[1], params[2], urnCategoryWithoutVersion[3]);
        }

        return null;
    }

    private static String getLastVersionDefaultUrnCategory(ExternalCategoryService externalCategoryService, String agency, String resourceId) {
        String query = "LATEST EQ 'true'";
        CategorySchemes categoryScheme = externalCategoryService.getCategoryByQuery(agency, resourceId, query);
        if (categoryScheme != null && !categoryScheme.getCategorySchemes().isEmpty()) {
            return categoryScheme.getCategorySchemes().get(0).getUrn();
        }
        return null;
    }

    protected static String getUrlEniTheme(String urlBase, String eniId) {
        return urlBase + "/" + eniId;
    }

    protected static List<CkanResource> createResources(String description, String urn, String urlBaseIdentifier, String code, String packageId, List<String> formatValues) {
        List<CkanResource> resources = new ArrayList<>();

        String urlBaseResource = getResourceUrlBase(urn, urlBaseIdentifier);

        for (String resourceFormat : formatValues) {
            resources.add(createResource(packageId, description, resourceFormat, code, urlBaseResource));
        }
        return resources;
    }

    private static CkanResource createResource(String packageId, String description, String resourceFormat, String nameResourceBase, String urlBaseResource) {
        CkanResource resource = new CkanResource();

        resource.setPackage_id(packageId);
        resource.setUrl(getUrlResource(urlBaseResource, resourceFormat));
        resource.setDescription(description);
        resource.setFormat((DatasetResourcesFormat.getFormat(resourceFormat)));
        resource.setName(getNameResource(nameResourceBase, resourceFormat));
        resource.setMimetype(DatasetResourcesFormat.getMimeType(resourceFormat));
        resource.setCreated(CkanDateUtil.getNowInIso());
        return resource;
    }

    protected static List<CkanResource> updateResources(String urn, String urlBaseIdentifier, String packageId, List<CkanResource> existingResources) {
        List<CkanResource> resources = new ArrayList<>();

        String urlBaseResource = getResourceUrlBase(urn, urlBaseIdentifier);

        for (CkanResource existingResource : existingResources) {
            resources.add(updateResource(existingResource.getId(), packageId, urlBaseResource, existingResource.getFormat(), existingResource.getName()));
        }
        return resources;
    }

    protected static String getDefaultOrganisation(MetadataConfigurationService metadataConfigurationService) {
        return metadataConfigurationService.retrieveCkanDefaultOrganisation().toLowerCase();
    }

    private static CkanResource updateResource(String resourceId, String packageId, String urlBaseResource, String format, String name) {
        CkanResource resource = new CkanResource();

        resource.setPackage_id(packageId);
        resource.setId(resourceId);
        resource.setUrl(getUrlResource(urlBaseResource, format));
        resource.setLast_modified(CkanDateUtil.getNowInIso());
        resource.setName(name);
        resource.setFormat(format);
        resource.setMimetype(DatasetResourcesFormat.getFormat(format));
        return resource;
    }

    private static String getResourceUrlBase(String urn, String urlBaseIdentifier) {

        String[] params = UrnUtils.splitUrnItem(urn);
        return urlBaseIdentifier + "/" + params[0] + "/" + params[1] + "/" + params[2];
    }

    private static String getUrlResource(String baseUrl, String format) {
        return baseUrl + "." + format.toLowerCase();
    }

    private static String getNameResource(String nameResourceBase, String format) {
        return nameResourceBase + " (" + format + ")";
    }

}
