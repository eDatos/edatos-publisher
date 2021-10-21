import { Route } from '@angular/router';
import { CategoryMasterTreeComponent } from '@app/modules/admin/category-master-tree/category-master-tree.component';

export const categoryMasterTreeRoutes: Route = {
    path: 'category-master-tree',
    children: [
        {
            path: '',
            pathMatch: 'full',
            component: CategoryMasterTreeComponent,
        },
    ],
};
