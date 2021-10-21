package es.gobcan.istac.edatos.publisher.core.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "tb_captcha_responses")
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class CaptchaResponseEntity {

    @Id
    private String id;

    @NotNull
    @Column(nullable = false)
    private String response;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private Instant createdDate = Instant.now();

    public CaptchaResponseEntity(String id, String response) {
        super();
        this.id = id;
        this.response = response;
    }

    public CaptchaResponseEntity() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }
}
