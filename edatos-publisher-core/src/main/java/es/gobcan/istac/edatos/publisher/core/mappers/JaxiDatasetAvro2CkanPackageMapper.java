package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.siemac.edatos.core.common.enume.utils.ResourcesFormat;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.core.JsonProcessingException;

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
import es.gobcan.istac.edatos.publisher.core.util.InternationalStringStatisticalResourcesUtil;
import es.ibestat.jaxi.stream.messages.DatasetAvro;

public class JaxiDatasetAvro2CkanPackageMapper extends CkanPackageMapper {

    private DatasetAvro                  jaxiDataset;
    private MetadataConfigurationService metadataConfigurationService;
    private MessageSource                messageSource;
    private Map<String, String>          title;

    public static final String           URL_BASE_DATASETS           = "datasets";
    public static final String           URL_DATASET_BASE_IDENTIFIER = "dataset";

    public JaxiDatasetAvro2CkanPackageMapper(DatasetAvro jaxiDataset, MetadataConfigurationService metadataConfigurationService, MessageSource messageSource) {
        this.jaxiDataset = jaxiDataset;
        this.metadataConfigurationService = metadataConfigurationService;
        this.messageSource = messageSource;
        this.setTitle(getInternationalTitle());
    }

    public CkanPackage toNewCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService) throws JsonProcessingException {
        CkanPackage newPackage = new CkanPackage();

        newPackage.setName(getCkanPackageName());
        newPackage.setAuthor(metadataConfigurationService.retrieveCkanDefaultCreator());
        newPackage.setPrivates(false);
        newPackage.setMaintainer(newPackage.getAuthor());
        newPackage.setLicenceId(metadataConfigurationService.retrieveCkanLicenceId());

        toCkanPackage(externalCategoryService, externalOperationService, newPackage);

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(getDefaultOrganisation(metadataConfigurationService));
        newPackage.setResources(new ArrayList<>());
        newPackage.getResources().addAll(createResources(newPackage.getName()));

        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());

        return newPackage;
    }

    public CkanPackage toExistingCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, String id) throws JsonProcessingException {
        CkanPackage existingPackage = new CkanPackage();

        existingPackage.setId(id);
        toCkanPackage(externalCategoryService, externalOperationService, existingPackage);

        return existingPackage;
    }

    public void toCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, CkanPackage ckanPackage) throws JsonProcessingException {
        ckanPackage.setTitle(getTitle());
        ckanPackage.setDescription(getResourceDescription());

        ckanPackage.setUrl(jaxiDataset.getHtmlLink());
        ckanPackage.setVersion("0");

        setCommonMetadata(URL_DATASET_BASE_IDENTIFIER, ckanPackage, metadataConfigurationService, getCkanPackageName());

        ckanPackage.setTags(getTags());
        // extra fields
        ckanPackage.setExtras(new ArrayList<>());
        ckanPackage.getExtras().addAll(getDatasetExtraFields(externalCategoryService, externalOperationService, getCkanPackageName()));

        ckanPackage.setTheme(getTheme(externalCategoryService, externalOperationService));

        ckanPackage.setGroups(getCkanGroups());
    }

    public List<CkanResource> createResources(String packageId) {

        return createResources(packageId, getAvailableFormats());
    }

    public String getCkanPackageName() {
        String urn = CkanFormatUtil.formatCkanNameFromDatasetUrn((jaxiDataset.getUrn()));
        return super.getCkanPackageName(urn);
    }

    @Override
    protected List<String> getAvailableFormats() {
        return CkanResourcesFormatOperation.getJaxiDatasetResourcesFormat();
    }

    @Override
    protected String getUrn() {
        return jaxiDataset.getUrn();
    }

    @Override
    protected CkanUrlResource getCkanUrlResource() {
        CkanUrlResource ckanUrlResource = new CkanUrlResource(true);
        ckanUrlResource.addJaxiUrlByFormat(ResourcesFormat.PCAXIS.name(), jaxiDataset.getHtmlLink());
        // json url does not come with extension. This is a problem in ckan because download the file with .txt extension.
        ckanUrlResource.addJaxiUrlByFormat(ResourcesFormat.JSON.name(), getUrlWithExtensionJaxiJsonLink(jaxiDataset.getJsonLink()));
        return ckanUrlResource;
    }

    private String getUrlWithExtensionJaxiJsonLink(String url) {
        if (url != null) {
            String extension = "";
            if (url.contains(".")) {
                extension = url.substring(url.lastIndexOf(".") + 1);
            }
            if (!ResourcesFormat.JSON.name().equals(extension.toUpperCase())) {
                return url + "." + ResourcesFormat.JSON.name().toLowerCase();
            }
        }
        return url;
    }

    private List<CkanExtra> getDatasetExtraFields(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, String packageName) {
        List<CkanExtra> extraFields = getExtraFields(URL_DATASET_BASE_IDENTIFIER, metadataConfigurationService, packageName);
        return extraFields;
    }

    @Override
    protected Map<String, String> getResourceDescription() {
        return getTitle();
    }

    private Map<String, String> getInternationalTitle() {
        String defaultTerritory = metadataConfigurationService.retrieveCkanDefaultTerritoryDescription();
        return InternationalStringStatisticalResourcesUtil.getStatisticalResourcesInternationalStringTitleToCkanInternationalString(jaxiDataset.getTitle(), defaultTerritory);
    }

    private List<String> getTheme(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService) {
        return getThemeFromStatisticalOperation(jaxiDataset.getStatisticalOperation().getCode(), metadataConfigurationService, externalCategoryService, externalOperationService);
    }

    @Override
    protected Map<String, String> getNameResourceBase(String agency, String code) {
        return InternationalStringStatisticalResourcesUtil.generateCkanInternationalStringMessage(messageSource, Constants.BASE_DATASET_RESOURCE, null,
                metadataConfigurationService.retrieveInternationalizationLanguages(), agency + ":" + code);
    }

    private Map<String, List<String>> getTags() {
        return InternationalStringStatisticalResourcesUtil.generateCkanInternationalStringMessage(messageSource, Arrays.asList(Constants.DATASET_TAG), null,
                metadataConfigurationService.retrieveInternationalizationLanguages());

    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public Map<String, String> getTitle() {
        return title;
    }
}
