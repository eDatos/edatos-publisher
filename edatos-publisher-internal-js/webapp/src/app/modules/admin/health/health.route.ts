import { Route } from '@angular/router';
import { UserRouteAccessGuard } from '@app/core/guard/user-route-access.guard';
import { ADMIN_ROLES } from '@app/core/service';

import { HealthCheckComponent } from './health.component';

export const healthRoute: Route = {
    path: 'health',
    component: HealthCheckComponent,
    data: {
        pageTitle: 'health.title',
        roles: ADMIN_ROLES,
    },
    canActivate: [UserRouteAccessGuard],
};
