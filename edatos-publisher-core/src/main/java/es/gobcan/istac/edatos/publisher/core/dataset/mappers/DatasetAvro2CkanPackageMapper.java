package es.gobcan.istac.edatos.publisher.core.dataset.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringItemAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetResourcesFormat;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanDateUtil;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;

public class DatasetAvro2CkanPackageMapper {

    private DatasetAvro2CkanPackageMapper() {

    }

    private static final String DEFAULT_LANGUAGE = "es";
    private static final String URL_BASE_DATASETS = "datasets";
    private static final String BASE_DATASET_RESOURCE = "Cubo estadístico. ";

    public static CkanPackage toNewCkanPackage(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService) {
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
        newPackage.setExtras(new ArrayList<>());
        newPackage.getExtras().add(new CkanExtra("urn", datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getIdentifiableStatisticalResource().getUrn()));
        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());
        newPackage.setGroups(new ArrayList<>());
        return newPackage;
    }

    public static String getCkanPackageName(DatasetVersionAvro datasetVersion) {
        return CkanFormatUtil.formatCkanNameFromDatasetUrn((datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getIdentifiableStatisticalResource().getUrn()));
    }

    private static String getDatasetDescription(DatasetVersionAvro datasetVersion) {
        return datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource().getDescription()
                .getLocalisedStrings().stream().filter(locString -> DEFAULT_LANGUAGE.equals(locString.getLocale())).findFirst().map(InternationalStringItemAvro::getLabel).orElse("");
    }

    public static CkanPackage toExistingCkanPackage(DatasetVersionAvro datasetVersion, String id) {
        CkanPackage existingPackage = new CkanPackage();

        existingPackage.setId(id);
        existingPackage.setNotes(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getDescription().getLocalisedStrings().stream().filter(locString -> DEFAULT_LANGUAGE.equals(locString.getLocale())).findFirst().map(InternationalStringItemAvro::getLabel).orElse(""));

        existingPackage.setUrl(datasetVersion.getVisualizerHtmlLink());
        existingPackage.setExtras(new ArrayList<>());
        existingPackage.setVersion(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getVersionLogic());
        existingPackage.getExtras().add(new CkanExtra("urn", datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getIdentifiableStatisticalResource().getUrn()));

        return existingPackage;
    }

    private static String getDefaultLocalisedString(List<InternationalStringItemAvro> localisedString) {

        if (localisedString.stream().anyMatch(x -> x.getLocale().equals(DEFAULT_LANGUAGE))) {
            return localisedString.stream().filter(x -> x.getLocale().equals(DEFAULT_LANGUAGE)).findFirst().get().getLabel();
        }
        return null;
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
