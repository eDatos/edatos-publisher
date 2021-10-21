import { registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import localeEs from '@angular/common/locales/es';
import { LOCALE_ID, NgModule, Optional, SkipSelf } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { CookieModule } from 'ngx-cookie';
import { UserRouteAccessGuard } from './guard';

import {
    AuthServerProvider,
    CSRFService,
    ExternalUserService,
    LoginService,
    PageTitleService,
    PermissionService,
    Principal,
    ProfileService,
    StateStorageService,
    throwIfAlreadyLoaded,
    UserService,
} from './service';

registerLocaleData(localeEs, 'es');
@NgModule({
    imports: [BrowserAnimationsModule, HttpClientModule, CookieModule.forRoot()],
    providers: [
        AuthServerProvider,
        CSRFService,
        LoginService,
        PermissionService,
        Principal,
        StateStorageService,
        UserService,
        UserRouteAccessGuard,
        PageTitleService,
        Title,
        {
            provide: LOCALE_ID,
            useValue: 'es',
        },
        ProfileService,
        ExternalUserService,
    ],
})
export class CoreModule {
    constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
        throwIfAlreadyLoaded(parentModule, 'CoreModule');
    }
}
