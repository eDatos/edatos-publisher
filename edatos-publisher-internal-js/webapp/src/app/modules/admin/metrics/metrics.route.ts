import { Route } from '@angular/router';
import { UserRouteAccessGuard } from '@app/core/guard/user-route-access.guard';
import { ADMIN_ROLES } from '@app/core/service';

import { MetricsMonitoringComponent } from './metrics.component';

export const metricsRoute: Route = {
    path: 'metrics',
    component: MetricsMonitoringComponent,
    data: {
        pageTitle: 'metrics.title',
        roles: ADMIN_ROLES,
    },
    canActivate: [UserRouteAccessGuard],
};
