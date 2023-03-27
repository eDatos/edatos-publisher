package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeTypeEnumAvro;
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
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.core.util.CkanUtil;
import es.gobcan.istac.edatos.publisher.core.util.InternationalStringUtil;

public class OrganisationSchemeAvro2CkanPackageMapper extends CkanPackageMapper {

    private OrganisationSchemeAvro organisationScheme;
    private MetadataConfigurationService metadataConfigurationService;
    private MessageSource messageSource;

    public OrganisationSchemeAvro2CkanPackageMapper(OrganisationSchemeAvro organisationScheme, MetadataConfigurationService metadataConfigurationService, MessageSource messageSource) {
        this.organisationScheme = organisationScheme;
        this.metadataConfigurationService = metadataConfigurationService;
        this.messageSource = messageSource;
    }

    private static final String URL_BASE_ORGANISATION_SCHEME = "organisationschemes";
    private static final String URL_BASE_IDENTIFIER = "dataset";

    public CkanPackage toNewCkanPackage(ExternalCategoryService externalCategoryService) {
        CkanPackage newPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(getUrn());
        String version = params[2];

        newPackage.setName(getCkanPackageName());
        newPackage.setTitle(getDefaultLocalisedStringFromLocalisedStringAvro(organisationScheme.getName().getLocalisedStrings(), Constants.CKAN_LANGUAGE));
        newPackage.setPrivates(false);
        String maintainer = getMaintainer();

        newPackage.setAuthor(maintainer);
        newPackage.setMaintainer(maintainer);
        newPackage.setLicense_id(metadataConfigurationService.retrieveCkanLicenceId());
        newPackage.setNotes(getResourceDescription());

        newPackage.setVersion(version);

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(getDefaultOrganisation(metadataConfigurationService));
        newPackage.setResources(createResources(newPackage.getName()));
        newPackage.setTags(getTags(organisationScheme.getOrganisationSchemeType()));

        // empty metadata
        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());
        newPackage.setGroups(new ArrayList<>());

        // extra fields
        newPackage.setExtras(getOrganisationSchemeExtraFields(externalCategoryService, newPackage.getName()));

        return newPackage;
    }

    private String getMaintainer() {
        if (organisationScheme.getMaintainer() != null) {
            return getDefaultLocalisedStringFromLocalisedStringAvro(organisationScheme.getMaintainer().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
        } else {
            return getDefaultOrganisation(metadataConfigurationService);
        }
    }

    public String getCkanPackageName() {
        return CkanFormatUtil.formatCkanNameFromDatasetUrn(getUrn());
    }

    public CkanPackage toExistingCkanPackage(ExternalCategoryService externalCategoryService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(getUrn());
        String version = params[2];

        existingPackage.setId(id);
        existingPackage.setNotes(getResourceDescription());

        existingPackage.setVersion(version);

        // extra fields
        existingPackage.setExtras(getOrganisationSchemeExtraFields(externalCategoryService, getCkanPackageName()));

        return existingPackage;
    }

    private List<CkanTag> getTags(OrganisationSchemeTypeEnumAvro organisationSchemeTypeEnumAvro) {
        List<CkanTag> tags = new ArrayList<>();

        tags.add(new CkanTag(InternationalStringUtil.generateTextI18n(messageSource, Constants.ORGANISATION_TAG, null)));

        switch (organisationSchemeTypeEnumAvro) {
            case ORGANISATION_UNIT_SCHEME:
                tags.add(new CkanTag(InternationalStringUtil.generateTextI18n(messageSource, Constants.ORGANISATION_UNIT_TAG, null)));
                break;
            case AGENCY_SCHEME:
                tags.add(new CkanTag(InternationalStringUtil.generateTextI18n(messageSource, Constants.ORGANISATION_AGENCY_TAG, null)));
                break;
            case DATA_PROVIDER_SCHEME:
                tags.add(new CkanTag(InternationalStringUtil.generateTextI18n(messageSource, Constants.ORGANISATION_DATA_PROVIDER_TAG, null)));
                break;
            case DATA_CONSUMER_SCHEME:
                tags.add(new CkanTag(InternationalStringUtil.generateTextI18n(messageSource, Constants.ORGANISATION_DATA_CONSUMER_TAG, null)));
                break;
        }

        return tags;

    }

    public List<CkanResource> createResources(String packageId) {

        return createResources(packageId, getAvailableFormats());
    }

    @Override
    protected CkanUrlResource getCkanUrlResource() {
        return new CkanUrlResource(getResourceUrlBase(getUrn(), getResourceUrlBase()), Constants.URL_RESOURCES_ORGANISATIONS, null);
    }

    private List<CkanExtra> getOrganisationSchemeExtraFields(ExternalCategoryService externalCategoryService, String packageName) {
        List<CkanExtra> extraFields = getExtraFields(URL_BASE_IDENTIFIER, metadataConfigurationService, packageName);

        extraFields.add(new CkanExtra("theme", getDefaultTheme(externalCategoryService, metadataConfigurationService, metadataConfigurationService.retrieveCkanEniUrlBase())));

        return extraFields;
    }

    @Override
    protected String getResourceDescription() {
        if (organisationScheme.getDescription() != null) {
            return getDescriptionFromLocalisedStringAvro(organisationScheme.getDescription().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
        }
        return null;
    }

    @Override
    protected String getNameResourceBase(String agency, String code) {
        return InternationalStringUtil.generateTextI18n(messageSource, Constants.BASE_ORGANISATION_RESOURCE, null) + agency + ":" + code;
    }

    @Override
    protected String getUrn() {
        return organisationScheme.getUrn();
    }

    @Override
    protected List<String> getAvailableFormats() {
        return SrmResourcesFormat.getEnumList();
    }

    private String getResourceUrlBase() {
        return CkanUtil.getStructuralResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_ORGANISATION_SCHEME;
    }
}
