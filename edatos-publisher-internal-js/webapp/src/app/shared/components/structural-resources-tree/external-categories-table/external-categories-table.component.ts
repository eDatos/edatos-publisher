import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { ExternalCategory } from '@app/shared/model';
import { CategoryService } from '@app/shared/service';
import { LazyLoadEvent } from 'primeng/api';
import { Table } from 'primeng/table';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, finalize } from 'rxjs/operators';

@Component({
    selector: 'app-external-categories-table',
    templateUrl: './external-categories-table.component.html',
    styleUrls: ['./external-categories-table.component.scss'],
})
export class ExternalCategoriesTableComponent implements OnInit {
    /**
     * The selected external categories.
     */
    @Input()
    public selectedExternalCategories: ExternalCategory[];

    /**
     * The external categories that must appear as disabled.
     */
    @Input()
    public disabledExternalCategories: ExternalCategory[];

    /**
     * List of external categories to be displayed in the table. If null,
     * they will be automatically loaded by the component.
     */
    @Input()
    public externalCategories: ExternalCategory[] = [];

    @ViewChild(Table)
    public table: Table;

    public totalItems: number;
    public loading = false;
    public globalSearch = new Subject<InputEvent>();

    constructor(public categoryService: CategoryService) {}

    public ngOnInit(): void {
        this.globalSearch.pipe(debounceTime(750), distinctUntilChanged()).subscribe((event: InputEvent) => {
            this.table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
        });

        if (!this.externalCategories) {
            this.update();
        }
    }

    public getSelectedResources(): ExternalCategory[] {
        return this.selectedExternalCategories;
    }

    public isDisabled(resource: ExternalCategory): boolean {
        return this.disabledExternalCategories.some((category) => category.urn === resource.urn);
    }

    public onSelectAll() {
        this.selectedExternalCategories = this.selectedExternalCategories.filter((externalCat) => !this.isDisabled(externalCat));
    }

    public lazyLoad(e: LazyLoadEvent): void {
        const request = {
            page: e.first! / e.rows!,
            size: e.rows,
        };
        if (e.globalFilter) {
            Object.assign(request, { search: e.globalFilter });
        }
        if (e.sortField) {
            Object.assign(request, { sort: `${e.sortField},${e.sortOrder === 1 ? 'ASC' : 'DESC'}` });
        }
        this.update(request);
    }

    public update(params?): void {
        this.loading = true;
        this.categoryService
            .getExternalCategories(params)
            .pipe(finalize(() => (this.loading = false)))
            .subscribe((categories) => {
                this.totalItems = categories.totalCount()!;
                this.externalCategories = categories.body;
            });
    }
}
