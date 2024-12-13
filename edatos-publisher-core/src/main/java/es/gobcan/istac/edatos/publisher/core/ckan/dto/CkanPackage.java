package es.gobcan.istac.edatos.publisher.core.ckan.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CkanPackage implements Serializable {

    /**
     * 
     */
    private static final long         serialVersionUID = 1L;
    private String                    name;

    @JsonProperty("private")
    private boolean                   privates;                // private
    private String                    author_email;
    private String                    maintainer_email;

    @JsonProperty("license_id")
    private String                    licenceId;
    private String                    url;
    private String                    version;
    private String                    state;
    private String                    type;

    @JsonProperty("contact_email")
    private String                    contactEmail;
    private String                    identifier;
    private String                    spatial;
    private List<String>              language;
    private List<String>              theme;

    private String                    author;
    private String                    maintainer;
    @JsonProperty("description_translated")
    private Map<String, String>       description;

    @JsonProperty("title_translated")
    private Map<String, String>       title;

    @JsonProperty("multilingual_tags")
    private Map<String, List<String>> tags;
    private String                    publisher;

    private List<CkanResource>        resources;
    private List<CkanExtra>           extras;
    private List<CkanRelationship>    relationships_as_object;
    private List<CkanRelationship>    relationships_as_subject;
    private List<CkanGroup>           groups;
    private String                    owner_org;
    private String                    id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrivates() {
        return privates;
    }

    public void setPrivates(boolean privates) {
        this.privates = privates;
    }

    public String getAuthor_email() {
        return author_email;
    }

    public void setAuthor_email(String author_email) {
        this.author_email = author_email;
    }

    public String getMaintainer_email() {
        return maintainer_email;
    }

    public void setMaintainer_email(String maintainer_email) {
        this.maintainer_email = maintainer_email;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getTheme() {
        return theme;
    }

    public void setTheme(List<String> theme) {
        this.theme = theme;
    }

    public Map<String, List<String>> getTags() {
        return tags;
    }

    public void setTags(Map<String, List<String>> tags) {
        this.tags = tags;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMaintainer() {
        return maintainer;
    }

    public void setMaintainer(String maintainer) {
        this.maintainer = maintainer;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLicenceId() {
        return licenceId;
    }

    public void setLicenceId(String licenceId) {
        this.licenceId = licenceId;
    }

    public String getSpatial() {
        return spatial;
    }

    public void setSpatial(String spatial) {
        this.spatial = spatial;
    }
}
