package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;

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

public class CodelistAvro2CkanPackageMapper extends CkanPackageMapper {

    private CodelistAvro2CkanPackageMapper() {
    }

    private static final String URL_BASE_CODELISTS = "codelists";
    private static final String URL_BASE_IDENTIFIER = "dataset";
    private static final String BASE_CODELIST_RESOURCE = "Clasificación. ";
    private static final String CODELIST_TAG = "Clasificación";

    public static CkanPackage toNewCkanPackage(CodelistAvro codelist, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService) {
        CkanPackage newPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(codelist.getUrn());
        String version = params[2];

        newPackage.setName(getCkanPackageName(codelist));
        newPackage.setTitle(getDefaultLocalisedStringFromLocalisedStringAvro(codelist.getName().getLocalisedStrings()));
        newPackage.setPrivates(false);
        String maintainer = getDefaultLocalisedStringFromLocalisedStringAvro(codelist.getMaintainer().getTitle().getLocalisedStrings());
        newPackage.setAuthor(maintainer);
        newPackage.setMaintainer(maintainer);
        newPackage.setLicense_id(metadataConfigurationService.retrieveCkanLicenceId());
        newPackage.setNotes(getCodelistDescription(codelist));

        newPackage.setVersion(version);

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(getDefaultOrganisation(metadataConfigurationService));
        newPackage.setResources(createResources(codelist, metadataConfigurationService, newPackage.getName()));
        newPackage.setTags(getTags());

        // empty metadata
        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());
        newPackage.setGroups(new ArrayList<>());

        // extra fields
        newPackage.setExtras(getCodelistExtraFields(metadataConfigurationService, externalCategoryService, newPackage.getName()));

        return newPackage;
    }

    public static String getCkanPackageName(CodelistAvro codelist) {
        return CkanFormatUtil.formatCkanNameFromDatasetUrn(codelist.getUrn());
    }

    public static CkanPackage toExistingCkanPackage(CodelistAvro codelist, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(codelist.getUrn());
        String version = params[2];

        existingPackage.setId(id);
        existingPackage.setNotes(getCodelistDescription(codelist));

        existingPackage.setVersion(version);

        // extra fields
        existingPackage.setExtras(getCodelistExtraFields(metadataConfigurationService, externalCategoryService, getCkanPackageName(codelist)));

        return existingPackage;
    }

    private static List<CkanTag> getTags() {
        List<CkanTag> tags = new ArrayList<>();

        tags.add(new CkanTag(CODELIST_TAG));

        return tags;

    }

    public static List<CkanResource> createResources(CodelistAvro codelist, MetadataConfigurationService metadataConfigurationService, String packageId) {

        String[] params = UrnUtils.splitUrnItem(codelist.getUrn());

        return createResources(getCodelistDescription(codelist), codelist.getUrn(), getResourceUrlBase(metadataConfigurationService), getNameResourceBase(params[0], params[1]), packageId,
                CodelistResourcesFormat.getEnumList());
    }

    public static List<CkanResource> updateResources(CodelistAvro codelist, MetadataConfigurationService metadataConfigurationService, String packageId, CkanPackage existingPackage) {
        return updateResources(codelist.getUrn(), getResourceUrlBase(metadataConfigurationService), packageId, existingPackage.getResources());
    }

    private static List<CkanExtra> getCodelistExtraFields(MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService, String packageName) {
        List<CkanExtra> extraFields = getExtraFields(URL_BASE_IDENTIFIER, metadataConfigurationService, packageName);

        extraFields.add(new CkanExtra("theme", getDefaultTheme(externalCategoryService, metadataConfigurationService, metadataConfigurationService.retrieveCkanEniUrlBase())));

        return extraFields;
    }

    private static String getCodelistDescription(CodelistAvro codelist) {
        if (codelist.getDescription() != null) {
            return getDescriptionFromLocalisedStringAvro(codelist.getDescription().getLocalisedStrings());
        }
        return null;
    }

    private static String getNameResourceBase(String agency, String code) {
        return BASE_CODELIST_RESOURCE + agency + ":" + code;
    }

    private static String getResourceUrlBase(MetadataConfigurationService metadataConfigurationService) {
        return CkanUtil.getStructuralResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_CODELISTS;
    }

}
