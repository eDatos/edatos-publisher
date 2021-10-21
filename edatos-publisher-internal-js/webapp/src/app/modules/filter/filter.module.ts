import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FilterListComponent } from '@app/modules/filter/filter-list/filter-list.component';
import { FilterTableComponent } from '@app/modules/filter/filter-list/filter-table/filter-table.component';
import { FilterRoutingModule } from '@app/modules/filter/filter-routing.module';
import { FilterFilter } from '@app/modules/filter/filter-search/filter-search';
import { FilterSearchComponent } from '@app/modules/filter/filter-search/filter-search.component';
import { SharedModule } from '@app/shared';
import { ArteAutocompleteModule, ArteEntityListEmptyModule, ArteSpinnerModule, ArteTableModule, ArteTriInputSwitchModule } from 'arte-ng';
import { ArteDirectivesModule } from 'arte-ng/directives';
import { FilterFormComponent } from './filter-form/filter-form.component';

@NgModule({
    declarations: [FilterListComponent, FilterFormComponent, FilterSearchComponent, FilterTableComponent],
    imports: [
        CommonModule,
        FilterRoutingModule,
        ArteSpinnerModule,
        ArteEntityListEmptyModule,
        ArteTableModule,
        SharedModule,
        ArteDirectivesModule,
        ArteAutocompleteModule,
        ArteTriInputSwitchModule,
    ],
    providers: [FilterFilter],
    exports: [FilterTableComponent],
})
export class FilterModule {}
