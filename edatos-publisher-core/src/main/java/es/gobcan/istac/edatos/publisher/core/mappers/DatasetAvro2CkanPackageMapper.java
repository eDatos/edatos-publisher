package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.springframework.context.MessageSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanTag;
import es.gobcan.istac.edatos.publisher.core.domain.CkanUrlResource;
import es.gobcan.istac.edatos.publisher.core.domain.constants.Constants;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetResourcesFormat;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetStateEnum;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.ResourcesFormat;
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
    
    private final Logger log = LoggerFactory.getLogger(DatasetAvro2CkanPackageMapper.class);

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
        String urn = CkanFormatUtil.formatCkanNameFromDatasetUrn((datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getIdentifiableStatisticalResource().getUrn()));
        return super.getCkanPackageName(urn);
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

        existingPackage.setTags(getTags());
        // extra fields
        existingPackage.setExtras(new ArrayList<>());
        existingPackage.getExtras().addAll(getDatasetExtraFields(externalCategoryService, externalOperationService, getCkanPackageName()));

        existingPackage.setGroups(getCkanGroups());

        return existingPackage;
    }

    public List<CkanResource> createResources(String packageId) {

        return createResources(packageId, getAvailableFormats());
    }

    public List<String> deleteResources(CkanPackage existingPackage) {
        List<String> resourcesIdToDelete = new ArrayList<>();

        if (isInvalidXlsx()) {
            for (CkanResource existingResource : existingPackage.getResources()) {
                if (ResourcesFormat.XLSX.name().equals(existingResource.getFormat().toUpperCase())) {
                    resourcesIdToDelete.add(existingResource.getId());
                }
            }
        }

        return resourcesIdToDelete;
    }

    @Override
    protected List<String> getAvailableFormats() {
        List<String> formats = DatasetResourcesFormat.getEnumList();

        if (isInvalidXlsx()) {
            formats.remove(DatasetResourcesFormat.XLSX.name());
        }

        return formats;
    }

    private boolean isInvalidXlsx() {
        Long maxXlsxRows = Long.parseLong(metadataConfigurationService.retrieveMaxXlsxRows());
        if (maxXlsxRows != null && ((datasetVersion.getFormatExtentTableSize() != null && (datasetVersion.getFormatExtentTableSize() > maxXlsxRows)) || 
                (datasetVersion.getFormatExtentTableSize() == null && datasetVersion.getFormatExtentObservations() != null 
                    && (datasetVersion.getFormatExtentObservations() > maxXlsxRows)))) {
            log.info("Error: xlsx resource of the dataset with urn " + getUrn()
                    + " will not be generated because the table size or number of observations exceeds the limit");
            return true;
        } else {
            return false;
        }
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

        String theme = getTheme(externalCategoryService, externalOperationService);
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

    private String getTheme(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService) {
        return getThemeFromStatisticalOperation(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getIdentifiableStatisticalResource().getStatisticalOperation().getCode(), metadataConfigurationService, externalCategoryService,
                externalOperationService);
    }

    @Override
    protected String getNameResourceBase(String agency, String code) {
        return InternationalStringUtil.generateTextI18n(messageSource, Constants.BASE_DATASET_RESOURCE, null) + agency + ":" + code;
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
