package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetResourcesFormat;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetStateEnum;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.core.util.CkanUtil;

public class DatasetAvro2CkanPackageMapper extends CkanPackageMapper {

    private DatasetAvro2CkanPackageMapper() {
    }

    private static final String URL_BASE_DATASETS = "datasets";
    private static final String URL_BASE_IDENTIFIER = "dataset";
    private static final String BASE_DATASET_RESOURCE = "Cubo estadístico. ";

    public static CkanPackage toNewCkanPackage(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService,
            ExternalOperationService externalOperationService) {
        CkanPackage newPackage = new CkanPackage();
        newPackage.setName(getCkanPackageName(datasetVersion));
        newPackage.setTitle(getDefaultLocalisedStringFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource()
                .getVersionableStatisticalResource().getNameableStatisticalResource().getTitle().getLocalisedStrings()));
        newPackage.setPrivates(false);
        newPackage.setAuthor(getDefaultLocalisedStringFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getCreator().getTitle().getLocalisedStrings()));
        newPackage.setMaintainer(getDefaultLocalisedStringFromInternationalStringItemAvro(
                datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getMaintainer().getTitle().getLocalisedStrings()));
        newPackage.setLicense_id(metadataConfigurationService.retrieveCkanLicenceId());
        newPackage.setNotes(getDatasetDescription(datasetVersion));

        newPackage.setUrl(datasetVersion.getVisualizerHtmlLink());
        newPackage.setVersion(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getVersionLogic());

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(getDefaultOrganisation(metadataConfigurationService));
        newPackage.setResources(new ArrayList<>());
        newPackage.getResources().addAll(createResourcesFromDatasetVersion(datasetVersion, metadataConfigurationService, newPackage.getName()));
        newPackage.setTags(new ArrayList<>());

        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());
        newPackage.setGroups(new ArrayList<>());

        // extra fields
        newPackage.setExtras(new ArrayList<>());
        newPackage.getExtras().addAll(getDatasetExtraFields(datasetVersion, metadataConfigurationService, externalCategoryService, externalOperationService, newPackage.getName()));

        return newPackage;
    }

    public static String getCkanPackageName(DatasetVersionAvro datasetVersion) {
        return CkanFormatUtil.formatCkanNameFromDatasetUrn((datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getIdentifiableStatisticalResource().getUrn()));
    }

    public static CkanPackage toExistingCkanPackage(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService,
            ExternalOperationService externalOperationService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        existingPackage.setId(id);
        existingPackage.setNotes(getDescriptionFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource()
                .getVersionableStatisticalResource().getNameableStatisticalResource().getDescription().getLocalisedStrings()));

        existingPackage.setUrl(datasetVersion.getVisualizerHtmlLink());
        existingPackage.setVersion(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getVersionLogic());

        // extra fields
        existingPackage.setExtras(new ArrayList<>());
        existingPackage.getExtras().addAll(getDatasetExtraFields(datasetVersion, metadataConfigurationService, externalCategoryService, externalOperationService, getCkanPackageName(datasetVersion)));

        return existingPackage;
    }

    public static List<CkanResource> createResourcesFromDatasetVersion(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService, String packageId) {
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getUrn();
        String[] params = UrnUtils.splitUrnItem(urn);
        return createResources(getDatasetDescription(datasetVersion), urn, getResourceUrlBase(metadataConfigurationService), getNameResourceBase(params[0], params[1]), packageId,
                DatasetResourcesFormat.getEnumList());
    }

    public static List<CkanResource> updateResourcesFromDatasetVersion(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService, String packageId,
            CkanPackage existingDataset) {
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getUrn();
        return updateResources(urn, getResourceUrlBase(metadataConfigurationService), packageId, existingDataset.getResources());
    }

    private static List<CkanExtra> getDatasetExtraFields(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService,
            ExternalOperationService externalOperationService, String packageName) {
        List<CkanExtra> extraFields = getExtraFields(URL_BASE_IDENTIFIER, metadataConfigurationService, packageName);

        String theme = getThemeFromStatisticalOperation(datasetVersion, metadataConfigurationService, externalCategoryService, externalOperationService);
        if (theme != null) {
            extraFields.add(new CkanExtra("theme", theme));
        }

        return extraFields;
    }

    private static String getDatasetDescription(DatasetVersionAvro datasetVersion) {
        return getDescriptionFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getDescription().getLocalisedStrings());
    }

    private static String getThemeFromStatisticalOperation(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService,
            ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService) {

        Set<String> eniThemes = new HashSet<>();
        String eniUrlBase = metadataConfigurationService.retrieveCkanEniUrlBase();

        Operation operation = externalOperationService.retrieveStatisticalOperationById(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource()
                .getVersionableStatisticalResource().getNameableStatisticalResource().getIdentifiableStatisticalResource().getStatisticalOperation().getCode());

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

    private static String getNameResourceBase(String agency, String code) {
        return BASE_DATASET_RESOURCE + agency + ":" + code;
    }

    private static String getResourceUrlBase(MetadataConfigurationService metadataConfigurationService) {
        return CkanUtil.getStatisticalResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_DATASETS;
    }

}
