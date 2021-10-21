import { Route } from '@angular/router';
import { UserRouteAccessGuard } from '@app/core/guard/user-route-access.guard';
import { ADMIN_ROLES } from '@app/core/service/auth';

import { LogsComponent } from './logs.component';

export const logsRoute: Route = {
    path: 'logs',
    component: LogsComponent,
    data: {
        pageTitle: 'logs.title',
        roles: ADMIN_ROLES,
    },
    canActivate: [UserRouteAccessGuard],
};
