package es.gobcan.istac.edatos.publisher.core.dataset.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.InternationalStringItemAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;

public class DatasetAvro2CkanPackageMapper {

    private static final String DEFAULT_LANGUAGE = "es";

    public static CkanPackage toCkanPackage(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService) {

        CkanPackage newPackage = new CkanPackage();
        newPackage.setName(CkanFormatUtil.formatCkanNameFromDatasetUrn(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getIdentifiableStatisticalResource().getUrn()));
        newPackage.setTitle(getDefaultLocalisedString(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getTitle().getLocalisedStrings()));
        newPackage.setPrivates(false);
        newPackage.setAuthor(getDefaultLocalisedString(datasetVersion.getSiemacMetadataStatisticalResource().getCreator().getTitle().getLocalisedStrings()));

        newPackage.setAuthor_email("arte@correo.com"); // TODO EDATOS-3460

        newPackage.setMaintainer(getDefaultLocalisedString(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getMaintainer().getTitle().getLocalisedStrings()));

        newPackage.setMaintainer_email("arte@correo.com"); // TODO EDATOS-3460

        newPackage.setLicense_id(metadataConfigurationService.retrieveCkanLicenceId());
        newPackage.setNotes(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getDescription().getLocalisedStrings().stream().filter(locString -> DEFAULT_LANGUAGE.equals(locString.getLocale())).findFirst().map(InternationalStringItemAvro::getLabel).orElse(""));

        newPackage.setUrl("url"); // TODO EDATOS-3460
        newPackage.setVersion(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getVersionLogic());

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getMaintainer().getCode().toLowerCase());
        newPackage.getResources().addAll(createResourcesFromDatasetVersion(newPackage.getName(), datasetVersion));
        newPackage.setTags(new ArrayList<>());
        newPackage.setExtras(new ArrayList<>());
        newPackage.getExtras().add(new CkanExtra("urn", datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getIdentifiableStatisticalResource().getUrn()));
        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());
        newPackage.setGroups(new ArrayList<>());
        return newPackage;
    }

    private static String getDefaultLocalisedString(List<InternationalStringItemAvro> localisedString) {

        if (localisedString.stream().anyMatch(x -> x.getLocale().equals(DEFAULT_LANGUAGE))) {
            return localisedString.stream().filter(x -> x.getLocale().equals(DEFAULT_LANGUAGE)).findFirst().get().getLabel();
        }
        return null;
    }

    private static List<CkanResource> createResourcesFromDatasetVersion(String package_id, DatasetVersionAvro datasetVersion) {
        List<CkanResource> resources = new ArrayList<>();

        resources.add(createResourceFromDatasetVersion(package_id));

        return resources;
    }

    private static CkanResource createResourceFromDatasetVersion(String package_id) {
        CkanResource resource = new CkanResource();

        resource.setPackage_id(package_id);
        resource.setUrl(
                "https://www.stats.govt.nz/assets/Uploads/Annual-enterprise-survey/Annual-enterprise-survey-2020-financial-year-provisional/Download-data/annual-enterprise-survey-2020-financial-year-provisional-csv.csv");
        resource.setDescription("");
        resource.setFormat("");
        resource.setName("resourse-" + package_id);

        return resource;
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
