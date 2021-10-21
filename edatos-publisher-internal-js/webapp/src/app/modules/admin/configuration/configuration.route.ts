import { Route } from '@angular/router';
import { UserRouteAccessGuard } from '@app/core/guard/user-route-access.guard';
import { ADMIN_ROLES } from '@app/core/service/auth';

import { ConfigurationComponent } from './configuration.component';

export const configurationRoute: Route = {
    path: 'configuration',
    component: ConfigurationComponent,
    data: {
        pageTitle: 'configuration.title',
        roles: ADMIN_ROLES,
    },
    canActivate: [UserRouteAccessGuard],
};
