package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.siemac.edatos.core.common.enume.utils.ResourcesFormat;
import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.Item;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategorySchemes;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringItemAvro;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanGroup;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.domain.CkanUrlResource;
import es.gobcan.istac.edatos.publisher.core.domain.constants.Constants;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanDateUtil;
import es.gobcan.istac.edatos.publisher.core.util.CkanResourcesFormatOperation;
import es.gobcan.istac.edatos.publisher.core.util.markdown.MarkdownCkan;

public abstract class CkanPackageMapper {

    protected CkanPackageMapper() {
    }

    private Set<String>                          eniThemes                    = new HashSet<>();
    protected final CkanResourcesFormatOperation ckanResourcesFormatOperation = new CkanResourcesFormatOperation();

    protected abstract Map<String, String> getResourceDescription();
    protected abstract CkanUrlResource getCkanUrlResource();
    protected abstract Map<String, String> getNameResourceBase(String agency, String code);
    protected abstract String getUrn();
    protected abstract String getCkanPackageName();
    protected abstract List<String> getAvailableFormats();

    protected List<CkanExtra> getExtraFields(String urlBaseIdentifier, MetadataConfigurationService metadataConfigurationService, String packageName) {
        // no extra fields.
        return new ArrayList<>();
    }

    protected void setCommonMetadata(String urlBaseIdentifier, CkanPackage ckanPackage, MetadataConfigurationService metadataConfigurationService, String packageName) {
        ckanPackage.setSpatial(Arrays.asList(metadataConfigurationService.retrieveCkanSpatialUri()));
        ckanPackage.setLanguage(metadataConfigurationService.retrieveInternationalizationLanguages());
        ckanPackage.setIdentifier(getIdentifierCkan(urlBaseIdentifier, metadataConfigurationService, packageName));
        ckanPackage.setContactEmail(metadataConfigurationService.retrieveCkanDefaultContactEmail());
        ckanPackage.setPublisher(metadataConfigurationService.retrieveCkanPublisherUri());

    }

    protected String getDescriptionFromInternationalStringItemAvro(List<InternationalStringItemAvro> description, String defaultLanguage) {
        if (description != null) {
            String localeDescription = description.stream().filter(locString -> defaultLanguage.equals(locString.getLocale())).findFirst().map(InternationalStringItemAvro::getLabel).orElse("");
            return MarkdownCkan.stringHtmlToMarkdown(localeDescription);
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

    public Set<String> addThemes(ExternalCategoryService externalCategoryService, String eniUrlBase, String urnCategory) {
        String[] params = UrnUtils.splitUrnItem(urnCategory);

        return addThemes(externalCategoryService, eniUrlBase, params[0], params[1], params[2], params[3]);
    }

    private boolean existsEniSchemas(Category category) {
        return category != null && category.getEniSchemaTypes() != null && !category.getEniSchemaTypes().getEniSchemaTypes().isEmpty();
    }

    public Set<String> addThemes(ExternalCategoryService externalCategoryService, String eniUrlBase, String agency, String resourceId, String version, String categoryId) {
        Set<String> eniSchemasUrl = new HashSet<>();
        Category category = externalCategoryService.getCategory(agency, resourceId, version, categoryId);
        if (existsEniSchemas(category)) {
            for (Item eniSchema : category.getEniSchemaTypes().getEniSchemaTypes()) {
                addEniThemes(eniSchema.getId());
                eniSchemasUrl.add(getUrlEniTheme(eniUrlBase, eniSchema.getId()));
            }
        }
        return eniSchemasUrl;
    }

    public List<String> getDefaultTheme(ExternalCategoryService externalCategoryService, MetadataConfigurationService metadataConfigurationService, String eniUrlBase) {
        String[] urnCategoryWithoutVersion = UrnUtils.splitUrnItem(metadataConfigurationService.retrieveCkanDefaultUrnCategory());

        String urnLastVersionDefaultCategoryScheme = getLastVersionDefaultUrnCategory(externalCategoryService, urnCategoryWithoutVersion[0], urnCategoryWithoutVersion[1]);
        if (urnLastVersionDefaultCategoryScheme != null) {
            String[] params = UrnUtils.splitUrnItem(urnLastVersionDefaultCategoryScheme);
            Set<String> defaultSchemas = addThemes(externalCategoryService, eniUrlBase, params[0], params[1], params[2], urnCategoryWithoutVersion[3]);
            if (!defaultSchemas.isEmpty()) {
                return new ArrayList<>(defaultSchemas);
            }
        }

        return null;
    }

    protected List<String> getThemeFromStatisticalOperation(String stadisticalOperationCode, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService,
            ExternalOperationService externalOperationService) {

        Set<String> eniThemesOfStatisticalOperation = new HashSet<>();
        String eniUrlBase = metadataConfigurationService.retrieveCkanEniUrlBase();

        Operation operation = externalOperationService.retrieveStatisticalOperationById(stadisticalOperationCode);

        // get eniSchemas from subjectArea field of statistical operation
        Set<String> eniSchemas = addThemes(externalCategoryService, eniUrlBase, operation.getSubjectArea().getUrn());
        if (!eniSchemas.isEmpty()) {
            eniThemesOfStatisticalOperation.addAll(eniSchemas);
        }

        // get eniSchemas from secondary subject area field of statistical operation. This field is optional
        if (operation.getSecondarySubjectAreas() != null && !operation.getSecondarySubjectAreas().getSecondarySubjectAreas().isEmpty()) {
            for (ResourceInternal category : operation.getSecondarySubjectAreas().getSecondarySubjectAreas()) {
                eniSchemas = addThemes(externalCategoryService, eniUrlBase, category.getUrn());
                if (eniSchemas != null) {
                    eniThemesOfStatisticalOperation.addAll(eniSchemas);
                }
            }
        }

        if (!eniThemesOfStatisticalOperation.isEmpty()) {
            return new ArrayList<>(eniThemesOfStatisticalOperation);
        } else {
            // SubjectArea is mandatory in statistical operation but it could be possible that the eni scheme is no assigned for this category and the dataset could be send without theme.
            return getDefaultTheme(externalCategoryService, metadataConfigurationService, metadataConfigurationService.retrieveCkanEniUrlBase());
        }
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

    protected List<CkanResource> createResources(Map<String, String> description, CkanUrlResource ckanUrlResource, Map<String, String> code, String packageId, List<String> formatValues) {
        List<CkanResource> resources = new ArrayList<>();

        for (String resourceFormat : formatValues) {
            resources.add(createResource(packageId, description, resourceFormat, code, ckanUrlResource));
        }
        return resources;
    }

    private CkanResource createResource(String packageId, Map<String, String> description, String resourceFormat, Map<String, String> nameResourceBase, CkanUrlResource ckanUrlResource) {
        CkanResource resource = new CkanResource();

        resource.setPackage_id(packageId);
        resource.setUrl(getUrlResource(ckanUrlResource, resourceFormat));
        resource.setDescription(description);
        resource.setFormat((ckanResourcesFormatOperation.getFormat(resourceFormat)));

        resource.setFormat((ckanResourcesFormatOperation.getFormat(resourceFormat)));

        resource.setName(getNameResource(nameResourceBase, resourceFormat));
        resource.setMimetype(ckanResourcesFormatOperation.getMimeType(resourceFormat, false));
        resource.setCreated(CkanDateUtil.getNowInIso());
        return resource;
    }

    public List<CkanResource> createResources(String packageId, List<String> formats) {
        String[] params;

        CkanUrlResource ckanUrlResource = getCkanUrlResource();
        boolean requiredVersion = !Boolean.TRUE.equals(ckanUrlResource.getIsJaxiDataset());
        params = UrnUtils.splitUrnItem(getUrn(), requiredVersion);

        return createResources(getResourceDescription(), ckanUrlResource, getNameResourceBase(params[0], params[1]), packageId, formats);
    }

    public List<CkanResource> createResources(CkanPackage existingPackage) {
        List<CkanResource> newResources = new ArrayList<>();

        Set<String> existingFormats = new HashSet<>();
        for (CkanResource existingResource : existingPackage.getResources()) {
            if (isJsonStatFormat(existingResource.getFormat(), getFirstInternationalName(existingResource.getName()))) {
                existingFormats.add(ResourcesFormat.JSONSTAT.name());
            } else {

                existingFormats.add(existingResource.getFormat());
            }
        }

        List<String> formats = getAvailableFormats().stream().filter(format -> !existingFormats.contains(format)).collect(Collectors.toList());

        if (!formats.isEmpty()) {
            newResources = createResources(existingPackage.getId(), formats);
        }
        return newResources;
    }

    public List<CkanResource> updateResources(String packageId, List<CkanResource> existingResources) {
        List<CkanResource> resources = new ArrayList<>();
        Map<String, String> description = getResourceDescription();

        for (CkanResource existingResource : existingResources) {
            if (ckanResourcesFormatOperation.getMimeType(existingResource.getFormat(), true) != null) {
                resources.add(updateResource(existingResource.getId(), packageId, getCkanUrlResource(), existingResource.getFormat(), existingResource.getName(), description));
            }
        }

        return resources;
    }

    private boolean isJsonStatFormat(String format, String name) {
        // in ckan, jsonstat is converted to json. Because of this, we must ckeck de name to verify if the real format is jsonstat.
        return ResourcesFormat.JSON.name().equals(format) && name.indexOf(ResourcesFormat.JSONSTAT.name()) > -1;
    }

    protected String getDefaultOrganisation(MetadataConfigurationService metadataConfigurationService) {
        return metadataConfigurationService.retrieveCkanDefaultOrganisation().toLowerCase();
    }

    private CkanResource updateResource(String resourceId, String packageId, CkanUrlResource ckanUrlResource, String format, Map<String, String> name, Map<String, String> description) {
        CkanResource resource = new CkanResource();

        if (isJsonStatFormat(format, getFirstInternationalName(name))) {
            format = ResourcesFormat.JSONSTAT.name();
        }

        resource.setPackage_id(packageId);
        resource.setId(resourceId);
        resource.setUrl(getUrlResource(ckanUrlResource, format));
        resource.setLast_modified(CkanDateUtil.getNowInIso());
        resource.setName(name);
        resource.setFormat(ckanResourcesFormatOperation.getFormat(format));
        resource.setMimetype(ckanResourcesFormatOperation.getMimeType(format, false));
        resource.setDescription(description);
        return resource;
    }

    protected String getResourceUrlBase(String urn, String urlBaseIdentifier) {

        String[] params = UrnUtils.splitUrnItem(urn);
        return urlBaseIdentifier + "/" + params[0] + "/" + params[1] + "/" + params[2];
    }

    private String getUrlResource(CkanUrlResource ckanUrlResource, String format) {

        if (Boolean.TRUE.equals(ckanUrlResource.getIsJaxiDataset())) {
            return ckanUrlResource.getJaxiUrlByFormat().get(format);
        }

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

    private Map<String, String> getNameResource(Map<String, String> nameResourceBase, String format) {
        return nameResourceBase.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() + " (" + format + ")"));
    }
    public Set<String> getEniThemes() {
        return eniThemes;
    }
    private void addEniThemes(String eniTheme) {
        this.eniThemes.add(eniTheme);
    }

    protected List<CkanGroup> getCkanGroups() {
        List<CkanGroup> categories = new ArrayList<CkanGroup>();

        for (String eniScheme : getEniThemes()) {
            categories.add(new CkanGroup(eniScheme));
        }
        return categories;
    }

    protected String getCkanPackageName(String urn) {
        if (urn.length() > Constants.MAX_SIZE_PACKAGE_CKAN_NAME) {
            return StringUtils.right(urn, Constants.MAX_SIZE_PACKAGE_CKAN_NAME);
        }
        return urn;
    }

    protected String getCkanEndTitle(String agency, String resourceId) {
        return " (" + agency + ": " + resourceId + ")";
    }

    protected void debugPackage(Logger log, CkanPackage ckanPackage) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(ckanPackage);
            log.debug(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private String getFirstInternationalName(Map<String, String> map) {
        Optional<String> firstKey = map.keySet().stream().findFirst();
        if (firstKey.isPresent()) {
            return map.get(firstKey.get());
        }
        return "";
    }
}
