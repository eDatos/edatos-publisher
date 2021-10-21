package es.gobcan.istac.edatos.publisher.rest.common.vm;

import es.gobcan.istac.edatos.publisher.rest.common.dto.UsuarioDto;

/**
 * View Model extending the UserDTO, which is meant to be used in the user
 * management UI.
 */
public class ManagedUserVM extends UsuarioDto {

    private static final long serialVersionUID = 1L;

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public ManagedUserVM(ManagedUserVM userDto) {
        super();
        updateFrom(userDto);
    }

    @Override
    public String toString() {
        return "ManagedUserVM (id = " + getId() + ", Nombre = " + getNombre() + ", Apellido1 = " + getApellido1() + ", Apellido2 = " + getApellido2() + ")";
    }
}
