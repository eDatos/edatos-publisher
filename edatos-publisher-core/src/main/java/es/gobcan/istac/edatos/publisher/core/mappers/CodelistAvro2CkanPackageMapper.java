package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
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

public class CodelistAvro2CkanPackageMapper extends CkanPackageMapper {

    private CodelistAvro                 codelist;
    private MetadataConfigurationService metadataConfigurationService;
    private MessageSource                messageSource;

    public CodelistAvro2CkanPackageMapper(CodelistAvro codelist, MetadataConfigurationService metadataConfigurationService, MessageSource messageSource) {
        this.codelist = codelist;
        this.metadataConfigurationService = metadataConfigurationService;
        this.messageSource = messageSource;
    }

    private static final String URL_BASE_CODELISTS  = "codelists";
    private static final String URL_BASE_IDENTIFIER = "dataset";

    public CkanPackage toNewCkanPackage(ExternalCategoryService externalCategoryService) {
        CkanPackage newPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(getUrn());
        String version = params[2];

        newPackage.setName(getCkanPackageName());
        newPackage.setTitle(getCodelistTitle(params[0], params[1]));
        newPackage.setPrivates(false);
        String maintainer = getDefaultLocalisedStringFromLocalisedStringAvro(codelist.getMaintainer().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
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
        newPackage.setExtras(getCodelistExtraFields(externalCategoryService, newPackage.getName()));

        newPackage.setGroups(getCkanGroups());

        return newPackage;
    }

    @Override
    public String getCkanPackageName() {
        String urn = CkanFormatUtil.formatCkanNameFromDatasetUrn(getUrn());
        return super.getCkanPackageName(urn);
    }

    public CkanPackage toExistingCkanPackage(ExternalCategoryService externalCategoryService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(getUrn());
        String version = params[2];

        existingPackage.setId(id);
        existingPackage.setTitle(getCodelistTitle(params[0], params[1]));
        existingPackage.setNotes(getResourceDescription());

        existingPackage.setVersion(version);

        existingPackage.setTags(getTags());
        // extra fields
        existingPackage.setExtras(getCodelistExtraFields(externalCategoryService, getCkanPackageName()));

        existingPackage.setGroups(getCkanGroups());

        return existingPackage;
    }

    private List<CkanTag> getTags() {
        List<CkanTag> tags = new ArrayList<>();

        tags.add(new CkanTag(InternationalStringUtil.generateTextI18n(messageSource, Constants.CODELIST_TAG, null)));

        return tags;

    }

    public List<CkanResource> createResources(String packageId) {

        return createResources(packageId, getAvailableFormats());
    }

    @Override
    protected CkanUrlResource getCkanUrlResource() {
        return new CkanUrlResource(getResourceUrlBase(getUrn(), getResourceUrlBase()), Constants.URL_RESOURCES_CODELIST_CODES, "fields=+description");
    }

    private List<CkanExtra> getCodelistExtraFields(ExternalCategoryService externalCategoryService, String packageName) {
        List<CkanExtra> extraFields = getExtraFields(URL_BASE_IDENTIFIER, metadataConfigurationService, packageName);

        extraFields.add(new CkanExtra("theme", getDefaultTheme(externalCategoryService, metadataConfigurationService, metadataConfigurationService.retrieveCkanEniUrlBase())));

        return extraFields;
    }

    @Override
    protected String getResourceDescription() {
        if (codelist.getDescription() != null) {
            return getDescriptionFromLocalisedStringAvro(codelist.getDescription().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
        }
        return null;
    }

    @Override
    protected String getNameResourceBase(String agency, String code) {
        return InternationalStringUtil.generateTextI18n(messageSource, Constants.BASE_CODELIST_RESOURCE, null) + agency + ":" + code;
    }

    @Override
    protected String getUrn() {
        return codelist.getUrn();
    }

    @Override
    protected List<String> getAvailableFormats() {
        return SrmResourcesFormat.getEnumList();
    }

    private String getResourceUrlBase() {
        return CkanUtil.getStructuralResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_CODELISTS;
    }

    private String getCodelistTitle(String agency, String resourceId) {
        return getCkanTitle(getDefaultLocalisedStringFromLocalisedStringAvro(codelist.getName().getLocalisedStrings(), Constants.CKAN_LANGUAGE), agency, resourceId);
    }

}
