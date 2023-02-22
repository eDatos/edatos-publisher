package es.gobcan.istac.edatos.publisher.core.ckan.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CkanPackage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String title;

    @JsonProperty("private")
    private boolean privates; // private
    private String author;
    private String author_email;
    private String maintainer;
    private String maintainer_email;
    private String license_id;
    private String notes;
    private String url;
    private String version;
    private String state;
    private String type;
    private List<CkanResource> resources;
    private List<CkanTag> tags;
    private List<CkanExtra> extras;
    private List<CkanRelationship> relationships_as_object;
    private List<CkanRelationship> relationships_as_subject;
    private List<CkanGroup> groups;
    private String owner_org;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPrivates() {
        return privates;
    }

    public void setPrivates(boolean privates) {
        this.privates = privates;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor_email() {
        return author_email;
    }

    public void setAuthor_email(String author_email) {
        this.author_email = author_email;
    }

    public String getMaintainer() {
        return maintainer;
    }

    public void setMaintainer(String maintainer) {
        this.maintainer = maintainer;
    }

    public String getMaintainer_email() {
        return maintainer_email;
    }

    public void setMaintainer_email(String maintainer_email) {
        this.maintainer_email = maintainer_email;
    }

    public String getLicense_id() {
        return license_id;
    }

    public void setLicense_id(String license_id) {
        this.license_id = license_id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CkanResource> getResources() {
        return resources;
    }

    public void setResources(List<CkanResource> resources) {
        this.resources = resources;
    }

    public List<CkanTag> getTags() {
        return tags;
    }

    public void setTags(List<CkanTag> tags) {
        this.tags = tags;
    }

    public List<CkanExtra> getExtras() {
        return extras;
    }

    public void setExtras(List<CkanExtra> extras) {
        this.extras = extras;
    }

    public List<CkanRelationship> getRelationships_as_object() {
        return relationships_as_object;
    }

    public void setRelationships_as_object(List<CkanRelationship> relationships_as_object) {
        this.relationships_as_object = relationships_as_object;
    }

    public List<CkanRelationship> getRelationships_as_subject() {
        return relationships_as_subject;
    }

    public void setRelationships_as_subject(List<CkanRelationship> relationships_as_subject) {
        this.relationships_as_subject = relationships_as_subject;
    }

    public List<CkanGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<CkanGroup> groups) {
        this.groups = groups;
    }

    public String getOwner_org() {
        return owner_org;
    }

    public void setOwner_org(String owner_org) {
        this.owner_org = owner_org;
    }
}
