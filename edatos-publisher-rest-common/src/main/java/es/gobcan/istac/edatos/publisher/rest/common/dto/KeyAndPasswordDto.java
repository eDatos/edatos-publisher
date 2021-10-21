package es.gobcan.istac.edatos.publisher.rest.common.dto;

import java.io.Serializable;

public class KeyAndPasswordDto implements Serializable {

    private static final long serialVersionUID = -2395466878977953390L;
    private String newPassword;
    private String key;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
