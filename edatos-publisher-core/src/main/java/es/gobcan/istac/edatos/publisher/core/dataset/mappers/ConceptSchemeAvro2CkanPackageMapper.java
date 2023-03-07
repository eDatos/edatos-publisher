package es.gobcan.istac.edatos.publisher.core.dataset.mappers;

import java.util.ArrayList;
import java.util.List;

import org.siemac.edatos.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanExtra;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanTag;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.CodelistResourcesFormat;
import es.gobcan.istac.edatos.publisher.core.domain.enumeration.DatasetStateEnum;
import es.gobcan.istac.edatos.publisher.core.service.ExternalCategoryService;
import es.gobcan.istac.edatos.publisher.core.service.ExternalOperationService;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.core.util.CkanFormatUtil;
import es.gobcan.istac.edatos.publisher.core.util.CkanUtil;

public class ConceptSchemeAvro2CkanPackageMapper extends CkanPackageMapper {

    private ConceptSchemeAvro2CkanPackageMapper() {
    }

    private static final String URL_BASE_CONCEPT_SCHEME = "conceptschemes";
    private static final String URL_BASE_IDENTIFIER = "dataset";
    private static final String BASE_CONCEPT_RESOURCE = "Esquema de conceptos. ";
    private static final String CONCEPT_TAG = "Concepto";

    public static CkanPackage toNewCkanPackage(ConceptSchemeAvro conceptScheme, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService,
            ExternalOperationService externalOperationService) {
        CkanPackage newPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(conceptScheme.getUrn());
        String version = params[2];

        newPackage.setName(getCkanPackageName(conceptScheme));
        newPackage.setTitle(getDefaultLocalisedStringFromLocalisedStringAvro(conceptScheme.getName().getLocalisedStrings()));
        newPackage.setPrivates(false);
        newPackage.setMaintainer(getDefaultLocalisedStringFromLocalisedStringAvro(conceptScheme.getMaintainer().getTitle().getLocalisedStrings()));
        newPackage.setLicense_id(metadataConfigurationService.retrieveCkanLicenceId());
        newPackage.setNotes(getConceptSchemeDescription(conceptScheme));

        newPackage.setVersion(version);

        newPackage.setState(DatasetStateEnum.ACTIVE.getName());
        newPackage.setOwner_org(conceptScheme.getMaintainer().getCode().toLowerCase());
        newPackage.setResources(createResources(conceptScheme, metadataConfigurationService, newPackage.getName()));
        newPackage.setTags(getTags());

        // empty metadata
        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());
        newPackage.setGroups(new ArrayList<>());

        // extra fields
        newPackage.setExtras(getCodelistExtraFields(metadataConfigurationService, externalCategoryService, externalOperationService, newPackage.getName()));

        return newPackage;
    }

    public static String getCkanPackageName(ConceptSchemeAvro conceptScheme) {
        return CkanFormatUtil.formatCkanNameFromDatasetUrn(conceptScheme.getUrn());
    }

    public static CkanPackage toExistingCkanPackage(ConceptSchemeAvro conceptScheme, MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService,
            ExternalOperationService externalOperationService, String id) {
        CkanPackage existingPackage = new CkanPackage();

        String[] params = UrnUtils.splitUrnItem(conceptScheme.getUrn());
        String version = params[2];

        existingPackage.setId(id);
        existingPackage.setNotes(getConceptSchemeDescription(conceptScheme));

        existingPackage.setVersion(version);

        // extra fields
        existingPackage.setExtras(getCodelistExtraFields(metadataConfigurationService, externalCategoryService, externalOperationService, getCkanPackageName(conceptScheme)));

        return existingPackage;
    }

    private static List<CkanTag> getTags() {
        List<CkanTag> tags = new ArrayList<>();

        tags.add(new CkanTag(CONCEPT_TAG));

        return tags;

    }

    public static List<CkanResource> createResources(ConceptSchemeAvro conceptScheme, MetadataConfigurationService metadataConfigurationService, String packageId) {

        String[] params = UrnUtils.splitUrnItem(conceptScheme.getUrn());

        return createResources(getConceptSchemeDescription(conceptScheme), conceptScheme.getUrn(), getResourceUrlBase(metadataConfigurationService), getNameResourceBase(params[0], params[1]),
                packageId, CodelistResourcesFormat.getEnumList());
    }

    public static List<CkanResource> updateResources(ConceptSchemeAvro conceptScheme, MetadataConfigurationService metadataConfigurationService, String packageId, CkanPackage existingPackage) {
        return updateResources(conceptScheme.getUrn(), getResourceUrlBase(metadataConfigurationService), packageId, existingPackage.getResources());
    }

    private static List<CkanExtra> getCodelistExtraFields(MetadataConfigurationService metadataConfigurationService, ExternalCategoryService externalCategoryService,
            ExternalOperationService externalOperationService, String packageName) {
        List<CkanExtra> extraFields = getExtraFields(URL_BASE_IDENTIFIER, metadataConfigurationService, packageName);
        String genericEniUrl = getUrlEniTheme(metadataConfigurationService.retrieveCkanEniUrlBase(), metadataConfigurationService.retrieveCkanGenericEniTheme());
        extraFields.add(new CkanExtra("theme", genericEniUrl));

        return extraFields;
    }

    private static String getConceptSchemeDescription(ConceptSchemeAvro conceptScheme) {
        if (conceptScheme.getDescription() != null) {
            return getDescriptionFromLocalisedStringAvro(conceptScheme.getDescription().getLocalisedStrings());
        }
        return null;
    }

    private static String getNameResourceBase(String agency, String code) {
        return BASE_CONCEPT_RESOURCE + agency + ":" + code;
    }

    private static String getResourceUrlBase(MetadataConfigurationService metadataConfigurationService) {
        return CkanUtil.getStructuralResourceUrlBase(metadataConfigurationService) + "/" + URL_BASE_CONCEPT_SCHEME;
    }

}
