import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ErrorComponent } from './error.component';

export const errorRoute: Routes = [
    {
        path: 'error',
        component: ErrorComponent,
        data: {
            roles: [],
            pageTitle: 'error.404.title',
        },
    },
    {
        path: 'accessdenied',
        component: ErrorComponent,
        data: {
            roles: [],
            pageTitle: 'error.403.title',
            error403: true,
        },
    },
    {
        path: 'notfound',
        component: ErrorComponent,
        data: {
            roles: [],
            pageTitle: 'error.404.title',
            error404: true,
        },
    },
    {
        path: 'non-existent-user',
        component: ErrorComponent,
        data: {
            roles: [],
            pageTitle: 'error.nonExistingUser.title',
            nonExistentUser: true,
        },
    },
    {
        path: 'blocked',
        component: ErrorComponent,
        data: {
            roles: [],
            pageTitle: 'error.blockedUser.title',
            blockedUser: true,
        },
    },
];

@NgModule({
    imports: [RouterModule.forChild(errorRoute)],
    exports: [RouterModule],
})
export class ErrorRoutingModule {}
