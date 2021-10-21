package es.gobcan.istac.edatos.publisher.rest.common.dto;

import java.io.Serializable;

public class ChangePasswordDto implements Serializable {

    private static final long serialVersionUID = 7725286882249242585L;

    private String currentPassword;

    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
