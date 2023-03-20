package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanTag;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.CodelistResourcesFormat;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetStateEnum;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.core.util.CkanUtil;

public class CategorySchemeAvro2CkanPackageMapper extends CkanPackageMapper {

    private CategorySchemeAvro2CkanPackageMapper() {
    }

    private static final String URL_BASE_CATEGORY_SCHEME = "categoryschemes";
    private static final String URL_BASE_IDENTIFIER = "dataset";
    private static final String BASE_CATEGORY_RESOURCE = "Temas. ";
    private static final String CATEGORY_TAG = "Tema";

    public static CkanPackage toNewCkanPackage(CategorySchemeAvro categoryScheme, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService) {
        CkanPackage newPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(categoryScheme.getUrn());
        String version = params[2];

        newPackage.setName(getCkanPackageName(categoryScheme));
        newPackage.setTitle(getDefaultLocalisedStringFromLocalisedStringAvro(categoryScheme.getName().getLocalisedStrings(), metadataConfigurationService.retrieveCkanDefaultLanguage()));
        newPackage.setPrivates(false);
        String maintainer = getDefaultLocalisedStringFromLocalisedStringAvro(categoryScheme.getMaintainer().getTitle().getLocalisedStrings(),
                metadataConfigurationService.retrieveCkanDefaultLanguage());
        newPackage.setAuthor(maintainer);
        newPackage.setMaintainer(maintainer);
        newPackage.setLicense_id(metadataConfigurationService.retrieveCkanLicenceId());
        newPackage.setNotes(getConceptSchemeDescription(categoryScheme, metadataConfigurationService.retrieveCkanDefaultLanguage()));

        newPackage.setVersion(version);

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(getDefaultOrganisation(metadataConfigurationService));
        newPackage.setResources(createResources(categoryScheme, metadataConfigurationService, newPackage.getName()));
        newPackage.setTags(getTags());

        // empty metadata
        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());
        newPackage.setGroups(new ArrayList<>());

        // extra fields
        newPackage.setExtras(getCategorySchemeExtraFields(metadataConfigurationService, externalCategoryService, newPackage.getName()));

        return newPackage;
    }

    public static String getCkanPackageName(CategorySchemeAvro categoryScheme) {
        return CkanFormatUtil.formatCkanNameFromDatasetUrn(categoryScheme.getUrn());
    }

    public static CkanPackage toExistingCkanPackage(CategorySchemeAvro categoryScheme, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService,
            String id) {
        CkanPackage existingPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(categoryScheme.getUrn());
        String version = params[2];

        existingPackage.setId(id);
        existingPackage.setNotes(getConceptSchemeDescription(categoryScheme, metadataConfigurationService.retrieveCkanDefaultLanguage()));

        existingPackage.setVersion(version);

        // extra fields
        existingPackage.setExtras(getCategorySchemeExtraFields(metadataConfigurationService, externalCategoryService, getCkanPackageName(categoryScheme)));

        return existingPackage;
    }

    private static List<CkanTag> getTags() {
        List<CkanTag> tags = new ArrayList<>();

        tags.add(new CkanTag(CATEGORY_TAG));

        return tags;

    }

    public static List<CkanResource> createResources(CategorySchemeAvro categoryScheme, MetadataConfigurationService metadataConfigurationService, String packageId) {

        String[] params = UrnUtils.splitUrnItem(categoryScheme.getUrn());

        return createResources(getConceptSchemeDescription(categoryScheme, metadataConfigurationService.retrieveCkanDefaultLanguage()), categoryScheme.getUrn(),
                getResourceUrlBase(metadataConfigurationService), getNameResourceBase(params[0], params[1]), packageId, CodelistResourcesFormat.getEnumList());
    }

    public static List<CkanResource> updateResources(CategorySchemeAvro categoryScheme, MetadataConfigurationService metadataConfigurationService, String packageId, CkanPackage existingPackage) {
        return updateResources(categoryScheme.getUrn(), getResourceUrlBase(metadataConfigurationService), packageId, existingPackage.getResources());
    }

    private static List<CkanExtra> getCategorySchemeExtraFields(MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService, String packageName) {
        List<CkanExtra> extraFields = getExtraFields(URL_BASE_IDENTIFIER, metadataConfigurationService, packageName);

        extraFields.add(new CkanExtra("theme", getDefaultTheme(externalCategoryService, metadataConfigurationService, metadataConfigurationService.retrieveCkanEniUrlBase())));

        return extraFields;
    }

    private static String getConceptSchemeDescription(CategorySchemeAvro categoryScheme, String defaultLanguage) {
        if (categoryScheme.getDescription() != null) {
            return getDescriptionFromLocalisedStringAvro(categoryScheme.getDescription().getLocalisedStrings(), defaultLanguage);
        }
        return null;
    }

    private static String getNameResourceBase(String agency, String code) {
        return BASE_CATEGORY_RESOURCE + agency + ":" + code;
    }

    private static String getResourceUrlBase(MetadataConfigurationService metadataConfigurationService) {
        return CkanUtil.getStructuralResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_CATEGORY_SCHEME;
    }

}
