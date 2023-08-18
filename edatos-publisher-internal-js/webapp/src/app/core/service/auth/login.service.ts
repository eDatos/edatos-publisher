import { Injectable } from '@angular/core';

import { Principal } from './principal.service';
import { AuthServerProvider } from './auth-jwt.service';
import { ConfigService } from '@app/config/config.service';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class LoginService {
    constructor(private principal: Principal, private authServerProvider: AuthServerProvider, private configService: ConfigService, private http: HttpClient) {}

    logout() {
        this.authServerProvider.logout().subscribe();
        this.principal.authenticate(null);
    }

    loginInCas() {
        const config = this.configService.getConfig();
        window.location.href = config.cas.login + '?service=' + encodeURIComponent(config.cas.service);
    }
}
