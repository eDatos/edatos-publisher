package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategorySchemes;
import org.siemac.metamac.srm.core.stream.message.LocalisedStringAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringItemAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.domain.CkanUrlResource;
import es.gobcan.istac.edatos.publisher.core.domain.constants.Constants;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.ResourcesFormat;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanDateUtil;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;

public abstract class CkanPackageMapper {

    protected CkanPackageMapper() {
    }

    protected abstract String getResourceDescription();
    protected abstract CkanUrlResource getCkanUrlResource();
    protected abstract String getNameResourceBase(String agency, String code);
    protected abstract String getUrn();
    protected abstract List<String> getAvailableFormats();

    protected List<CkanExtra> getExtraFields(String urlBaseIdentifier, MetadataConfigurationService metadataConfigurationService, String packageName) {
        List<CkanExtra> extraFields = new ArrayList<>();

        extraFields.add(new CkanExtra("identifier", getIdentifierCkan(urlBaseIdentifier, metadataConfigurationService, packageName)));

        extraFields.add(new CkanExtra("language", Constants.CKAN_LANGUAGE));

        extraFields.add(new CkanExtra("publisher_uri", metadataConfigurationService.retrieveCkanPublisherUri()));

        extraFields.add(new CkanExtra("spatial_uri", metadataConfigurationService.retrieveCkanSpatialUri()));

        return extraFields;
    }

    protected String getCkanPackageName(String urn) {
        return CkanFormatUtil.formatCkanNameFromDatasetUrn(urn);
    }

    protected String getDescriptionFromInternationalStringItemAvro(List<InternationalStringItemAvro> description, String defaultLanguage) {
        if (description != null) {
            return description.stream().filter(locString -> defaultLanguage.equals(locString.getLocale())).findFirst().map(InternationalStringItemAvro::getLabel).orElse("");
        }
        return null;
    }

    protected String getDescriptionFromLocalisedStringAvro(List<LocalisedStringAvro> description, String defaultLanguage) {
        if (description != null) {
            return description.stream().filter(locString -> defaultLanguage.equals(locString.getLocale())).findFirst().map(LocalisedStringAvro::getLabel).orElse("");
        }
        return null;
    }

    protected String getDefaultLocalisedStringFromInternationalStringItemAvro(List<InternationalStringItemAvro> localisedString, String defaultLanguage) {

        if (localisedString.stream().anyMatch(x -> x.getLocale().equals(defaultLanguage))) {
            return localisedString.stream().filter(x -> x.getLocale().equals(defaultLanguage)).findFirst().get().getLabel();
        }
        return null;
    }

    protected String getDefaultLocalisedStringFromLocalisedStringAvro(List<LocalisedStringAvro> localisedString, String defaultLanguage) {

        if (localisedString.stream().anyMatch(x -> x.getLocale().equals(defaultLanguage))) {
            return localisedString.stream().filter(x -> x.getLocale().equals(defaultLanguage)).findFirst().get().getLabel();
        }
        return null;
    }

    protected String getIdentifierCkan(String urlBaseIdentifier, MetadataConfigurationService metadataConfigurationService, String packageName) {
        String url = metadataConfigurationService.retrieveCkanUrlBase();
        if (url != null) {
            url += "/" + urlBaseIdentifier + "/" + packageName;
        }
        return url;
    }

    public String addTheme(ExternalCategoryService externalCategoryService, String eniUrlBase, String urnCategory) {
        String[] params = UrnUtils.splitUrnItem(urnCategory);

        return addTheme(externalCategoryService, eniUrlBase, params[0], params[1], params[2], params[3]);
    }

    public String addTheme(ExternalCategoryService externalCategoryService, String eniUrlBase, String agency, String resourceId, String version, String categoryId) {
        Category category = externalCategoryService.getCategory(agency, resourceId, version, categoryId);
        if (category != null && category.getEniSchemeType() != null) {
            return getUrlEniTheme(eniUrlBase, category.getEniSchemeType().getId());
        }
        return null;
    }

    public String getDefaultTheme(ExternalCategoryService externalCategoryService, MetadataConfigurationService metadataConfigurationService, String eniUrlBase) {
        String[] urnCategoryWithoutVersion = UrnUtils.splitUrnItem(metadataConfigurationService.retrieveCkanDefaultUrnCategory());

        String urnLastVersionDefaultCategoryScheme = getLastVersionDefaultUrnCategory(externalCategoryService, urnCategoryWithoutVersion[0], urnCategoryWithoutVersion[1]);
        if (urnLastVersionDefaultCategoryScheme != null) {
            String[] params = UrnUtils.splitUrnItem(urnLastVersionDefaultCategoryScheme);
            return addTheme(externalCategoryService, eniUrlBase, params[0], params[1], params[2], urnCategoryWithoutVersion[3]);
        }

        return null;
    }

    protected String getThemeFromStatisticalOperation(String stadisticalOperationCode, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService,
            ExternalOperationService externalOperationService) {

        Set<String> eniThemes = new HashSet<>();
        String eniUrlBase = metadataConfigurationService.retrieveCkanEniUrlBase();

        Operation operation = externalOperationService.retrieveStatisticalOperationById(stadisticalOperationCode);

        // get eniScheme from subjectArea field of statistical operation
        String eniScheme = addTheme(externalCategoryService, eniUrlBase, operation.getSubjectArea().getUrn());
        if (eniScheme != null) {
            eniThemes.add(eniScheme);
        }

        // get eniScheme from secondary subject area field of statistical operation. This field is optional
        if (operation.getSecondarySubjectAreas() != null && !operation.getSecondarySubjectAreas().getSecondarySubjectAreas().isEmpty()) {
            for (ResourceInternal category : operation.getSecondarySubjectAreas().getSecondarySubjectAreas()) {
                eniScheme = addTheme(externalCategoryService, eniUrlBase, category.getUrn());
                if (eniScheme != null) {
                    eniThemes.add(eniScheme);
                }
            }
        }

        if (!eniThemes.isEmpty()) {
            return String.join(",", eniThemes);
        }
        return null;
    }

    private String getLastVersionDefaultUrnCategory(ExternalCategoryService externalCategoryService, String agency, String resourceId) {
        String query = "LATEST EQ 'true'";
        CategorySchemes categoryScheme = externalCategoryService.getCategoryByQuery(agency, resourceId, query);
        if (categoryScheme != null && !categoryScheme.getCategorySchemes().isEmpty()) {
            return categoryScheme.getCategorySchemes().get(0).getUrn();
        }
        return null;
    }

    protected String getUrlEniTheme(String urlBase, String eniId) {
        return urlBase + "/" + eniId;
    }

    protected List<CkanResource> createResources(String description, CkanUrlResource ckanUrlResource, String code, String packageId, List<String> formatValues) {
        List<CkanResource> resources = new ArrayList<>();

        for (String resourceFormat : formatValues) {
            resources.add(createResource(packageId, description, resourceFormat, code, ckanUrlResource));
        }
        return resources;
    }

    private CkanResource createResource(String packageId, String description, String resourceFormat, String nameResourceBase, CkanUrlResource ckanUrlResource) {
        CkanResource resource = new CkanResource();

        resource.setPackage_id(packageId);
        resource.setUrl(getUrlResource(ckanUrlResource, resourceFormat));
        resource.setDescription(description);
        resource.setFormat((ResourcesFormat.getFormat(resourceFormat)));
        resource.setName(getNameResource(nameResourceBase, resourceFormat));
        resource.setMimetype(ResourcesFormat.getMimeType(resourceFormat));
        resource.setCreated(CkanDateUtil.getNowInIso());
        return resource;
    }

    public List<CkanResource> createResources(String packageId, List<String> formats) {

        String[] params = UrnUtils.splitUrnItem(getUrn());

        return createResources(getResourceDescription(), getCkanUrlResource(), getNameResourceBase(params[0], params[1]), packageId, formats);
    }

    public List<CkanResource> createResources(CkanPackage existingPackage) {
        List<CkanResource> newResources = new ArrayList<>();

        Set<String> existingFormats = existingPackage.getResources().stream().map(CkanResource::getFormat).collect(Collectors.toSet());

        List<String> formats = getAvailableFormats().stream().filter(format -> !existingFormats.contains(format)).collect(Collectors.toList());

        if (!formats.isEmpty()) {
            newResources = createResources(existingPackage.getId(), formats);
        }
        return newResources;
    }

    public List<CkanResource> updateResources(String packageId, List<CkanResource> existingResources) {
        List<CkanResource> resources = new ArrayList<>();

        for (CkanResource existingResource : existingResources) {
            resources.add(updateResource(existingResource.getId(), packageId, getCkanUrlResource(), existingResource.getFormat(), existingResource.getName()));
        }

        return resources;
    }

    protected String getDefaultOrganisation(MetadataConfigurationService metadataConfigurationService) {
        return metadataConfigurationService.retrieveCkanDefaultOrganisation().toLowerCase();
    }

    private CkanResource updateResource(String resourceId, String packageId, CkanUrlResource ckanUrlResource, String format, String name) {
        CkanResource resource = new CkanResource();

        resource.setPackage_id(packageId);
        resource.setId(resourceId);
        resource.setUrl(getUrlResource(ckanUrlResource, format));
        resource.setLast_modified(CkanDateUtil.getNowInIso());
        resource.setName(name);
        resource.setFormat(format);
        resource.setMimetype(ResourcesFormat.getFormat(format));
        return resource;
    }

    protected String getResourceUrlBase(String urn, String urlBaseIdentifier) {

        String[] params = UrnUtils.splitUrnItem(urn);
        return urlBaseIdentifier + "/" + params[0] + "/" + params[1] + "/" + params[2];
    }

    private String getUrlResource(CkanUrlResource ckanUrlResource, String format) {
        String urlResource = ckanUrlResource.getUrlBaseResource();

        if (!StringUtils.isEmpty(ckanUrlResource.getUrlSuffixResource())) {
            urlResource += "/" + ckanUrlResource.getUrlSuffixResource();
        }

        urlResource += "." + format.toLowerCase();

        if (!StringUtils.isEmpty(ckanUrlResource.getUrlParametersFields())) {
            urlResource += "?" + ckanUrlResource.getUrlParametersFields();
        }

        return urlResource;
    }

    private String getNameResource(String nameResourceBase, String format) {
        return nameResourceBase + " (" + format + ")";
    }

}
