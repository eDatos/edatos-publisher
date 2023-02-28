package es.gobcan.istac.edatos.publisher.core.dataset.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringItemAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetResourcesFormat;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;

public class DatasetAvro2CkanPackageMapper {

    private static final String DEFAULT_LANGUAGE = "es";
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
        newPackage.setNotes(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getDescription().getLocalisedStrings().stream().filter(locString -> DEFAULT_LANGUAGE.equals(locString.getLocale())).findFirst().map(InternationalStringItemAvro::getLabel).orElse(""));

        newPackage.setUrl(datasetVersion.getVisualizerHtmlLink());
        newPackage.setVersion(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getVersionLogic());

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getMaintainer().getCode().toLowerCase());
        newPackage.setResources(new ArrayList<>());
        newPackage.getResources().addAll(createResourcesFromDatasetVersion(newPackage.getName(), newPackage.getNotes(), datasetVersion));
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

    private static List<CkanResource> createResourcesFromDatasetVersion(String packageId, String datasetDescription, DatasetVersionAvro datasetVersion) {
        List<CkanResource> resources = new ArrayList<>();

        for (DatasetResourcesFormat resourceFormat : DatasetResourcesFormat.values()) {
            resources.add(createResourceFromDatasetVersion(packageId, datasetDescription, resourceFormat.name(), datasetVersion));
        }
        return resources;
    }

    private static CkanResource createResourceFromDatasetVersion(String packageId, String datasetDescription, String resourceFormat, DatasetVersionAvro datasetVersion) {
        CkanResource resource = new CkanResource();
        String datasetCode = datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getCode();

        // TODO EDATOS-3460 urlBaseResource debe venir en datasetVersion
        String urlBaseResource = "https://datos.canarias.es/api/estadisticas/statistical-resources/v1.0/ISTAC/C00010A_000026/2.30";

        resource.setPackage_id(packageId);
        resource.setUrl(getUrlResource(urlBaseResource, resourceFormat));
        resource.setDescription(datasetDescription);
        resource.setFormat(resourceFormat);
        resource.setName(getNameResource(datasetCode, resourceFormat));

        return resource;
    }

    private static String getUrlResource(String baseUrl, String format) {
        return baseUrl + "." + format.toLowerCase();
    }

    private static String getNameResource(String datasetCode, String format) {
        return BASE_DATASET_RESOURCE + datasetCode + "(" + format + ")";
    }

    public static CkanPackage toCkanPackagePruebas(String urn, MetadataConfigurationService metadataConfigurationService) {

        CkanPackage newPackage = new CkanPackage();

        newPackage.setName(CkanFormatUtil.formatCkanNameFromDatasetUrn(urn));
        newPackage.setTitle("pruebas título dataset v2");
        newPackage.setPrivates(false);
        newPackage.setOwner_org("istac");
        newPackage.setResources(new ArrayList<>());
        newPackage.getResources().add(new CkanResource(newPackage.getName(),
                "https://www.stats.govt.nz/assets/Uploads/Annual-enterprise-survey/Annual-enterprise-survey-2020-financial-year-provisional/Download-data/annual-enterprise-survey-2020-financial-year-provisional-csv.csv",
                "Prueba recurso"));
        newPackage.setTags(new ArrayList<>());
        newPackage.setExtras(new ArrayList<>());
        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());
        newPackage.setGroups(new ArrayList<>());
        return newPackage;
    }
}
