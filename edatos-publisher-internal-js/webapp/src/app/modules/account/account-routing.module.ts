import { Routes, RouterModule } from '@angular/router';

import { AccountComponent } from './account.component';
import { UserRouteAccessGuard } from '@app/core/guard/user-route-access.guard';
import { NgModule } from '@angular/core';

export const accountRoutes: Routes = [
    {
        path: '',
        component: AccountComponent,
        data: {
            pageTitle: 'account.title',
        },
        canActivate: [UserRouteAccessGuard],
    },
    {
        path: 'edit',
        component: AccountComponent,
        data: {
            pageTitle: 'account.title',
        },
        canActivate: [UserRouteAccessGuard],
    },
];

@NgModule({
    imports: [RouterModule.forChild(accountRoutes)],
    exports: [RouterModule],
})
export class AccountRoutingModule {}
