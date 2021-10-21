import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ErrorComponent } from '@app/layouts/error';
import { DEFAULT_PATH } from './app.constants';
import { UserRouteAccessGuard } from './core/guard';
import { EXTERNAL_USER_ROLES, FAVORITE_ROLES, FILTER_ROLES, TOOLS_ROLES } from './core/service';

const APP_ROUTES: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: DEFAULT_PATH,
    },
    {
        path: 'account',
        loadChildren: () => import('./modules/account/account.module').then((m) => m.AccountModule),
        canLoad: [UserRouteAccessGuard],
    },
    {
        path: 'admin',
        loadChildren: () => import('./modules/admin/admin.module').then((m) => m.AdminModule),
        canLoad: [UserRouteAccessGuard],
        data: {
            roles: TOOLS_ROLES,
        },
    },
    {
        path: 'filter',
        loadChildren: () => import('./modules/filter/filter.module').then((m) => m.FilterModule),
        canLoad: [UserRouteAccessGuard],
        data: {
            roles: FILTER_ROLES,
        },
    },
    {
        path: 'category-subscriptions',
        loadChildren: () => import('./modules/category-subscriptions/category-subscriptions.module').then((m) => m.CategorySubscriptionsModule),
        canLoad: [UserRouteAccessGuard],
        data: {
            roles: FAVORITE_ROLES,
        },
    },
    {
        path: 'external-users',
        loadChildren: () => import('./modules/external-user/external-user.module').then((m) => m.ExternalUserModule),
        canLoad: [UserRouteAccessGuard],
        data: {
            roles: EXTERNAL_USER_ROLES,
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
