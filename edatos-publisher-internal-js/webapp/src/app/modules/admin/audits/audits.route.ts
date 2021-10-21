import { Route } from '@angular/router';
import { ITEMS_PER_PAGE } from '@app/app.constants';
import { UserRouteAccessGuard } from '@app/core/guard/user-route-access.guard';
import { ADMIN_ROLES } from '@app/core/service/auth';
import { PagingParamsResolver } from 'arte-ng/services';
import { AuditsComponent } from './audits.component';

export const auditsRoute: Route = {
    path: 'audits',
    component: AuditsComponent,
    resolve: {
        pagingParams: PagingParamsResolver,
    },
    data: {
        pageTitle: 'audits.title',
        roles: ADMIN_ROLES,
        defaultPagingParams: {
            page: '1',
            sort: 'auditEventDate,desc',
            size: ITEMS_PER_PAGE,
        },
    },
    canActivate: [UserRouteAccessGuard],
};
