package es.gobcan.istac.edatos.publisher.core.mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
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

        newPackage.setName(getCkanPackageName());

        toCkanPackage(externalCategoryService, newPackage);

        newPackage.setPrivates(false);
        String maintainer = InternationalStringSrmUtil.getDefaultLocalisedStringFromLocalisedStringAvro(codelist.getMaintainer().getTitle().getLocalisedStrings(), Constants.CKAN_LANGUAGE);
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

    @Override
    public String getCkanPackageName() {
        String urn = CkanFormatUtil.formatCkanNameFromDatasetUrn(getUrn());
        return super.getCkanPackageName(urn);
    }

    public CkanPackage toExistingCkanPackage(ExternalCategoryService externalCategoryService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        existingPackage.setId(id);

        toCkanPackage(externalCategoryService, existingPackage);

        return existingPackage;
    }

    public void toCkanPackage(ExternalCategoryService externalCategoryService, CkanPackage ckanPackage) {
        String[] params = UrnUtils.splitUrnItem(getUrn());
        String version = params[2];

        String name = getCkanPackageName();

        ckanPackage.setTitle(getCodelistTitle(params[0], params[1]));
        ckanPackage.setDescription(getResourceDescription());
        ckanPackage.setUrl(codelist.getVisualizerHtmlLink());
        ckanPackage.setVersion(version);

        ckanPackage.setTags(getTags());
        // extra fields
        ckanPackage.setExtras(getCodelistExtraFields(externalCategoryService, name));

        ckanPackage.setTheme(getTheme(externalCategoryService));

        setCommonMetadata(URL_BASE_IDENTIFIER, ckanPackage, metadataConfigurationService, name);

        ckanPackage.setGroups(getCkanGroups());
    }

    private Map<String, List<String>> getTags() {
        return InternationalStringSrmUtil.generateCkanInternationalStringMessage(messageSource, Arrays.asList(Constants.CODELIST_TAG), null,
                metadataConfigurationService.retrieveInternationalizationLanguages());
    }

    public List<CkanResource> createResources(String packageId) {

        return createResources(packageId, getAvailableFormats());
    }

    @Override
    protected CkanUrlResource getCkanUrlResource() {
        CkanUrlResource ckanUrlResource = new CkanUrlResource(getResourceUrlBase(getUrn(), getResourceUrlBase()), Constants.URL_RESOURCES_CODELIST_CODES, "fields=+description");
        ckanUrlResource.setHtmlUrl(codelist.getVisualizerHtmlLink());
        return ckanUrlResource;
    }

    private List<CkanExtra> getCodelistExtraFields(ExternalCategoryService externalCategoryService, String packageName) {
        List<CkanExtra> extraFields = getExtraFields(URL_BASE_IDENTIFIER, metadataConfigurationService, packageName);
        return extraFields;
    }

    private List<String> getTheme(ExternalCategoryService externalCategoryService) {
        return getDefaultTheme(externalCategoryService, metadataConfigurationService, metadataConfigurationService.retrieveCkanEniUrlBase());
    }

    @Override
    protected Map<String, String> getResourceDescription() {
        if (codelist.getDescription() != null) {
            return InternationalStringSrmUtil.getSrmInternationalStringToCkanInternationalStringMarkDown(codelist.getDescription(), null);
        }
        return null;
    }

    @Override
    protected Map<String, String> getNameResourceBase(String agency, String code) {
        return InternationalStringSrmUtil.generateCkanInternationalStringMessage(messageSource, Constants.BASE_CODELIST_RESOURCE, null,
                metadataConfigurationService.retrieveInternationalizationLanguages(), agency + ":" + code);
    }

    @Override
    protected String getUrn() {
        return codelist.getUrn();
    }

    @Override
    protected List<String> getAvailableFormats() {
        return filterHtmlFormat(CkanResourcesFormatOperation.getSrmResourcesFormat(), codelist.getVisualizerHtmlLink());
    }

    private String getResourceUrlBase() {
        return CkanUtil.getStructuralResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_CODELISTS;
    }

    private Map<String, String> getCodelistTitle(String agency, String resourceId) {
        return InternationalStringSrmUtil.getSrmInternationalStringToCkanInternationalString(codelist.getName(), getCkanEndTitle(agency, resourceId));
    }

}
