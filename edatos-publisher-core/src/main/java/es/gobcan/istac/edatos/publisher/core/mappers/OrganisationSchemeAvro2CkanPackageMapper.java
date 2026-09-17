package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeTypeEnumAvro;
import org.springframework.context.MessageSource;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.domain.CkanUrlResource;
import es.gobcan.istac.edatos.publisher.core.domain.constants.Constants;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetStateEnum;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.core.util.CkanResourcesFormatOperation;
import es.gobcan.istac.edatos.publisher.core.util.CkanUtil;
import es.gobcan.istac.edatos.publisher.core.util.InternationalStringSrmUtil;

public class OrganisationSchemeAvro2CkanPackageMapper extends CkanPackageMapper {

    private OrganisationSchemeAvro       organisationScheme;
    private MetadataConfigurationService metadataConfigurationService;
    private MessageSource                messageSource;

    public OrganisationSchemeAvro2CkanPackageMapper(OrganisationSchemeAvro organisationScheme, MetadataConfigurationService metadataConfigurationService, MessageSource messageSource) {
        this.organisationScheme = organisationScheme;
        this.metadataConfigurationService = metadataConfigurationService;
        this.messageSource = messageSource;
    }

    private static final String URL_BASE_ORGANISATION_SCHEME = "organisationschemes";
    private static final String URL_BASE_IDENTIFIER          = "dataset";

    public CkanPackage toNewCkanPackage(ExternalCategoryService externalCategoryService) {
        CkanPackage newPackage = new CkanPackage();

        newPackage.setName(getCkanPackageName());
        toCkanPackage(externalCategoryService, newPackage);

        newPackage.setPrivates(false);
        String maintainer = getMaintainer();

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

    public void toCkanPackage(ExternalCategoryService externalCategoryService, CkanPackage ckanPackage) {
        String[] params = UrnUtils.splitUrnItem(getUrn());
        String version = params[2];

        String name = getCkanPackageName();

        ckanPackage.setTitle(getOrganisationSchemeTitle(params[0], params[1]));
        ckanPackage.setDescription(getResourceDescription());
        ckanPackage.setUrl(organisationScheme.getVisualizerHtmlLink());
        ckanPackage.setVersion(version);

        ckanPackage.setTags(getTags(organisationScheme.getOrganisationSchemeType()));
        // extra fields
        ckanPackage.setExtras(getOrganisationSchemeExtraFields(externalCategoryService, name));

        ckanPackage.setTheme(getTheme(externalCategoryService));

        setCommonMetadata(URL_BASE_IDENTIFIER, ckanPackage, metadataConfigurationService, name);

        ckanPackage.setGroups(getCkanGroups());
    }

    public CkanPackage toExistingCkanPackage(ExternalCategoryService externalCategoryService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        existingPackage.setId(id);

        toCkanPackage(externalCategoryService, existingPackage);

        return existingPackage;
    }

    private List<String> getTheme(ExternalCategoryService externalCategoryService) {
        return getDefaultTheme(externalCategoryService, metadataConfigurationService, metadataConfigurationService.retrieveCkanEniUrlBase());
    }

    private String getMaintainer() {
        if (organisationScheme.getMaintainer() != null) {
            return InternationalStringSrmUtil.getDefaultLocalisedStringFromLocalisedStringAvro(organisationScheme.getMaintainer().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
        } else {
            return getDefaultOrganisation(metadataConfigurationService);
        }
    }

    @Override
    public String getCkanPackageName() {
        return super.getCkanPackageName(CkanFormatUtil.formatCkanNameFromDatasetUrn(getUrn()));
    }

    private Map<String, List<String>> getTags(OrganisationSchemeTypeEnumAvro organisationSchemeTypeEnumAvro) {

        List<String> tagTexts = new ArrayList<>();
        tagTexts.add(Constants.ORGANISATION_TAG);

        switch (organisationSchemeTypeEnumAvro) {
            case ORGANISATION_UNIT_SCHEME:
                tagTexts.add(Constants.ORGANISATION_UNIT_TAG);
                break;
            case AGENCY_SCHEME:
                tagTexts.add(Constants.ORGANISATION_AGENCY_TAG);
                break;
            case DATA_PROVIDER_SCHEME:
                tagTexts.add(Constants.ORGANISATION_DATA_PROVIDER_TAG);
                break;
            case DATA_CONSUMER_SCHEME:
                tagTexts.add(Constants.ORGANISATION_DATA_CONSUMER_TAG);
                break;
        }

        return InternationalStringSrmUtil.generateCkanInternationalStringMessage(messageSource, tagTexts, null, metadataConfigurationService.retrieveInternationalizationLanguages());

    }

    public List<CkanResource> createResources(String packageId) {

        return createResources(packageId, getAvailableFormats());
    }

    @Override
    protected CkanUrlResource getCkanUrlResource() {
        CkanUrlResource ckanUrlResource = new CkanUrlResource(getResourceUrlBase(getUrn(), getResourceUrlBase()), Constants.URL_RESOURCES_ORGANISATIONS, null);
        ckanUrlResource.setHtmlUrl(organisationScheme.getVisualizerHtmlLink());
        return ckanUrlResource;
    }

    private List<CkanExtra> getOrganisationSchemeExtraFields(ExternalCategoryService externalCategoryService, String packageName) {
        return getExtraFields(URL_BASE_IDENTIFIER, metadataConfigurationService, packageName);
    }

    @Override
    protected Map<String, String> getResourceDescription() {
        if (organisationScheme.getDescription() != null) {
            return InternationalStringSrmUtil.getSrmInternationalStringToCkanInternationalStringMarkDown(organisationScheme.getDescription(), null);
        }
        return null;
    }

    @Override
    protected Map<String, String> getNameResourceBase(String agency, String code) {
        return InternationalStringSrmUtil.generateCkanInternationalStringMessage(messageSource, Constants.BASE_ORGANISATION_RESOURCE, null,
                metadataConfigurationService.retrieveInternationalizationLanguages(), agency + ":" + code);
    }

    @Override
    protected String getUrn() {
        return organisationScheme.getUrn();
    }

    @Override
    protected List<String> getAvailableFormats() {
        return filterHtmlFormat(CkanResourcesFormatOperation.getSrmResourcesFormat(), organisationScheme.getVisualizerHtmlLink());
    }

    private String getResourceUrlBase() {
        return CkanUtil.getStructuralResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_ORGANISATION_SCHEME;
    }

    private Map<String, String> getOrganisationSchemeTitle(String agency, String resourceId) {
        return InternationalStringSrmUtil.getSrmInternationalStringToCkanInternationalString(organisationScheme.getName(), getCkanEndTitle(agency, resourceId));
    }
}
