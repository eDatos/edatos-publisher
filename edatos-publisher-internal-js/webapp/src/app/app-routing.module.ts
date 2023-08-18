import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ErrorComponent } from '@app/layouts/error';
import { DEFAULT_PATH } from './app.constants';
import {UserRouteAccessGuard} from "@app/core/guard";

const APP_ROUTES: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: DEFAULT_PATH,
    },
    {
        path: 'external-resources',
        loadChildren: () => import('./modules/external-resources/external-resources.module').then((m) => m.ExternalResourcesModule),
        canLoad: [UserRouteAccessGuard],
        data: {
            //roles: EXTERNAL_USER_ROLES,
        },
    },

    {
        path: '**',
        component: ErrorComponent,
    },
];

@NgModule({
    imports: [RouterModule.forRoot(APP_ROUTES, { useHash: true, onSameUrlNavigation: 'reload' })],
    exports: [RouterModule],
})
export class AppRoutingModule {}
