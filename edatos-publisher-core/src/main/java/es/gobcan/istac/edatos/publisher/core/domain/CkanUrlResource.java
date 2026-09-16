package es.gobcan.istac.edatos.publisher.core.domain;

import java.util.HashMap;
import java.util.Map;

public class CkanUrlResource {

    private String              urlBaseResource;
    private String              urlSuffixResource;
    private String              urlParametersFields;
    private Boolean             isJaxiDataset;
    private Map<String, String> jaxiUrlByFormat;

    private boolean             isExcelFormatVisible = true;
    private String              htmlUrl;

    public CkanUrlResource(String urlBaseResource, String urlSuffixResource, String urlParametersFields) {
        this.urlBaseResource = urlBaseResource;
        this.urlSuffixResource = urlSuffixResource;
        this.urlParametersFields = urlParametersFields;
        this.setIsJaxiDataset(false);
    }

    public CkanUrlResource(Boolean isJaxiDataset) {
        this.jaxiUrlByFormat = new HashMap<>();
        this.setIsJaxiDataset(isJaxiDataset);
    }

    public String getUrlBaseResource() {
        return urlBaseResource;
    }
    public void setUrlBaseResource(String urlBaseResource) {
        this.urlBaseResource = urlBaseResource;
    }
    public String getUrlSuffixResource() {
        return urlSuffixResource;
    }
    public void setUrlSuffixResource(String urlSuffixResource) {
        this.urlSuffixResource = urlSuffixResource;
    }
    public String getUrlParametersFields() {
        return urlParametersFields;
    }
    public void setUrlParametersFields(String urlParametersFields) {
        this.urlParametersFields = urlParametersFields;
    }

    public boolean isExcelFormatVisible() {
        return isExcelFormatVisible;
    }

    public void setExcelFormatVisible(boolean isExcelFormatVisible) {
        this.isExcelFormatVisible = isExcelFormatVisible;
    }

    public Boolean getIsJaxiDataset() {
        return isJaxiDataset;
    }

    public void setIsJaxiDataset(Boolean isJaxiDataset) {
        this.isJaxiDataset = isJaxiDataset;
    }

    public void addJaxiUrlByFormat(String resourceFormat, String url) {
        this.jaxiUrlByFormat.put(resourceFormat, url);

    }
    public Map<String, String> getJaxiUrlByFormat() {
        return jaxiUrlByFormat;
    }

    public void setJaxiUrlByFormat(Map<String, String> jaxiUrlByFormat) {
        this.jaxiUrlByFormat = jaxiUrlByFormat;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }
}
