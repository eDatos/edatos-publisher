package es.gobcan.istac.edatos.publisher.core.dataset.mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringItemAvro;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetResourcesFormat;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanDateUtil;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;

public class DatasetAvro2CkanPackageMapper {

    private DatasetAvro2CkanPackageMapper() {

    }

    private static final String DEFAULT_LANGUAGE = "es";
    private static final String URL_BASE_DATASETS = "datasets";
    private static final String URL_BASE_IDENTIFIER = "dataset";
    private static final String BASE_DATASET_RESOURCE = "Cubo estadístico. ";
    private static final String ENI_FIELD = "+eniSchemeType";

    public static CkanPackage toNewCkanPackage(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService,
            ExternalOperationService externalOperationService) {
        CkanPackage newPackage = new CkanPackage();
        newPackage.setName(getCkanPackageName(datasetVersion));
        newPackage.setTitle(getDefaultLocalisedString(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getTitle().getLocalisedStrings()));
        newPackage.setPrivates(false);
        newPackage.setAuthor(getDefaultLocalisedString(datasetVersion.getSiemacMetadataStatisticalResource().getCreator().getTitle().getLocalisedStrings()));
        newPackage.setMaintainer(getDefaultLocalisedString(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getMaintainer().getTitle().getLocalisedStrings()));
        newPackage.setLicense_id(metadataConfigurationService.retrieveCkanLicenceId());
        newPackage.setNotes(getDatasetDescription(datasetVersion));

        newPackage.setUrl(datasetVersion.getVisualizerHtmlLink());
        newPackage.setVersion(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getVersionLogic());

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getMaintainer().getCode().toLowerCase());
        newPackage.setResources(new ArrayList<>());
        newPackage.getResources().addAll(createResourcesFromDatasetVersion(datasetVersion, newPackage.getName()));
        newPackage.setTags(new ArrayList<>());

        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());
        newPackage.setGroups(new ArrayList<>());

        // extra fields
        newPackage.setExtras(new ArrayList<>());
        newPackage.getExtras().addAll(getExtraFields(datasetVersion, metadataConfigurationService, externalCategoryService, externalOperationService, newPackage.getName()));

        return newPackage;
    }

    private static List<CkanExtra> getExtraFields(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService,
            ExternalOperationService externalOperationService, String packageName) {
        List<CkanExtra> extraFields = new ArrayList<>();

        extraFields.add(new CkanExtra("identifier", getIdentifierCkan(metadataConfigurationService, packageName)));

        extraFields.add(new CkanExtra("language", DEFAULT_LANGUAGE));

        extraFields.add(new CkanExtra("publisher_uri", metadataConfigurationService.retrieveCkanPublisherUri()));

        extraFields.add(new CkanExtra("spatial_uri", metadataConfigurationService.retrieveCkanSpatialUri()));

        String theme = getThemeFromStatisticalOperation(datasetVersion, metadataConfigurationService, externalCategoryService, externalOperationService);
        if (theme != null) {
            extraFields.add(new CkanExtra("theme", theme));
        }

        return extraFields;
    }

    public static String getCkanPackageName(DatasetVersionAvro datasetVersion) {
        return CkanFormatUtil.formatCkanNameFromDatasetUrn((datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getIdentifiableStatisticalResource().getUrn()));
    }

    private static String getDatasetDescription(DatasetVersionAvro datasetVersion) {
        return datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource().getDescription()
                .getLocalisedStrings().stream().filter(locString -> DEFAULT_LANGUAGE.equals(locString.getLocale())).findFirst().map(InternationalStringItemAvro::getLabel).orElse("");
    }

    public static CkanPackage toExistingCkanPackage(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService,
            ExternalOperationService externalOperationService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        existingPackage.setId(id);
        existingPackage.setNotes(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getDescription().getLocalisedStrings().stream().filter(locString -> DEFAULT_LANGUAGE.equals(locString.getLocale())).findFirst().map(InternationalStringItemAvro::getLabel).orElse(""));

        existingPackage.setUrl(datasetVersion.getVisualizerHtmlLink());
        existingPackage.setVersion(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getVersionLogic());

        // extra fields
        existingPackage.setExtras(new ArrayList<>());
        existingPackage.getExtras().addAll(getExtraFields(datasetVersion, metadataConfigurationService, externalCategoryService, externalOperationService, getCkanPackageName(datasetVersion)));

        return existingPackage;
    }

    private static String getDefaultLocalisedString(List<InternationalStringItemAvro> localisedString) {

        if (localisedString.stream().anyMatch(x -> x.getLocale().equals(DEFAULT_LANGUAGE))) {
            return localisedString.stream().filter(x -> x.getLocale().equals(DEFAULT_LANGUAGE)).findFirst().get().getLabel();
        }
        return null;
    }

    private static String getIdentifierCkan(MetadataConfigurationService metadataConfigurationService, String packageName) {
        String url = metadataConfigurationService.retrieveCkanUrlBase();
        if (url != null) {
            url += "/" + URL_BASE_IDENTIFIER + "/" + packageName;
        }
        return url;
    }

    private static String getThemeFromStatisticalOperation(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService,
            ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService) {

        Set<String> eniThemes = new HashSet<>();
        Pageable pageable = (new PageRequest(0, 100)).first();
        String eniUrlBase = metadataConfigurationService.retrieveCkanEniUrlBase();

        Operation operation = externalOperationService.retrieveStatisticalOperationById(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource()
                .getVersionableStatisticalResource().getNameableStatisticalResource().getIdentifiableStatisticalResource().getStatisticalOperation().getCode());

        // get eniScheme from subjectArea field of statistical operation
        String eniScheme = addTheme(externalCategoryService, eniUrlBase, pageable, operation.getSubjectArea().getUrn());
        if (eniScheme != null) {
            eniThemes.add(eniScheme);
        }

        // get eniScheme from secondary subject area field of statistical operation. This field is optional
        if (operation.getSecondarySubjectAreas() != null && !operation.getSecondarySubjectAreas().getSecondarySubjectAreas().isEmpty()) {
            for (ResourceInternal category : operation.getSecondarySubjectAreas().getSecondarySubjectAreas()) {
                eniScheme = addTheme(externalCategoryService, eniUrlBase, pageable, category.getUrn());
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

    public static String addTheme(ExternalCategoryService externalCategoryService, String eniUrlBase, Pageable pageable, String urnCategory) {
        String[] params = UrnUtils.splitUrnItem(urnCategory);

        Category category = externalCategoryService.getCategory(pageable, params[0], params[1], params[2], params[3]);
        if (category != null && category.getEniSchemeType() != null) {
            return getUrlEniTheme(eniUrlBase, category.getEniSchemeType().getId());
        }
        return null;
    }

    private static String getUrlEniTheme(String urlBase, String eniId) {
        return urlBase + "/" + eniId;
    }

    public static List<CkanResource> createResourcesFromDatasetVersion(DatasetVersionAvro datasetVersion, String packageId) {
        List<CkanResource> resources = new ArrayList<>();

        String datasetCode = datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getCode();
        String urlBaseResource = getResourceUrlBase(datasetVersion);

        for (DatasetResourcesFormat resourceFormat : DatasetResourcesFormat.values()) {
            resources.add(createResourceFromDatasetVersion(packageId, getDatasetDescription(datasetVersion), resourceFormat.name(), datasetCode, urlBaseResource));
        }
        return resources;
    }

    private static CkanResource createResourceFromDatasetVersion(String packageId, String datasetDescription, String resourceFormat, String datasetCode, String urlBaseResource) {
        CkanResource resource = new CkanResource();

        resource.setPackage_id(packageId);
        resource.setUrl(getUrlResource(urlBaseResource, resourceFormat));
        resource.setDescription(datasetDescription);
        resource.setFormat(DatasetResourcesFormat.getFormat(resourceFormat));
        resource.setName(getNameResource(datasetCode, resourceFormat));
        resource.setMimetype(DatasetResourcesFormat.getMimeType(resourceFormat));
        resource.setCreated(CkanDateUtil.getNowInIso());
        return resource;
    }

    public static List<CkanResource> updateResourcesFromDatasetVersion(DatasetVersionAvro datasetVersion, String packageId, CkanPackage existingDataset) {
        List<CkanResource> resources = new ArrayList<>();

        String urlBaseResource = getResourceUrlBase(datasetVersion);

        for (CkanResource existingResource : existingDataset.getResources()) {
            resources.add(updateResourceFromDatasetVersion(existingResource.getId(), packageId, urlBaseResource, existingResource.getFormat(), existingResource.getName()));
        }
        return resources;
    }

    private static CkanResource updateResourceFromDatasetVersion(String resourceId, String packageId, String urlBaseResource, String format, String name) {
        CkanResource resource = new CkanResource();

        resource.setPackage_id(packageId);
        resource.setId(resourceId);
        resource.setUrl(getUrlResource(urlBaseResource, format));
        resource.setLast_modified(CkanDateUtil.getNowInIso());
        resource.setName(name);
        resource.setFormat(DatasetResourcesFormat.getFormat(format));
        resource.setMimetype(DatasetResourcesFormat.getMimeType(format));
        return resource;
    }

    private static String getResourceUrlBase(DatasetVersionAvro datasetVersion) {
        String url = datasetVersion.getResourcesUrlBase();
        if (url != null) {
            String[] params = UrnUtils.splitUrnItem(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                    .getNameableStatisticalResource().getIdentifiableStatisticalResource().getUrn());
            url += "/" + URL_BASE_DATASETS + "/" + params[0] + "/" + params[1] + "/" + params[2];
        }
        return url;
    }

    private static String getUrlResource(String baseUrl, String format) {
        return baseUrl + "." + format.toLowerCase();
    }

    private static String getNameResource(String datasetCode, String format) {
        return BASE_DATASET_RESOURCE + datasetCode + "(" + format + ")";
    }

}
