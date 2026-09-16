package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.siemac.edatos.core.common.enume.utils.ResourcesFormat;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.domain.CkanUrlResource;
import es.gobcan.istac.edatos.publisher.core.domain.constants.Constants;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetStateEnum;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.core.util.CkanResourcesFormatOperation;
import es.gobcan.istac.edatos.publisher.core.util.CkanUtil;
import es.gobcan.istac.edatos.publisher.core.util.InternationalStringSrmUtil;
import es.gobcan.istac.edatos.publisher.core.util.InternationalStringStatisticalResourcesUtil;

public class DatasetAvro2CkanPackageMapper extends CkanPackageMapper {

    private DatasetVersionAvro           datasetVersion;
    private MetadataConfigurationService metadataConfigurationService;
    private MessageSource                messageSource;

    public static final String           URL_BASE_DATASETS           = "datasets";
    public static final String           URL_DATASET_BASE_IDENTIFIER = "dataset";

    private final Logger                 log                         = LoggerFactory.getLogger(DatasetAvro2CkanPackageMapper.class);

    public DatasetAvro2CkanPackageMapper(DatasetVersionAvro datasetVersion, MetadataConfigurationService metadataConfigurationService, MessageSource messageSource) {
        this.datasetVersion = datasetVersion;
        this.metadataConfigurationService = metadataConfigurationService;
        this.messageSource = messageSource;
    }

    public CkanPackage toNewCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService) {
        CkanPackage newPackage = new CkanPackage();
        newPackage.setName(getCkanPackageName());

        toCkanPackage(externalCategoryService, externalOperationService, newPackage);

        newPackage.setPrivates(false);
        newPackage.setAuthor(getAuthor());

        newPackage.setMaintainer(getMaintainer());
        newPackage.setLicenceId(metadataConfigurationService.retrieveCkanLicenceId());
        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(getDefaultOrganisation(metadataConfigurationService));
        newPackage.setResources(new ArrayList<>());
        newPackage.getResources().addAll(createResources(newPackage.getName()));

        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());

        if (log.isDebugEnabled()) {
            debugPackage(log, newPackage);
        }

        return newPackage;
    }

    public void toCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, CkanPackage ckanPackage) {

        String packageName = getCkanPackageName();

        ckanPackage.setTitle(getTitle());
        ckanPackage.setDescription(getResourceDescription());
        ckanPackage.setUrl(datasetVersion.getVisualizerHtmlLink());

        setCommonMetadata(URL_DATASET_BASE_IDENTIFIER, ckanPackage, metadataConfigurationService, packageName);

        ckanPackage.setVersion(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getVersionLogic());
        ckanPackage.setTags(getTags());

        // extra fields
        ckanPackage.setExtras(new ArrayList<>());
        ckanPackage.getExtras().addAll(getDatasetExtraFields(externalCategoryService, externalOperationService, packageName));

        ckanPackage.setTheme(getTheme(externalCategoryService, externalOperationService));

        ckanPackage.setGroups(getCkanGroups());
    }

    public CkanPackage toExistingCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        existingPackage.setId(id);

        toCkanPackage(externalCategoryService, externalOperationService, existingPackage);

        if (log.isDebugEnabled()) {
            debugPackage(log, existingPackage);
        }

        return existingPackage;
    }

    public String getCkanPackageName() {
        String urn = CkanFormatUtil.formatCkanNameFromDatasetUrn((datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getIdentifiableStatisticalResource().getUrn()));
        return super.getCkanPackageName(urn);
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
        List<String> formats = new ArrayList<>(CkanResourcesFormatOperation.getDatasetResourcesFormat());

        if (isInvalidXlsx()) {
            formats.remove(ResourcesFormat.XLSX.name());
        }
        if (datasetVersion.getVisualizerHtmlLink() == null) {
            formats.remove(ResourcesFormat.HTML.name());
        }
        return formats;
    }

    private boolean isInvalidXlsx() {
        Long maxXlsxRows = Long.parseLong(metadataConfigurationService.retrieveMaxXlsxRows());
        if (maxXlsxRows != null && ((datasetVersion.getFormatExtentTableSize() != null && (datasetVersion.getFormatExtentTableSize() > maxXlsxRows))
                || (datasetVersion.getFormatExtentTableSize() == null && datasetVersion.getFormatExtentObservations() != null && (datasetVersion.getFormatExtentObservations() > maxXlsxRows)))) {
            log.info("Error: xlsx resource of the dataset with urn " + getUrn() + " will not be generated because the table size or number of observations exceeds the limit");
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
        CkanUrlResource ckanUrlResource = new CkanUrlResource(getResourceUrlBase(getUrn(), getResourceUrlBase()), null, null);
        ckanUrlResource.setHtmlUrl(datasetVersion.getVisualizerHtmlLink());
        return ckanUrlResource;
    }

    private List<CkanExtra> getDatasetExtraFields(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, String packageName) {
        List<CkanExtra> extraFields = getExtraFields(URL_DATASET_BASE_IDENTIFIER, metadataConfigurationService, packageName);
        return extraFields;
    }

    @Override
    protected Map<String, String> getResourceDescription() {
        return InternationalStringStatisticalResourcesUtil.getSrmInternationalStringToCkanInternationalStringMarkDown(
                datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource().getDescription(), null);
    }

    private List<String> getTheme(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService) {
        log.debug("getTheme() with statisticalOperation for process dataset urn " + getUrn());
        return getThemeFromStatisticalOperation(datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource()
                .getNameableStatisticalResource().getIdentifiableStatisticalResource().getStatisticalOperation().getCode(), metadataConfigurationService, externalCategoryService,
                externalOperationService);
    }

    @Override
    protected Map<String, String> getNameResourceBase(String agency, String code) {
        return InternationalStringStatisticalResourcesUtil.generateCkanInternationalStringMessage(messageSource, Constants.BASE_DATASET_RESOURCE, null,
                metadataConfigurationService.retrieveInternationalizationLanguages(), agency + ":" + code);
    }

    private Map<String, String> getTitle() {
        return InternationalStringStatisticalResourcesUtil.getStatisticalResourcesInternationalStringTitleToCkanInternationalString(
                datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getVersionableStatisticalResource().getNameableStatisticalResource().getTitle(), null);
    }

    private String getAuthor() {
        return InternationalStringStatisticalResourcesUtil
                .getDefaultLocalisedStringFromInternationalStringItemAvro(datasetVersion.getSiemacMetadataStatisticalResource().getCreator().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
    }

    private String getMaintainer() {
        return InternationalStringStatisticalResourcesUtil.getDefaultLocalisedStringFromInternationalStringItemAvro(
                datasetVersion.getSiemacMetadataStatisticalResource().getLifecycleStatisticalResource().getMaintainer().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
    }

    private String getResourceUrlBase() {
        return CkanUtil.getStatisticalResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_DATASETS;
    }

    private Map<String, List<String>> getTags() {
        return InternationalStringSrmUtil.generateCkanInternationalStringMessage(messageSource, Arrays.asList(Constants.DATASET_TAG), null,
                metadataConfigurationService.retrieveInternationalizationLanguages());
    }
}
