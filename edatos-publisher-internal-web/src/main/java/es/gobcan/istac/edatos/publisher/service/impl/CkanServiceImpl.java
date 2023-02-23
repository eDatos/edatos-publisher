package es.gobcan.istac.edatos.publisher.service.impl;

import static es.gobcan.istac.edatos.publisher.core.util.CkanUtil.buildPackageCreateUrl;
import static es.gobcan.istac.edatos.publisher.core.util.CkanUtil.buildPackagePatchUrl;
import static es.gobcan.istac.edatos.publisher.core.util.CkanUtil.buildPackageSearchByUrnUrl;
import static es.gobcan.istac.edatos.publisher.core.util.CkanUtil.buildResourceCreateUrl;
import static es.gobcan.istac.edatos.publisher.core.util.CkanUtil.getBaseHttpHeader;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanGenericResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackage;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanPackageResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResource;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanResourceResponse;
import es.gobcan.istac.edatos.publisher.core.ckan.dto.CkanSearchResponse;
import es.gobcan.istac.edatos.publisher.core.service.MetadataConfigurationService;
import es.gobcan.istac.edatos.publisher.service.CkanService;

@Service
public class CkanServiceImpl implements CkanService {

    private final MetadataConfigurationService metadataConfigurationService;

    RestTemplate restTemplate;
    String urlBase;
    String apiKey;

    public CkanServiceImpl(MetadataConfigurationService metadataConfigurationService) {
        this.metadataConfigurationService = metadataConfigurationService;
        restTemplate = new RestTemplate();
        urlBase = this.metadataConfigurationService.retrieveCkanUrlBase();
        apiKey = this.metadataConfigurationService.retrieveCkanApiKey();
    }

    @Override
    public CkanSearchResponse getDatasetByUrn(String urn) {
        String packageSearchUrl = buildPackageSearchByUrnUrl(urlBase, urn);

        HttpHeaders httpHeaders = getBaseHttpHeader(apiKey);
        ResponseEntity<CkanSearchResponse> response = restTemplate.getForEntity(packageSearchUrl, CkanSearchResponse.class, httpHeaders);

        return response.getBody();
    }

    @Override
    public CkanPackageResponse createDataset(CkanPackage dataset) {
        String packageCreateUrl = buildPackageCreateUrl(urlBase);
        HttpEntity<CkanPackage> request = new HttpEntity<>(dataset, getBaseHttpHeader(apiKey));
        ResponseEntity<CkanPackageResponse> response = restTemplate.postForEntity(packageCreateUrl, request, CkanPackageResponse.class);

        return response.getBody();
    }

    @Override
    public CkanResourceResponse createResource(CkanResource ckanResource) {

        String resourceCreateUrl = buildResourceCreateUrl(urlBase);
        HttpEntity<CkanResource> request = new HttpEntity<>(ckanResource, getBaseHttpHeader(apiKey));

        ResponseEntity<CkanResourceResponse> response = restTemplate.postForEntity(resourceCreateUrl, request, CkanResourceResponse.class);

        return response.getBody();
    }

    @Override
    public CkanGenericResponse updateDataset(String datasetId, CkanPackage dataset) {

        String packagePatchUrl = buildPackagePatchUrl(urlBase, datasetId);
        HttpEntity<CkanPackage> request = new HttpEntity<>(dataset, getBaseHttpHeader(apiKey));
        ResponseEntity<CkanGenericResponse> response = restTemplate.postForEntity(packagePatchUrl, request, CkanGenericResponse.class);

        return response.getBody();
    }

}
