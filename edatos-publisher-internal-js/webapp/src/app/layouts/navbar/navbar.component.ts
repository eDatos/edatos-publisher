import { Component, OnInit } from '@angular/core';
import { VERSION } from '@app/app.constants';
import { ConfigService } from '@app/config';
import { Role } from '@app/core/model';
import { LoginService, PermissionService, Principal, ProfileService } from '@app/core/service';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
    public inProduction: boolean;
    public isNavbarCollapsed: boolean;
    public version: string;
    public login: string | undefined;
    public isAdmin: boolean;

    constructor(
        private loginService: LoginService,
        public permissionService: PermissionService,
        public principal: Principal,
        private profileService: ProfileService,
        private configService: ConfigService
    ) {
        this.version = VERSION ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
    }

    public ngOnInit() {
        this.profileService.getProfileInfo().subscribe((profileInfo) => {
            this.inProduction = profileInfo.inProduction;
        });

        this.principal.identity().then((user) => {
            this.login = user?.login;
            this.isAdmin = user?.hasRole(Role.ADMINISTRADOR) ?? false;
        });
    }

    public collapseNavbar() {
        this.isNavbarCollapsed = true;
    }

    public isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    public logout() {
        this.collapseNavbar();
        this.loginService.logout();
        const config = this.configService.getConfig();
        window.location.href = config.cas.logout;
    }

    public toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    public correctlyLogged(): boolean {
        return Boolean(this.principal.correctlyLogged());
    }

    public canNavigateAdminMenu(): boolean {
        return this.permissionService.canNavigateAdminMenu();
    }
}
