import * as jwtDecode from 'jwt-decode';
import { Role } from './rol.model';

export interface RolCAS {
    app: string;
    role: string;
}

export class UserCAS {
    private readonly ACL_APP_NAME: string = 'GESTOR_USUARIOS_EXTERNOS';
    constructor(public login: string, public roles: RolCAS[]) {}

    public static fromJwt(token: string) {
        const payload: { sub: string; auth: string; exp: string } = jwtDecode(token);
        const rolesCas = payload.auth.split(',').map((appRole) => {
            const [app, role] = appRole.split('#', 2);
            return { app, role } as RolCAS;
        });
        return new UserCAS(payload.sub, rolesCas);
    }

    public hasRole(rol: Role): boolean {
        return this.roles.some((userRol) => userRol.app == this.ACL_APP_NAME && userRol.role == rol);
    }
}
