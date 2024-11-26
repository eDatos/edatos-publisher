package es.gobcan.istac.edatos.publisher.core.ckan.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CkanResource {

    private String              id;
    private String              package_id;

    private String              url;

    @JsonProperty("resource_identifier")
    private String              resourceIdentifier; // TODO EDATOS-4447 VER SI QUITAR
    @JsonProperty("name_translated")
    private Map<String, String> name;
    private Map<String, String> description;
    private String              format;
    private String              hash;

    private String              resource_type;
    private String              mimetype;
    private String              mimetype_inner;
    private String              cache_url;

    @JsonProperty("byte_size")
    private int                 size;
    private String              created;
    private String              last_modified;
    private String              cache_last_updated;

    public CkanResource() {
    }

    public CkanResource(String package_id, String url, Map<String, String> name) {
        this.package_id = package_id;
        this.url = url;
        this.name = name;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getMimetype_inner() {
        return mimetype_inner;
    }

    public void setMimetype_inner(String mimetype_inner) {
        this.mimetype_inner = mimetype_inner;
    }

    public String getCache_url() {
        return cache_url;
    }

    public void setCache_url(String cache_url) {
        this.cache_url = cache_url;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    public String getCache_last_updated() {
        return cache_last_updated;
    }

    public void setCache_last_updated(String cache_last_updated) {
        this.cache_last_updated = cache_last_updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public String getResourceIdentifier() {
        return resourceIdentifier;
    }

    public void setResourceIdentifier(String resourceIdentifier) {
        this.resourceIdentifier = resourceIdentifier;
    }
}
