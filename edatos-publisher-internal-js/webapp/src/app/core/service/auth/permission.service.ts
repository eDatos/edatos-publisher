import { Injectable } from '@angular/core';
import { Role } from '@app/core/model';
import { Principal } from './principal.service';

export const USER_MANAGEMENT_ROLES = [Role.GESTOR, Role.ADMINISTRADOR];
export const TOOLS_ROLES = [Role.GESTOR, Role.ADMINISTRADOR];
export const ADMIN_ROLES = [Role.ADMINISTRADOR];

@Injectable()
export class PermissionService {
    constructor(private principal: Principal) {}

}
