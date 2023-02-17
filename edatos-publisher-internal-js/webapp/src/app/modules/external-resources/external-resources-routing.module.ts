import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessGuard } from '@app/core/guard/user-route-access.guard';
import {ExternalResourcesListComponent} from "@app/modules/external-resources/external-resources-list/external-resources-list.component";
import {PagingParamsResolver} from "arte-ng/services";
import {ITEMS_PER_PAGE} from "@app/app.constants";

export const EXTERNAL_USER_ROUTES: Routes = [
    {
        path: '',
        canActivate: [UserRouteAccessGuard],
        component: ExternalResourcesListComponent,
        resolve: {
            pagingParams: PagingParamsResolver,
        },
        data: {
            pageTitle: 'filter.home.title',
            defaultPagingParams: {
                page: '1',
                sort: 'created_date,desc',
                size: ITEMS_PER_PAGE,
            },
        },

    },
];

@NgModule({
    imports: [RouterModule.forChild(EXTERNAL_USER_ROUTES)],
    exports: [RouterModule],
})
export class ExternalResourcesRoutingModule {}
