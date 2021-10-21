import { Route } from '@angular/router';
import { NotificationsComponent } from './notifications.component';

export const notificationsRoutes: Route = {
    path: 'notifications',
    children: [
        {
            path: '',
            pathMatch: 'full',
            component: NotificationsComponent,
            data: {
                pageTitle: 'notifications.home.title',
            },
        },
    ],
};
