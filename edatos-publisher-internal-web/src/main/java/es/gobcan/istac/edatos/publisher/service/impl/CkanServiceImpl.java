package es.gobcan.istac.edatos.publisher.service.impl;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.*;
import es.gobcan.istac.edatos.publisher.service.CkanService;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static es.gobcan.istac.edatos.publisher.core.util.CkanUtil.*;

@Service
public class CkanServiceImpl implements CkanService {

    RestTemplate restTemplate;

    public CkanServiceImpl() {
        restTemplate = new RestTemplate();
    }

    @Override
    public CkanSearchResponse getDatasetByUrn(String urn) {
        String packageSearchUrl = buildPackageSearchByUrnUrl(urn);

        HttpHeaders httpHeaders = getBaseHttpHeader(restTemplate, packageSearchUrl);
        ResponseEntity<CkanSearchResponse> response = restTemplate.getForEntity(packageSearchUrl, CkanSearchResponse.class, httpHeaders);

        return response.getBody();
    }

    @Override
    public CkanPackageResponse createDataset(CkanPackage dataset) {
        String packageCreateUrl = buildPackageCreateUrl();
        HttpEntity<CkanPackage> request = new HttpEntity<>(dataset, getBaseHttpHeader(restTemplate, packageCreateUrl));
        ResponseEntity<CkanPackageResponse> response = restTemplate.postForEntity(packageCreateUrl, request, CkanPackageResponse.class);

        //createResource(response.getBody().getResult().getId(), "https://www.stats.govt.nz/assets/Uploads/Annual-enterprise-survey/Annual-enterprise-survey-2020-financial-year-provisional/Download-data/annual-enterprise-survey-2020-financial-year-provisional-csv.csv"); // TODO EDATOS-3460

        return response.getBody();
    }

    @Override
    public CkanResourceResponse createResource(String package_id, String url) {
        CkanResource resource = new CkanResource(package_id, url, "Nombre del recursos porque sí"); // TODO EDATOS-3460

        String resourceCreateUrl = buildResourceCreateUrl();
        HttpEntity<CkanResource> request = new HttpEntity<>(resource, getBaseHttpHeader(restTemplate, resourceCreateUrl));

        ResponseEntity<CkanResourceResponse> response = restTemplate.postForEntity(resourceCreateUrl, request, CkanResourceResponse.class);

        return response.getBody();
    }

    @Override
    public CkanGenericResponse updateDataset(String id, CategorySchemeAvro category) {
        createResource(id, "https://www.stats.govt.nz/assets/Uploads/Annual-enterprise-survey/Annual-enterprise-survey-2020-financial-year-provisional/Download-data/annual-enterprise-survey-2020-financial-year-provisional-csv.csv"); // TODO EDATOS-3460

        CkanPackage newPackage = new CkanPackage();
        newPackage.setResources(new ArrayList<>());

        String packagePatchUrl = buildPackagePatchUrl(id);
        HttpEntity<CkanPackage> request = new HttpEntity<>(newPackage, getBaseHttpHeader(restTemplate, packagePatchUrl));
        ResponseEntity<CkanGenericResponse> response = restTemplate.postForEntity(packagePatchUrl, request, CkanGenericResponse.class);

        return response.getBody();
    }
}
