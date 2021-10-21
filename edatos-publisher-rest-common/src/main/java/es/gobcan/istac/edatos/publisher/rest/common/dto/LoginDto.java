package es.gobcan.istac.edatos.publisher.rest.common.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class LoginDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private String login;

    @NotNull
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginDTO{" + "login='" + login + '\'' + '}';
    }
}
