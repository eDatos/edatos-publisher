import { Route } from '@angular/router';
import { TOOLS_ROLES } from '@app/core/service';
import { ExternalOperationsListComponent } from '@app/modules/admin/external-operations-list/external-operations-list.component';

export const externalOperationsListRoutes: Route = {
    path: 'external-operations-list',
    children: [
        {
            path: '',
            pathMatch: 'full',
            component: ExternalOperationsListComponent,
            data: {
                pageTitle: 'externalOperations.home.title',
                roles: TOOLS_ROLES,
            },
        },
    ],
};
