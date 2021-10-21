import { Route } from '@angular/router';
import { UserRouteAccessGuard } from '@app/core/guard';
import { TOOLS_ROLES } from '@app/core/service';

import { DataProtectionPolicyComponent } from './data-protection-policy.component';

export const dataProtectionPolicyConfigurationRoute: Route = {
    path: 'data-protection-policy',
    data: {
        roles: TOOLS_ROLES,
    },
    children: [
        {
            path: '',
            component: DataProtectionPolicyComponent,
        },
        {
            path: 'edit',
            component: DataProtectionPolicyComponent,
        },
    ],
    canActivate: [UserRouteAccessGuard],
};
