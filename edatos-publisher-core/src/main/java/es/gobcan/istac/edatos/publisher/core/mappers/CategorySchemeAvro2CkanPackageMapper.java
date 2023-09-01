package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
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

        String[] params = UrnUtils.splitUrnItem(getUrn());
        String version = params[2];

        newPackage.setName(getCkanPackageName());
        newPackage.setTitle(getDefaultLocalisedStringFromLocalisedStringAvro(categoryScheme.getName().getLocalisedStrings(), Constants.CKAN_LANGUAGE));
        newPackage.setPrivates(false);
        String maintainer = getDefaultLocalisedStringFromLocalisedStringAvro(categoryScheme.getMaintainer().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
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
        newPackage.setExtras(getCategorySchemeExtraFields(externalCategoryService, newPackage.getName()));

        newPackage.setGroups(getCkanGroups());

        return newPackage;
    }

    public List<CkanResource> createResources(String packageId) {

        return createResources(packageId, getAvailableFormats());
    }

    public String getCkanPackageName() {
        return CkanFormatUtil.formatCkanNameFromDatasetUrn(getUrn());
    }

    public CkanPackage toExistingCkanPackage(ExternalCategoryService externalCategoryService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(getUrn());
        String version = params[2];

        existingPackage.setId(id);
        existingPackage.setTitle(getDefaultLocalisedStringFromLocalisedStringAvro(categoryScheme.getName().getLocalisedStrings(), Constants.CKAN_LANGUAGE));
        existingPackage.setNotes(getResourceDescription());

        existingPackage.setVersion(version);

        // extra fields
        existingPackage.setExtras(getCategorySchemeExtraFields(externalCategoryService, getCkanPackageName()));

        existingPackage.setGroups(getCkanGroups());

        return existingPackage;
    }

    private List<CkanTag> getTags() {
        List<CkanTag> tags = new ArrayList<>();

        tags.add(new CkanTag(InternationalStringUtil.generateTextI18n(messageSource, Constants.CATEGORY_TAG, null)));

        return tags;

    }

    @Override
    protected CkanUrlResource getCkanUrlResource() {
        return new CkanUrlResource(getResourceUrlBase(getUrn(), getResourceUrlBase()), Constants.URL_RESOURCES_CATEGORIES, null);
    }

    private List<CkanExtra> getCategorySchemeExtraFields(ExternalCategoryService externalCategoryService, String packageName) {
        List<CkanExtra> extraFields = getExtraFields(URL_BASE_IDENTIFIER, metadataConfigurationService, packageName);

        extraFields.add(new CkanExtra("theme", getDefaultTheme(externalCategoryService, metadataConfigurationService, metadataConfigurationService.retrieveCkanEniUrlBase())));

        return extraFields;
    }

    @Override
    protected String getResourceDescription() {
        if (categoryScheme.getDescription() != null) {
            return getDescriptionFromLocalisedStringAvro(categoryScheme.getDescription().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
        }
        return null;
    }

    @Override
    protected String getNameResourceBase(String agency, String code) {
        return InternationalStringUtil.generateTextI18n(messageSource, Constants.BASE_CATEGORY_RESOURCE, null) + agency + ":" + code;
    }

    @Override
    protected String getUrn() {
        return categoryScheme.getUrn();
    }

    @Override
    protected List<String> getAvailableFormats() {
        return SrmResourcesFormat.getEnumList();
    }

    private String getResourceUrlBase() {
        return CkanUtil.getStructuralResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_CATEGORY_SCHEME;
    }

}
