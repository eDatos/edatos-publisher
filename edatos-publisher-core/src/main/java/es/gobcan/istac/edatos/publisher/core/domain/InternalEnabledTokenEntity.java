package es.gobcan.istac.edatos.publisher.core.domain;

import java.time.Instant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_internal_enabled_token")
public class InternalEnabledTokenEntity {

    @Id
    private String serviceTicket;

    @NotNull
    @Column(unique = true, nullable = false, length = 1000)
    private String token;

    @NotNull
    @Column(nullable = false)
    private Instant expirationDate;

    public InternalEnabledTokenEntity() {
        super();
    }

    public InternalEnabledTokenEntity(String token, String serviceTicket, Instant expirationDate) {
        super();
        this.token = token;
        this.serviceTicket = serviceTicket;
        this.expirationDate = expirationDate;
    }

    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }


    public String getServiceTicket() {
        return serviceTicket;
    }


    public void setServiceTicket(String serviceTicket) {
        this.serviceTicket = serviceTicket;
    }


    public Instant getExpirationDate() {
        return expirationDate;
    }


    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InternalEnabledTokenEntity that = (InternalEnabledTokenEntity) o;
        return serviceTicket.equals(that.serviceTicket) && token.equals(that.token) && expirationDate.equals(that.expirationDate);
    }
}
