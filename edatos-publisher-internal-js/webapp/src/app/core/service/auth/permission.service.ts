import { Injectable } from '@angular/core';
import { Role } from '@app/core/model';
import { Principal } from './principal.service';

export const USER_MANAGEMENT_ROLES = [Role.GESTOR, Role.ADMINISTRADOR];
export const TOOLS_ROLES = [Role.GESTOR, Role.ADMINISTRADOR];
export const ADMIN_ROLES = [Role.ADMINISTRADOR];
export const NAVBAR_ROLES = [Role.GESTOR, Role.ADMINISTRADOR];

export const FILTER_ROLES = [Role.GESTOR, Role.ADMINISTRADOR];
export const FAVORITE_ROLES = [Role.GESTOR, Role.ADMINISTRADOR];
export const EXTERNAL_USER_ROLES = [Role.GESTOR, Role.ADMINISTRADOR];

@Injectable()
export class PermissionService {
    constructor(private principal: Principal) {}

    public canNavigateUserManagement(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(USER_MANAGEMENT_ROLES);
    }

    public canNavigateTools(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(TOOLS_ROLES);
    }

    public canNavigateAdminMenu(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(ADMIN_ROLES);
    }

    public canNavigateNavbarMenu(): boolean {
        return this.principal.rolesRutaMatchesRolesUsuario(NAVBAR_ROLES);
    }
}
