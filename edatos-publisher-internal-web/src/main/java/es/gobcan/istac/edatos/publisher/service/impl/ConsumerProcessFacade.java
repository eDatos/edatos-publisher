package es.gobcan.istac.edatos.publisher.service.impl;

import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeAvro;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.siemac.metamac.statistical.resources.core.stream.messages.DatasetVersionAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.edatos.publisher.service.ConsumerProcess;
import es.ibestat.jaxi.stream.messages.DatasetAvro;

@Component
public class ConsumerProcessFacade {

    @Autowired
    ConsumerProcess<DatasetVersionAvro>     datasetConsumerProcess;
    @Autowired
    ConsumerProcess<DatasetAvro>            jaxiDatasetConsumerProcess;
    @Autowired
    ConsumerProcess<CodelistAvro>           codelistConsumerProcess;
    @Autowired
    ConsumerProcess<OrganisationSchemeAvro> organisationSchemeConsumerProcess;
    @Autowired
    ConsumerProcess<ConceptSchemeAvro>      conceptSchemeConsumerProcess;
    @Autowired
    ConsumerProcess<CategorySchemeAvro>     categorySchemeConsumerProcess;

    public PublicationConsumerProcessResult processDatasetPublicationMessage(DatasetVersionAvro datasetVersion) {
        return datasetConsumerProcess.processPublicationEvent(datasetVersion);
    }

    public PublicationConsumerProcessResult processJaxiDatasetPublicationMessage(DatasetAvro jaxiDataset) {
        return jaxiDatasetConsumerProcess.processPublicationEvent(jaxiDataset);
    }

    public PublicationConsumerProcessResult processCodelistPublicationMessage(CodelistAvro codelist) {
        return codelistConsumerProcess.processPublicationEvent(codelist);
    }

    public PublicationConsumerProcessResult processConceptSchemePublicationMessage(ConceptSchemeAvro conceptScheme) {
        return conceptSchemeConsumerProcess.processPublicationEvent(conceptScheme);
    }

    public PublicationConsumerProcessResult processOrganisationSchemePublicationMessage(OrganisationSchemeAvro organisationScheme) {
        return organisationSchemeConsumerProcess.processPublicationEvent(organisationScheme);
    }

    public PublicationConsumerProcessResult processCategorySchemePublicationMessage(CategorySchemeAvro categoryScheme) {
        return categorySchemeConsumerProcess.processPublicationEvent(categoryScheme);
    }

}
