import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategorySubscriptionsRoutingModule } from '@app/modules/category-subscriptions/category-subscriptions-routing.module';
import { CategorySubscriptionsComponent } from '@app/modules/category-subscriptions/category-subscriptions.component';
import { SharedModule } from '@app/shared';

@NgModule({
    declarations: [CategorySubscriptionsComponent],
    imports: [CommonModule, CategorySubscriptionsRoutingModule, SharedModule],
})
export class CategorySubscriptionsModule {}
