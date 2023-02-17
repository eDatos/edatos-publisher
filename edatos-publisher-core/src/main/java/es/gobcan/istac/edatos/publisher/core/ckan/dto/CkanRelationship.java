package es.gobcan.istac.edatos.publisher.core.ckan.dto;

public class CkanRelationship {

    private String subject;
    private String object;
    private CkanRelationshipType type;
    private String comment;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public CkanRelationshipType getType() {
        return type;
    }

    public void setType(CkanRelationshipType type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
