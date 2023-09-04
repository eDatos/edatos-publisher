package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operation;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.springframework.context.MessageSource;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanTag;
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

    private DatasetVersionAvro           datasetVersion;
    private MetadataConfigurationService metadataConfigurationService;
    private MessageSource                messageSource;

    public static final String           URL_BASE_DATASETS           = "datasets";
    public static final String           URL_DATASET_BASE_IDENTIFIER = "dataset";

    public DatasetAvro2CkanPackageMapper(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService, MessageSource messageSource) {
        this.datasetVersion = datasetVersion;
        this.metadataConfigurationService = metadataConfigurationService;
        this.messageSource = messageSource;
    }

    public CkanPackage toNewCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService) {
        CkanPackage newPackage = new CkanPackage();
        newPackage.setName(getCkanPackageName());
        newPackage.setTitle(getDefaultLocalisedStringFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource()
                .getVersionableStatisticalResource().getNameableStatisticalResource().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE));
        newPackage.setPrivates(false);
        newPackage.setAuthor(
                getDefaultLocalisedStringFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getCreator().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE));
        newPackage.setMaintainer(getDefaultLocalisedStringFromInternationalStringItemAvro(
                datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getMaintainer().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE));
        newPackage.setLicense_id(metadataConfigurationService.retrieveCkanLicenceId());
        newPackage.setNotes(getResourceDescription());

        newPackage.setUrl(datasetVersion.getVisualizerHtmlLink());
        newPackage.setVersion(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getVersionLogic());

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(getDefaultOrganisation(metadataConfigurationService));
        newPackage.setResources(new ArrayList<>());
        newPackage.getResources().addAll(createResources(newPackage.getName()));
        newPackage.setTags(getTags());

        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());

        // extra fields
        newPackage.setExtras(new ArrayList<>());
        newPackage.getExtras().addAll(getDatasetExtraFields(externalCategoryService, externalOperationService, newPackage.getName()));

        newPackage.setGroups(getCkanGroups());

        return newPackage;
    }

    public String getCkanPackageName() {
        return CkanFormatUtil.formatCkanNameFromDatasetUrn((datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getIdentifiableStatisticalResource().getUrn()));
    }

    public CkanPackage toExistingCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        existingPackage.setId(id);
        existingPackage.setTitle(getDefaultLocalisedStringFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource()
                .getVersionableStatisticalResource().getNameableStatisticalResource().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE));
        existingPackage.setNotes(getDescriptionFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource()
                .getVersionableStatisticalResource().getNameableStatisticalResource().getDescription().getLocalisedStrings(), Constants.CKAN_LANGUAGE));

        existingPackage.setUrl(datasetVersion.getVisualizerHtmlLink());
        existingPackage.setVersion(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getVersionLogic());

        // extra fields
        existingPackage.setExtras(new ArrayList<>());
        existingPackage.getExtras().addAll(getDatasetExtraFields(externalCategoryService, externalOperationService, getCkanPackageName()));

        existingPackage.setGroups(getCkanGroups());

        return existingPackage;
    }

    public List<CkanResource> createResources(String packageId) {

        return createResources(packageId, getAvailableFormats());
    }

    @Override
    protected String getUrn() {
        return datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource()
                .getIdentifiableStatisticalResource().getUrn();
    }

    @Override
    protected CkanUrlResource getCkanUrlResource() {
        return new CkanUrlResource(getResourceUrlBase(getUrn(), getResourceUrlBase()), null, null);
    }

    private List<CkanExtra> getDatasetExtraFields(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, String packageName) {
        List<CkanExtra> extraFields = getExtraFields(URL_DATASET_BASE_IDENTIFIER, metadataConfigurationService, packageName);

        String theme = getThemeFromStatisticalOperation(externalCategoryService, externalOperationService);
        if (theme != null) {
            extraFields.add(new CkanExtra("theme", theme));
        }

        return extraFields;
    }

    @Override
    protected String getResourceDescription() {
        return getDescriptionFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getDescription().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
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

    @Override
    protected String getNameResourceBase(String agency, String code) {
        return InternationalStringUtil.generateTextI18n(messageSource, Constants.BASE_DATASET_RESOURCE, null) + agency + ":" + code;
    }

    @Override
    protected List<String> getAvailableFormats() {
        return DatasetResourcesFormat.getEnumList();
    }

    private String getResourceUrlBase() {
        return CkanUtil.getStatisticalResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_DATASETS;
    }

    private List<CkanTag> getTags() {
        List<CkanTag> tags = new ArrayList<>();

        tags.add(new CkanTag(InternationalStringUtil.generateTextI18n(messageSource, Constants.DATASET_TAG, null)));

        return tags;

    }

}
