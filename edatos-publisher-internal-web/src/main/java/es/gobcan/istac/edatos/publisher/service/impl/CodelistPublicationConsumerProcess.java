package es.gobcan.istac.edatos.publisher.service.impl;

import java.util.List;

import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.ckan.service.impl.CkanSenderService;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.codelist.service.CodelistPublisherService;
import es.gobcan.istac.edatos.publisher.service.ConsumerProcess;

@Component
public class CodelistPublicationConsumerProcess implements ConsumerProcess<CodelistAvro> {

    @Autowired
    CkanSenderService ckanSenderService;
    @Autowired
    CodelistPublisherService codelistService;

    @Override
    public boolean processPublicationEvent(CodelistAvro codelist) {

        String urn = codelist.getUrn();
        CkanPackage ckanPackage = null;
        String packageName = codelistService.retrieveCkanPackageName(codelist);
        CkanPackage actualPackage = ckanSenderService.getCkanPackageByName(urn, packageName);

        if (actualPackage == null) {
            return false;
        }

        if (actualPackage.getId() != null) {
            ckanPackage = codelistService.retrieveUpdateCkanPackage(codelist, actualPackage.getId());
        } else {
            ckanPackage = codelistService.retrieveNewCkanPackage(codelist);
        }

        if (ckanPackage != null) {
            Boolean resultado = ckanSenderService.createOrUpdateCkanPackage(ckanPackage, urn, actualPackage.getId());
            if (Boolean.TRUE.equals(resultado)) {
                if (actualPackage.getId() != null) {
                    List<CkanResource> resources = codelistService.retrieveUpdatedResources(codelist, actualPackage, packageName);
                    ckanSenderService.updateResources(resources, urn);
                }
                return resultado;
            }

        }
        return false;
    }
}
