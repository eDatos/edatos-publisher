package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.springframework.context.MessageSource;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.domain.CkanUrlResource;
import es.gobcan.istac.edatos.publisher.core.domain.constants.Constants;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetResourcesFormat;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetStateEnum;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.core.util.CkanUtil;
import es.gobcan.istac.edatos.publisher.core.util.InternationalStringUtil;

public class DatasetAvro2CkanPackageMapper extends CkanPackageMapper {

    private DatasetVersionAvro datasetVersion;
    private MetadataConfigurationService metadataConfigurationService;
    private MessageSource messageSource;

    public static final String URL_BASE_DATASETS = "datasets";
    public static final String URL_DATASET_BASE_IDENTIFIER = "dataset";

    public DatasetAvro2CkanPackageMapper(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService, MessageSource messageSource) {
        this.datasetVersion = datasetVersion;
        this.metadataConfigurationService = metadataConfigurationService;
        this.messageSource = messageSource;
    }

    public CkanPackage toNewCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService) {
        CkanPackage newPackage = new CkanPackage();
        newPackage.setName(getCkanPackageName());
        newPackage.setTitle(getDefaultLocalisedStringFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource()
                .getVersionableStatisticalResource().getNameableStatisticalResource().getTitle().getLocalisedStrings(), metadataConfigurationService.retrieveCkanDefaultLanguage()));
        newPackage.setPrivates(false);
        newPackage.setAuthor(getDefaultLocalisedStringFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getCreator().getTitle().getLocalisedStrings(),
                metadataConfigurationService.retrieveCkanDefaultLanguage()));
        newPackage.setMaintainer(getDefaultLocalisedStringFromInternationalStringItemAvro(
                datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getMaintainer().getTitle().getLocalisedStrings(),
                metadataConfigurationService.retrieveCkanDefaultLanguage()));
        newPackage.setLicense_id(metadataConfigurationService.retrieveCkanLicenceId());
        newPackage.setNotes(getDatasetDescription(metadataConfigurationService.retrieveCkanDefaultLanguage()));

        newPackage.setUrl(datasetVersion.getVisualizerHtmlLink());
        newPackage.setVersion(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getVersionLogic());

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(getDefaultOrganisation(metadataConfigurationService));
        newPackage.setResources(new ArrayList<>());
        newPackage.getResources().addAll(createResourcesFromDatasetVersion(newPackage.getName()));
        newPackage.setTags(new ArrayList<>());

        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());
        newPackage.setGroups(new ArrayList<>());

        // extra fields
        newPackage.setExtras(new ArrayList<>());
        newPackage.getExtras().addAll(getDatasetExtraFields(externalCategoryService, externalOperationService, newPackage.getName()));

        return newPackage;
    }

    public String getCkanPackageName() {
        return CkanFormatUtil.formatCkanNameFromDatasetUrn((datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getIdentifiableStatisticalResource().getUrn()));
    }

    public CkanPackage toExistingCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        existingPackage.setId(id);
        existingPackage.setNotes(getDescriptionFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource()
                .getVersionableStatisticalResource().getNameableStatisticalResource().getDescription().getLocalisedStrings(), metadataConfigurationService.retrieveCkanDefaultLanguage()));

        existingPackage.setUrl(datasetVersion.getVisualizerHtmlLink());
        existingPackage.setVersion(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getVersionLogic());

        // extra fields
        existingPackage.setExtras(new ArrayList<>());
        existingPackage.getExtras().addAll(getDatasetExtraFields(externalCategoryService, externalOperationService, getCkanPackageName()));

        return existingPackage;
    }

    public List<CkanResource> createResourcesFromDatasetVersion(String packageId) {
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getUrn();
        String[] params = UrnUtils.splitUrnItem(urn);
        return createResources(getDatasetDescription(metadataConfigurationService.retrieveCkanDefaultLanguage()), getCkanUrlResource(urn), getNameResourceBase(params[0], params[1]), packageId,
                DatasetResourcesFormat.getEnumList());
    }

    public List<CkanResource> updateResourcesFromDatasetVersion(String packageId, CkanPackage existingDataset) {
        String urn = datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getUrn();
        return updateResources(getCkanUrlResource(urn), packageId, existingDataset.getResources());
    }

    private CkanUrlResource getCkanUrlResource(String urn) {
        return new CkanUrlResource(getResourceUrlBase(urn, getResourceUrlBase()), null, null);
    }

    private List<CkanExtra> getDatasetExtraFields(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, String packageName) {
        List<CkanExtra> extraFields = getExtraFields(InternationalStringUtil.generateTextI18n(messageSource, URL_DATASET_BASE_IDENTIFIER, null), metadataConfigurationService, packageName);

        String theme = getThemeFromStatisticalOperation(externalCategoryService, externalOperationService);
        if (theme != null) {
            extraFields.add(new CkanExtra("theme", theme));
        }

        return extraFields;
    }

    private String getDatasetDescription(String defaultLanguage) {
        return getDescriptionFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getDescription().getLocalisedStrings(), defaultLanguage);
    }

    private String getThemeFromStatisticalOperation(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService) {

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

    private String getNameResourceBase(String agency, String code) {
        return InternationalStringUtil.generateTextI18n(messageSource, Constants.BASE_DATASET_RESOURCE, null) + agency + ":" + code;
    }

    private String getResourceUrlBase() {
        return CkanUtil.getStatisticalResourceUrlBase(metadataConfigurationService) + "/" + InternationalStringUtil.generateTextI18n(messageSource, URL_BASE_DATASETS, null);
    }

}
