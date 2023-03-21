package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeTypeEnumAvro;

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

public class OrganisationSchemeAvro2CkanPackageMapper extends CkanPackageMapper {

    private OrganisationSchemeAvro2CkanPackageMapper() {
    }

    private static final String URL_BASE_ORGANISATION_SCHEME = "organisationschemes";
    private static final String URL_BASE_IDENTIFIER = "dataset";
    private static final String BASE_ORGANISATION_RESOURCE = "Esquema de organizaciones. ";
    private static final String ORGANISATION_TAG = "Organización";
    private static final String ORGANISATION_UNIT_TAG = "Unidad organizativa";
    private static final String ORGANISATION_AGENCY_TAG = "Agencia";
    private static final String ORGANISATION_DATA_PROVIDER_TAG = "Proveedor de datos";
    private static final String ORGANISATION_DATA_CONSUMER_TAG = "Consumidor de datos";

    public static CkanPackage toNewCkanPackage(OrganisationSchemeAvro organisationScheme, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService) {
        CkanPackage newPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(organisationScheme.getUrn());
        String version = params[2];

        newPackage.setName(getCkanPackageName(organisationScheme));
        newPackage.setTitle(getDefaultLocalisedStringFromLocalisedStringAvro(organisationScheme.getName().getLocalisedStrings(), metadataConfigurationService.retrieveCkanDefaultLanguage()));
        newPackage.setPrivates(false);
        String maintainer = getMaintainer(organisationScheme, metadataConfigurationService);

        newPackage.setAuthor(maintainer);
        newPackage.setMaintainer(maintainer);
        newPackage.setLicense_id(metadataConfigurationService.retrieveCkanLicenceId());
        newPackage.setNotes(getOrganisationSchemeDescription(organisationScheme, metadataConfigurationService.retrieveCkanDefaultLanguage()));

        newPackage.setVersion(version);

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(getDefaultOrganisation(metadataConfigurationService));
        newPackage.setResources(createResources(organisationScheme, metadataConfigurationService, newPackage.getName()));
        newPackage.setTags(getTags(organisationScheme.getOrganisationSchemeType()));

        // empty metadata
        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());
        newPackage.setGroups(new ArrayList<>());

        // extra fields
        newPackage.setExtras(getOrganisationSchemeExtraFields(metadataConfigurationService, externalCategoryService, newPackage.getName()));

        return newPackage;
    }

    private static String getMaintainer(OrganisationSchemeAvro organisationScheme, MetadataConfigurationService metadataConfigurationService) {
        if (organisationScheme.getMaintainer() != null) {
            return getDefaultLocalisedStringFromLocalisedStringAvro(organisationScheme.getMaintainer().getTitle().getLocalisedStrings(), metadataConfigurationService.retrieveCkanDefaultLanguage());
        } else {
            return getDefaultOrganisation(metadataConfigurationService);
        }
    }

    public static String getCkanPackageName(OrganisationSchemeAvro organisationScheme) {
        return CkanFormatUtil.formatCkanNameFromDatasetUrn(organisationScheme.getUrn());
    }

    public static CkanPackage toExistingCkanPackage(OrganisationSchemeAvro organisationScheme, MetadataConfigurationService metadataConfigurationService,
            ExternalCategoryService externalCategoryService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(organisationScheme.getUrn());
        String version = params[2];

        existingPackage.setId(id);
        existingPackage.setNotes(getOrganisationSchemeDescription(organisationScheme, metadataConfigurationService.retrieveCkanDefaultLanguage()));

        existingPackage.setVersion(version);

        // extra fields
        existingPackage.setExtras(getOrganisationSchemeExtraFields(metadataConfigurationService, externalCategoryService, getCkanPackageName(organisationScheme)));

        return existingPackage;
    }

    private static List<CkanTag> getTags(OrganisationSchemeTypeEnumAvro organisationSchemeTypeEnumAvro) {
        List<CkanTag> tags = new ArrayList<>();

        tags.add(new CkanTag(ORGANISATION_TAG));

        switch (organisationSchemeTypeEnumAvro) {
            case ORGANISATION_UNIT_SCHEME:
                tags.add(new CkanTag(ORGANISATION_UNIT_TAG));
                break;
            case AGENCY_SCHEME:
                tags.add(new CkanTag(ORGANISATION_AGENCY_TAG));
                break;
            case DATA_PROVIDER_SCHEME:
                tags.add(new CkanTag(ORGANISATION_DATA_PROVIDER_TAG));
                break;
            case DATA_CONSUMER_SCHEME:
                tags.add(new CkanTag(ORGANISATION_DATA_CONSUMER_TAG));
                break;
        }

        return tags;

    }

    public static List<CkanResource> createResources(OrganisationSchemeAvro organisationScheme, MetadataConfigurationService metadataConfigurationService, String packageId) {

        String[] params = UrnUtils.splitUrnItem(organisationScheme.getUrn());

        return createResources(getOrganisationSchemeDescription(organisationScheme, metadataConfigurationService.retrieveCkanDefaultLanguage()),
                getCkanUrlResource(organisationScheme, metadataConfigurationService), getNameResourceBase(params[0], params[1]), packageId, SrmResourcesFormat.getEnumList());
    }

    public static List<CkanResource> updateResources(OrganisationSchemeAvro organisationScheme, MetadataConfigurationService metadataConfigurationService, String packageId,
            CkanPackage existingPackage) {
        return updateResources(getCkanUrlResource(organisationScheme, metadataConfigurationService), packageId, existingPackage.getResources());
    }

    private static CkanUrlResource getCkanUrlResource(OrganisationSchemeAvro organisationScheme, MetadataConfigurationService metadataConfigurationService) {
        return new CkanUrlResource(getResourceUrlBase(organisationScheme.getUrn(), getResourceUrlBase(metadataConfigurationService)), Constants.URL_RESOURCES_ORGANISATIONS, null);
    }

    private static List<CkanExtra> getOrganisationSchemeExtraFields(MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService, String packageName) {
        List<CkanExtra> extraFields = getExtraFields(URL_BASE_IDENTIFIER, metadataConfigurationService, packageName);

        extraFields.add(new CkanExtra("theme", getDefaultTheme(externalCategoryService, metadataConfigurationService, metadataConfigurationService.retrieveCkanEniUrlBase())));

        return extraFields;
    }

    private static String getOrganisationSchemeDescription(OrganisationSchemeAvro organisationScheme, String defaultLanguage) {
        if (organisationScheme.getDescription() != null) {
            return getDescriptionFromLocalisedStringAvro(organisationScheme.getDescription().getLocalisedStrings(), defaultLanguage);
        }
        return null;
    }

    private static String getNameResourceBase(String agency, String code) {
        return BASE_ORGANISATION_RESOURCE + agency + ":" + code;
    }

    private static String getResourceUrlBase(MetadataConfigurationService metadataConfigurationService) {
        return CkanUtil.getStructuralResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_ORGANISATION_SCHEME;
    }
}
