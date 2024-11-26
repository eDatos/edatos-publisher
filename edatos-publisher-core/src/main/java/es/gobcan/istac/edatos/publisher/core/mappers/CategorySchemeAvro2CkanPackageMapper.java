package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
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

public class CategorySchemeAvro2CkanPackageMapper extends CkanPackageMapper {

    private CategorySchemeAvro           categoryScheme;
    private MetadataConfigurationService metadataConfigurationService;
    private MessageSource                messageSource;

    public CategorySchemeAvro2CkanPackageMapper(CategorySchemeAvro categoryScheme, MetadataConfigurationService metadataConfigurationService, MessageSource messageSource) {
        this.categoryScheme = categoryScheme;
        this.metadataConfigurationService = metadataConfigurationService;
        this.messageSource = messageSource;
    }

    private static final String URL_BASE_CATEGORY_SCHEME = "categoryschemes";
    private static final String URL_BASE_IDENTIFIER      = "dataset";

    public CkanPackage toNewCkanPackage(ExternalCategoryService externalCategoryService) {
        CkanPackage newPackage = new CkanPackage();

        newPackage.setName(getCkanPackageName());
        toCkanPackage(externalCategoryService, newPackage);

        newPackage.setPrivates(false);
        String maintainer = InternationalStringSrmUtil.getDefaultLocalisedStringFromLocalisedStringAvro(categoryScheme.getMaintainer().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
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

        ckanPackage.setTitle(getCategorySchemeTitle(params[0], params[1]));
        ckanPackage.setDescription(getResourceDescription());
        ckanPackage.setVersion(version);

        ckanPackage.setTags(getTags());
        // extra fields
        ckanPackage.setExtras(getCategorySchemeExtraFields(externalCategoryService, getCkanPackageName()));

        ckanPackage.setTheme(getTheme(externalCategoryService));

        setCommonMetadata(URL_BASE_IDENTIFIER, ckanPackage, metadataConfigurationService, ckanPackage.getName());

        ckanPackage.setGroups(getCkanGroups());
    }

    public CkanPackage toExistingCkanPackage(ExternalCategoryService externalCategoryService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        existingPackage.setId(id);

        toCkanPackage(externalCategoryService, existingPackage);

        return existingPackage;
    }

    public List<CkanResource> createResources(String packageId) {

        return createResources(packageId, getAvailableFormats());
    }

    @Override
    public String getCkanPackageName() {
        String urn = CkanFormatUtil.formatCkanNameFromDatasetUrn(getUrn());
        return super.getCkanPackageName(urn);
    }

    private Map<String, List<String>> getTags() {
        return InternationalStringSrmUtil.generateCkanInternationalStringMessage(messageSource, Arrays.asList(Constants.CATEGORY_TAG), null,
                metadataConfigurationService.retrieveInternationalizationLanguages());
    }

    @Override
    protected CkanUrlResource getCkanUrlResource() {
        return new CkanUrlResource(getResourceUrlBase(getUrn(), getResourceUrlBase()), Constants.URL_RESOURCES_CATEGORIES, null);
    }

    private List<CkanExtra> getCategorySchemeExtraFields(ExternalCategoryService externalCategoryService, String packageName) {
        return getExtraFields(URL_BASE_IDENTIFIER, metadataConfigurationService, packageName);

    }

    private List<String> getTheme(ExternalCategoryService externalCategoryService) {
        return getDefaultTheme(externalCategoryService, metadataConfigurationService, metadataConfigurationService.retrieveCkanEniUrlBase());
    }

    @Override
    protected Map<String, String> getResourceDescription() {
        if (categoryScheme.getDescription() != null) {
            return InternationalStringSrmUtil.getSrmInternationalStringToCkanInternationalStringMarkDown(categoryScheme.getDescription(), null);
        }
        return null;
    }

    @Override
    protected Map<String, String> getNameResourceBase(String agency, String code) {
        return InternationalStringSrmUtil.generateCkanInternationalStringMessage(messageSource, Constants.BASE_CATEGORY_RESOURCE, null,
                metadataConfigurationService.retrieveInternationalizationLanguages(), agency + ":" + code);
    }

    @Override
    protected String getUrn() {
        return categoryScheme.getUrn();
    }

    @Override
    protected List<String> getAvailableFormats() {
        return CkanResourcesFormatOperation.getSrmResourcesFormat();
    }

    private String getResourceUrlBase() {
        return CkanUtil.getStructuralResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_CATEGORY_SCHEME;
    }

    private Map<String, String> getCategorySchemeTitle(String agency, String resourceId) {
        return InternationalStringSrmUtil.getSrmInternationalStringToCkanInternationalString(categoryScheme.getName(), getCkanEndTitle(agency, resourceId));

    }
}
