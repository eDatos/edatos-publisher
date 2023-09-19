package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeTypeEnumAvro;
import org.springframework.context.MessageSource;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanTag;
import es.gobcan.istac.edatos.publisher.core.domain.CkanUrlResource;
import es.gobcan.istac.edatos.publisher.core.domain.constants.Constants;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetStateEnum;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.SrmResourcesFormat;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.core.util.CkanUtil;
import es.gobcan.istac.edatos.publisher.core.util.InternationalStringUtil;

public class ConceptSchemeAvro2CkanPackageMapper extends CkanPackageMapper {

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

        String[] params = UrnUtils.splitUrnItem(getUrn());
        String version = params[2];

        newPackage.setName(getCkanPackageName());
        newPackage.setTitle(getConceptSchemeTitle(params[0], params[1]));
        newPackage.setPrivates(false);
        String maintainer = getDefaultLocalisedStringFromLocalisedStringAvro(conceptScheme.getMaintainer().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
        newPackage.setAuthor(maintainer);
        newPackage.setMaintainer(maintainer);
        newPackage.setLicense_id(metadataConfigurationService.retrieveCkanLicenceId());
        newPackage.setNotes(getResourceDescription());

        newPackage.setVersion(version);

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(getDefaultOrganisation(metadataConfigurationService));
        newPackage.setResources(createResources(newPackage.getName()));
        newPackage.setTags(getTags());

        // empty metadata
        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());

        // extra fields
        newPackage.setExtras(getConceptSchemeExtraFields(externalCategoryService, externalOperationService, conceptScheme.getConceptSchemeType(), newPackage.getName(),
                conceptScheme.getStatisticalOperation() != null ? conceptScheme.getStatisticalOperation().getCode() : null));

        newPackage.setGroups(getCkanGroups());

        return newPackage;
    }

    @Override
    public String getCkanPackageName() {
        String urn = CkanFormatUtil.formatCkanNameFromDatasetUrn(getUrn());
        return super.getCkanPackageName(urn);
    }

    public CkanPackage toExistingCkanPackage(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(getUrn());
        String version = params[2];

        existingPackage.setId(id);
        existingPackage.setTitle(getDefaultLocalisedStringFromLocalisedStringAvro(conceptScheme.getName().getLocalisedStrings(), Constants.CKAN_LANGUAGE));
        existingPackage.setNotes(getResourceDescription());

        existingPackage.setVersion(version);

        existingPackage.setTags(getTags());
        // extra fields
        existingPackage.setExtras(getConceptSchemeExtraFields(externalCategoryService, externalOperationService, conceptScheme.getConceptSchemeType(), getCkanPackageName(),
                conceptScheme.getStatisticalOperation() != null ? conceptScheme.getStatisticalOperation().getCode() : null));

        existingPackage.setGroups(getCkanGroups());

        return existingPackage;
    }

    private List<CkanTag> getTags() {
        List<CkanTag> tags = new ArrayList<>();

        tags.add(new CkanTag(InternationalStringUtil.generateTextI18n(messageSource, Constants.CONCEPT_TAG, null)));

        return tags;
    }

    public List<CkanResource> createResources(String packageId) {

        return createResources(packageId, getAvailableFormats());
    }

    @Override
    protected CkanUrlResource getCkanUrlResource() {
        return new CkanUrlResource(getResourceUrlBase(getUrn(), getResourceUrlBase()), Constants.URL_RESOURCES_CONCEPTS, "fields=+description");
    }

    private List<CkanExtra> getConceptSchemeExtraFields(ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService, ConceptSchemeTypeEnumAvro conceptSchemeType,
            String packageName, String stadisticalOperationCode) {

        List<CkanExtra> extraFields = getExtraFields(URL_BASE_IDENTIFIER, metadataConfigurationService, packageName);
        extraFields.add(new CkanExtra("theme", getTheme(stadisticalOperationCode, conceptSchemeType, metadataConfigurationService, externalCategoryService, externalOperationService)));

        return extraFields;
    }

    private String getTheme(String stadisticalOperationCode, ConceptSchemeTypeEnumAvro conceptSchemeType, MetadataConfigurationService metadataConfigurationService,
            ExternalCategoryService externalCategoryService, ExternalOperationService externalOperationService) {
        if (ConceptSchemeTypeEnumAvro.OPERATION.equals(conceptSchemeType)) {
            return getThemeFromStatisticalOperation(stadisticalOperationCode, metadataConfigurationService, externalCategoryService, externalOperationService);
        } else {
            return getDefaultTheme(externalCategoryService, metadataConfigurationService, metadataConfigurationService.retrieveCkanEniUrlBase());
        }
    }

    @Override
    protected String getResourceDescription() {
        if (conceptScheme.getDescription() != null) {
            return getDescriptionFromLocalisedStringAvro(conceptScheme.getDescription().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
        }
        return null;
    }

    @Override
    protected String getNameResourceBase(String agency, String code) {
        return InternationalStringUtil.generateTextI18n(messageSource, Constants.BASE_CONCEPT_RESOURCE, null) + agency + ":" + code;
    }

    @Override
    protected String getUrn() {
        return conceptScheme.getUrn();
    }

    @Override
    protected List<String> getAvailableFormats() {
        return SrmResourcesFormat.getEnumList();
    }

    private String getResourceUrlBase() {
        return CkanUtil.getStructuralResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_CONCEPT_SCHEME;
    }

    private String getConceptSchemeTitle(String agency, String resourceId) {
        return getCkanTitle(getDefaultLocalisedStringFromLocalisedStringAvro(conceptScheme.getName().getLocalisedStrings(), Constants.CKAN_LANGUAGE), agency, resourceId);
    }
}
