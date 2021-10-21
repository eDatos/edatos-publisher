import { Component, OnInit } from '@angular/core';
import { ConfigService } from '@app/config';
import { ScriptService } from 'ngx-script-loader';

declare const MetamacNavBar: {
    loadNavbar(obj?: { element: string }): void;
};

@Component({
    selector: 'app-edatos-navbar',
    template: `
        <div *ngIf="isLoading; else loadComplete" class="edatos-navbar"></div>
        <ng-template #loadComplete>
            <nav id="edatos-navbar"></nav>
        </ng-template>
    `,
    styleUrls: ['./edatos-navbar.component.scss'],
})
export class EdatosNavbarComponent implements OnInit {
    public isLoading = true;

    constructor(private configService: ConfigService, private scriptService: ScriptService) {}

    public ngOnInit() {
        const navbarScriptUrl = this.configService.getConfig().metadata.navbarScriptUrl;
        this.scriptService.loadScript(`${navbarScriptUrl}/js/metamac-navbar.js`).subscribe(
            (_) => {
                this.isLoading = false;
                if (typeof MetamacNavBar === 'undefined') {
                    console.warn('Could not load eDatos navbar');
                } else {
                    MetamacNavBar.loadNavbar({
                        element: 'edatos-navbar',
                    });
                }
            },
            (err) => {
                console.error('Error al obtener el navbar', err);
            }
        );
    }
}
