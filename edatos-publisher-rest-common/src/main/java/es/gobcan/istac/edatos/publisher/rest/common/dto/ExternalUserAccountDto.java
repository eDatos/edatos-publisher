package es.gobcan.istac.edatos.publisher.rest.common.dto;

public class ExternalUserAccountDto extends ExternalUserAccountBaseDto {

    private static final long serialVersionUID = 1L;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
