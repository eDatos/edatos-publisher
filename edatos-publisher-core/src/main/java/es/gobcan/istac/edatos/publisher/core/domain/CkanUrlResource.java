package es.gobcan.istac.edatos.publisher.core.domain;

public class CkanUrlResource {

    private String urlBaseResource;
    private String urlSuffixResource;
    private String urlParametersFields;

    public CkanUrlResource(String urlBaseResource, String urlSuffixResource, String urlParametersFields) {
        this.urlBaseResource = urlBaseResource;
        this.urlSuffixResource = urlSuffixResource;
        this.urlParametersFields = urlParametersFields;
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

}
