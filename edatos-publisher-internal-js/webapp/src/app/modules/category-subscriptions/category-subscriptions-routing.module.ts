import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessGuard } from '@app/core/guard';
import { CategorySubscriptionsComponent } from '@app/modules/category-subscriptions/category-subscriptions.component';

export const CATEGORY_SUBSCRIPTIONS_ROUTES: Routes = [
    {
        path: '',
        pathMatch: 'full',
        component: CategorySubscriptionsComponent,
        data: {
            pageTitle: 'categorySubscriptions.home.title',
        },
        canActivate: [UserRouteAccessGuard],
    },
];

@NgModule({
    imports: [RouterModule.forChild(CATEGORY_SUBSCRIPTIONS_ROUTES)],
    exports: [RouterModule],
})
export class CategorySubscriptionsRoutingModule {}
