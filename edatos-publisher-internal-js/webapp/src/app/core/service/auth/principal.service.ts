import { Injectable } from '@angular/core';
import { Role, UserCAS } from '@app/core/model';
import { Observable, Subject } from 'rxjs';
import { AuthServerProvider } from './auth-jwt.service';

@Injectable()
export class Principal {
    [x: string]: any;
    private userIdentity: UserCAS | null;
    private authenticated = false;
    private authenticationState = new Subject<any>();

    constructor(private authServerProvider: AuthServerProvider) {}

    public authenticate(identity) {
        this.userIdentity = identity;
        this.authenticated = identity !== null;
        this.authenticationState.next(this.userIdentity);
    }

    public hasRoles(rolesRuta: Role[]): Promise<boolean> {
        return Promise.resolve(this.rolesRutaMatchesRolesUsuario(rolesRuta));
    }

    public rolesRutaMatchesRolesUsuario(rolesRuta: Role[]) {
        rolesRuta = rolesRuta || [];
        if (rolesRuta.length === 0) {
            return true;
        }
        if (!this.userIdentity || !this.userIdentity.roles) {
            return false;
        }
        return rolesRuta.some((rolRuta) => this.userIdentity?.hasRole(rolRuta));
    }

    public identity(): Promise<UserCAS | null> {
        if (this.userIdentity) {
            return Promise.resolve(this.userIdentity);
        }

        const token: string = this.authServerProvider.getToken();
        if (token) {
            this.userIdentity = UserCAS.fromJwt(token);
            this.authenticated = true;
        } else {
            this.userIdentity = null;
            this.authenticated = false;
        }

        this.authenticationState.next(this.userIdentity);
        return Promise.resolve(this.userIdentity);
    }

    public isAuthenticated(): boolean {
        return this.authenticated;
    }

    public isIdentityResolved(): boolean {
        return this.userIdentity !== undefined;
    }

    public getAuthenticationState(): Observable<any> {
        return this.authenticationState.asObservable();
    }

    public correctlyLogged(): boolean {
        return Boolean(this.userIdentity && this.userIdentity.roles.length !== 0);
    }
}
