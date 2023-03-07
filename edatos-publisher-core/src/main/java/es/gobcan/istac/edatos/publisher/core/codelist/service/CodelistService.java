package es.gobcan.istac.edatos.publisher.core.codelist.service;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.CodelistAvro;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;

public interface CodelistService {

    CkanPackage retrieveNewCkanPackage(CodelistAvro codelist);
    CkanPackage retrieveUpdateCkanPackage(CodelistAvro codelist, String id);
    String retrieveCkanPackageName(CodelistAvro codelist);
    List<CkanResource> retrieveUpdatedResources(CodelistAvro codelist, CkanPackage existingPackage, String packageId);
    List<CkanResource> retrieveNewResources(CodelistAvro codelist, String packageId);

}
