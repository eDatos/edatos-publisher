import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SharedModule } from '@app/shared';
import { AccountRoutingModule } from './account-routing.module';
import { AccountComponent } from './account.component';
@NgModule({
    imports: [SharedModule, AccountRoutingModule],
    declarations: [AccountComponent],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AccountModule {}
