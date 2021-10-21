import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MultiLanguageInputModule } from '@app/shared/components/multi-language-input/multi-language-input.module';
import { StructuralResourcesTreeComponent } from '@app/shared/components/structural-resources-tree/structural-resources-tree.component';
import { TranslateModule } from '@ngx-translate/core';
import { ArteDirectivesModule } from 'arte-ng/directives';
import { TreeDragDropService } from 'primeng/api';
import { DialogModule } from 'primeng/dialog';
import { InputSwitchModule } from 'primeng/inputswitch';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { TooltipModule } from 'primeng/tooltip';
import { TreeModule } from 'primeng/tree';
import { ExternalCategoriesTableComponent } from './external-categories-table/external-categories-table.component';

@NgModule({
    declarations: [StructuralResourcesTreeComponent, ExternalCategoriesTableComponent],
    imports: [
        CommonModule,
        TreeModule,
        TranslateModule,
        FormsModule,
        InputTextModule,
        ArteDirectivesModule,
        DialogModule,
        MultiLanguageInputModule,
        TableModule,
        TooltipModule,
        InputSwitchModule,
    ],
    exports: [StructuralResourcesTreeComponent],
    providers: [TreeDragDropService],
})
export class StructuralResourcesTreeModule {}
