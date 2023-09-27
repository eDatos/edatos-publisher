package es.gobcan.istac.edatos.publisher.service.impl;

import static es.gobcan.istac.edatos.publisher.core.util.CkanUtil.buildPackageCreateUrl;
import static es.gobcan.istac.edatos.publisher.core.util.CkanUtil.buildPackagePatchResourceUrl;
import static es.gobcan.istac.edatos.publisher.core.util.CkanUtil.buildPackagePatchUrl;
import static es.gobcan.istac.edatos.publisher.core.util.CkanUtil.buildPackageSearchByIdUrl;
import static es.gobcan.istac.edatos.publisher.core.util.CkanUtil.buildPackageSearchByNameUrl;
import static es.gobcan.istac.edatos.publisher.core.util.CkanUtil.buildResourceCreateUrl;
import static es.gobcan.istac.edatos.publisher.core.util.CkanUtil.buildResourceDeleteUrl;
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

    HttpHeaders                                httpHeaders;
    RestTemplate                               restTemplate;
    String                                     urlBase;
    String                                     apiKey;

    public CkanServiceImpl(MetadataConfigurationService metadataConfigurationService) {
        this.metadataConfigurationService = metadataConfigurationService;
        restTemplate = new RestTemplate();
        httpHeaders = getBaseHttpHeader(apiKey);
        urlBase = this.metadataConfigurationService.retrieveCkanUrlBase();
        apiKey = this.metadataConfigurationService.retrieveCkanApiKey();
    }

    @Override
    public CkanSearchResponse getDatasetByName(String name) {
        String packageSearchUrl = buildPackageSearchByNameUrl(urlBase, name);

        ResponseEntity<CkanSearchResponse> response = restTemplate.getForEntity(packageSearchUrl, CkanSearchResponse.class, httpHeaders);

        return response.getBody();
    }

    @Override
    public CkanSearchResponse getDatasetById(String id) {
        String packageSearchUrl = buildPackageSearchByIdUrl(urlBase, id);

        ResponseEntity<CkanSearchResponse> response = restTemplate.getForEntity(packageSearchUrl, CkanSearchResponse.class, httpHeaders);

        return response.getBody();
    }

    @Override
    public CkanPackageResponse createPackage(CkanPackage ckanPackage) {
        String packageCreateUrl = buildPackageCreateUrl(urlBase);
        HttpEntity<CkanPackage> request = new HttpEntity<>(ckanPackage, getBaseHttpHeader(apiKey));
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
    public CkanPackageResponse updatePackage(CkanPackage ckanPackage) {

        String packagePatchUrl = buildPackagePatchUrl(urlBase);
        HttpEntity<CkanPackage> request = new HttpEntity<>(ckanPackage, getBaseHttpHeader(apiKey));
        ResponseEntity<CkanPackageResponse> response = restTemplate.postForEntity(packagePatchUrl, request, CkanPackageResponse.class);

        return response.getBody();
    }

    @Override
    public CkanResourceResponse updateResource(CkanResource resource) {

        String resourcePatchUrl = buildPackagePatchResourceUrl(urlBase);
        HttpEntity<CkanResource> request = new HttpEntity<>(resource, getBaseHttpHeader(apiKey));
        ResponseEntity<CkanResourceResponse> response = restTemplate.postForEntity(resourcePatchUrl, request, CkanResourceResponse.class);

        return response.getBody();
    }

    @Override
    public CkanGenericResponse deleteResource(String resourceId) {

        String resourceDeleteUrl = buildResourceDeleteUrl(urlBase);

        CkanResource resource = new CkanResource();
        resource.setId(resourceId);

        HttpEntity<CkanResource> request = new HttpEntity<>(resource, getBaseHttpHeader(apiKey));

        ResponseEntity<CkanGenericResponse> response = restTemplate.postForEntity(resourceDeleteUrl, request, CkanGenericResponse.class);

        return response.getBody();
    }

}
