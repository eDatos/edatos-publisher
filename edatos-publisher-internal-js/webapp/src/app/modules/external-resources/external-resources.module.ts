import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExternalResourcesListComponent } from './external-resources-list/external-resources-list.component';
import {ExternalResourcesRoutingModule} from "@app/modules/external-resources/external-resources-routing.module";
import {SharedModule} from "@app/shared";
import {ArteSpinnerModule, ArteTriInputSwitchModule} from "arte-ng";
import { ExternalResourcesSearchComponent } from './external-resources-search/external-resources-search.component';

@NgModule({
  declarations: [ExternalResourcesListComponent, ExternalResourcesSearchComponent],
    imports: [
        CommonModule,
        ExternalResourcesRoutingModule,
        SharedModule,
        ArteSpinnerModule,
        ArteTriInputSwitchModule
    ]
})
export class ExternalResourcesModule { }
