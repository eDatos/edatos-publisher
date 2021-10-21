import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ITEMS_PER_PAGE } from '@app/app.constants';
import { UserRouteAccessGuard } from '@app/core/guard/user-route-access.guard';
import { USER_MANAGEMENT_ROLES } from '@app/core/service/auth';
import { ExternalUserFormComponent } from '@app/modules/external-user/external-user-form/external-user-form.component';
import { ExternalUserListComponent } from '@app/modules/external-user/external-user-list/external-user-list.component';
import { PagingParamsResolver } from 'arte-ng/services';

export const EXTERNAL_USER_ROUTES: Routes = [
    {
        path: '',
        canActivate: [UserRouteAccessGuard],
        component: ExternalUserListComponent,
        resolve: {
            pagingParams: PagingParamsResolver,
        },
        data: {
            pageTitle: 'externalUser.home.title',
            roles: USER_MANAGEMENT_ROLES,
            defaultPagingParams: {
                page: '1',
                sort: 'name,asc',
                size: ITEMS_PER_PAGE,
            },
        },
    },
    {
        path: ':id',
        canActivate: [UserRouteAccessGuard],
        component: ExternalUserFormComponent,
        data: {
            pageTitle: 'externalUser.home.title',
            roles: USER_MANAGEMENT_ROLES,
        },
    },
];

@NgModule({
    imports: [RouterModule.forChild(EXTERNAL_USER_ROUTES)],
    exports: [RouterModule],
})
export class ExternalUserRoutingModule {}
