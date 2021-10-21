package es.gobcan.istac.edatos.publisher.core.domain;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_disabled_tokens")
public class DisabledTokenEntity {

    @Id
    private String token;

    @NotNull
    @Column(nullable = false)
    private Instant expirationDate;

    public DisabledTokenEntity() {
        super();
    }

    public DisabledTokenEntity(String token, Instant expirationDate) {
        super();
        this.token = token;
        this.expirationDate = expirationDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }
}
