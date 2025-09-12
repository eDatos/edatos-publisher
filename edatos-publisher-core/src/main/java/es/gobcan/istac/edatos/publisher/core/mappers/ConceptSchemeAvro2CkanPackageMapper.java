package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeTypeEnumAvro;
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

public class ConceptSchemeAvro2CkanPackageMapper extends CkanPackageMapper {

    private final Logger                 log = LoggerFactory.getLogger(ConceptSchemeAvro2CkanPackageMapper.class);

    private ConceptSchemeAvro            conceptScheme;
    private MetadataConfigurationService metadataConfigurationService;
    private MessageSource                messageSource;

    public ConceptSchemeAvro2CkanPackageMapper(ConceptSchemeAvro conceptScheme, MetadataConfigurationService metadataConfigurationService, MessageSource messageSource) {
        this.conceptScheme = conceptScheme;
        this.metadataConfigurationService = metadataConfigurationService;
        this.messageSource = messageSource;
    }

    private static final String URL_BASE_CONCEPT_SCHEME = "conceptschemes";
    private static final String URL_BASE_IDENTIFIER     = "dataset";

    public CkanPackage toNewCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService) {
        CkanPackage newPackage = new CkanPackage();

        newPackage.setName(getCkanPackageName());

        toCkanPackage(externalCategoryService, externalOperationService, newPackage);
        newPackage.setPrivates(false);
        String maintainer = InternationalStringSrmUtil.getDefaultLocalisedStringFromLocalisedStringAvro(conceptScheme.getMaintainer().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
        newPackage.setAuthor(maintainer);
        newPackage.setMaintainer(maintainer);
        newPackage.setLicenceId(metadataConfigurationService.retrieveCkanLicenceId());

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(getDefaultOrganisation(metadataConfigurationService));
        newPackage.setResources(createResources(newPackage.getName()));

        // empty metadata
        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());

        return newPackage;
    }

    public CkanPackage toExistingCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        existingPackage.setId(id);

        toCkanPackage(externalCategoryService, externalOperationService, existingPackage);

        return existingPackage;
    }

    public void toCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, CkanPackage ckanPackage) {
        String[] params = UrnUtils.splitUrnItem(getUrn());
        String version = params[2];

        String name = getCkanPackageName();

        ckanPackage.setTitle(getConceptSchemeTitle(params[0], params[1]));
        ckanPackage.setDescription(getResourceDescription());
        ckanPackage.setVersion(version);

        ckanPackage.setTags(getTags());
        // extra fields
        ckanPackage.setExtras(getConceptSchemeExtraFields(externalOperationService, name));

        ckanPackage.setTheme(getTheme(externalCategoryService, externalOperationService, conceptScheme.getConceptSchemeType(),
                conceptScheme.getStatisticalOperation() != null ? conceptScheme.getStatisticalOperation().getCode() : null));

        setCommonMetadata(URL_BASE_IDENTIFIER, ckanPackage, metadataConfigurationService, name);

        ckanPackage.setGroups(getCkanGroups());
    }

    @Override
    public String getCkanPackageName() {
        String urn = CkanFormatUtil.formatCkanNameFromDatasetUrn(getUrn());
        return super.getCkanPackageName(urn);
    }

    public List<CkanResource> createResources(String packageId) {

        return createResources(packageId, getAvailableFormats());
    }

    @Override
    protected CkanUrlResource getCkanUrlResource() {
        return new CkanUrlResource(getResourceUrlBase(getUrn(), getResourceUrlBase()), Constants.URL_RESOURCES_CONCEPTS, "fields=+description");
    }

    private List<CkanExtra> getConceptSchemeExtraFields(ExternalOperationService externalOperationService, String packageName) {

        return getExtraFields(URL_BASE_IDENTIFIER, metadataConfigurationService, packageName);
    }

    private List<String> getTheme(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, ConceptSchemeTypeEnumAvro conceptSchemeType,
            String stadisticalOperationCode) {
        if (ConceptSchemeTypeEnumAvro.OPERATION.equals(conceptSchemeType)) {
            log.debug("getTheme() with statisticalOperation for process concept urn " + getUrn());
            return getThemeFromStatisticalOperation(stadisticalOperationCode, metadataConfigurationService, externalCategoryService, externalOperationService);
        } else {
            return getDefaultTheme(externalCategoryService, metadataConfigurationService, metadataConfigurationService.retrieveCkanEniUrlBase());
        }
    }

    private Map<String, List<String>> getTags() {
        return InternationalStringSrmUtil.generateCkanInternationalStringMessage(messageSource, Arrays.asList(Constants.CONCEPT_TAG), null,
                metadataConfigurationService.retrieveInternationalizationLanguages());
    }

    @Override
    protected Map<String, String> getResourceDescription() {
        if (conceptScheme.getDescription() != null) {
            return InternationalStringSrmUtil.getSrmInternationalStringToCkanInternationalStringMarkDown(conceptScheme.getDescription(), null);
        }
        return null;
    }

    protected String getCkanEndTitle(String agency, String resourceId) {
        return " (" + agency + ": " + resourceId + ")";
    }

    private Map<String, String> getConceptSchemeTitle(String agency, String resourceId) {
        return InternationalStringSrmUtil.getSrmInternationalStringToCkanInternationalString(conceptScheme.getName(), getCkanEndTitle(agency, resourceId));

    }

    @Override
    protected Map<String, String> getNameResourceBase(String agency, String code) {
        return InternationalStringSrmUtil.generateCkanInternationalStringMessage(messageSource, Constants.BASE_CONCEPT_RESOURCE, null,
                metadataConfigurationService.retrieveInternationalizationLanguages(), agency + ":" + code);
    }

    @Override
    protected String getUrn() {
        return conceptScheme.getUrn();
    }

    @Override
    protected List<String> getAvailableFormats() {
        return CkanResourcesFormatOperation.getSrmResourcesFormat();
    }

    private String getResourceUrlBase() {
        return CkanUtil.getStructuralResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_CONCEPT_SCHEME;
    }

}
