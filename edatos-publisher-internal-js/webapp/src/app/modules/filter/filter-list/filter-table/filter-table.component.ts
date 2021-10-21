import { Component, Input, OnInit } from '@angular/core';
import { ITEMS_PER_PAGE, PAGINATION_OPTIONS } from '@app/app.constants';
import { ResponseWrapper } from '@app/core/utils/response-utils';
import { FilterFilter } from '@app/modules/filter/filter-search/filter-search';
import { Filter } from '@app/shared/model';
import { FilterService } from '@app/shared/service';
import { LazyLoadEvent } from 'primeng/api';
import { ConfigService } from '@app/config';

@Component({
    selector: 'app-filter-table',
    templateUrl: './filter-table.component.html',
})
export class FilterTableComponent implements OnInit {
    public filterTableColumns: any = [
        {
            fieldName: 'name',
            sortable: true,
            header: {
                handler: 'translate',
                translatePath: 'filter.entity.name',
            },
        },
        {
            fieldName: 'permalink',
            sortable: true,
            header: {
                handler: 'translate',
                translatePath: 'filter.entity.permalink',
            },
        },
        {
            fieldName: 'lastAccessDate',
            sortable: true,
            header: {
                handler: 'translate',
                translatePath: 'filter.entity.lastAccessDate',
            },
        },
    ];
    public filterAmount: number | null;
    public itemsPerPage = ITEMS_PER_PAGE;
    public paginatorOptions = PAGINATION_OPTIONS;
    public filters: Filter[];

    @Input()
    public userId: number;

    private filterSearch = new FilterFilter();
    private page = 1;
    private sortPredicate = 'created_date';
    private reverse = false;

    constructor(private filterService: FilterService, private configService: ConfigService) {}

    public ngOnInit(): void {
        this.loadAll();
    }

    public loadData(e: LazyLoadEvent) {
        this.page = e.first! / e.rows! + 1;
        this.itemsPerPage = e.rows!;
        if (e.sortField != null) {
            this.sortPredicate = this.filterSearch.fromCamelCaseToSnakeCase(e.sortField);
            this.reverse = e.sortOrder === 1;
        }
        this.loadAll();
    }

    public loadAll() {
        this.filterSearch.userId = this.userId;
        this.filterService
            .find({
                page: this.page - 1,
                size: PAGINATION_OPTIONS.indexOf(Number(this.itemsPerPage)) > -1 ? this.itemsPerPage : ITEMS_PER_PAGE,
                sort: this.sort(),
                query: this.filterSearch.toQuery(),
            })
            .subscribe((res) => this.onSuccess(res));
    }

    public sort() {
        const result = [this.sortPredicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.sortPredicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    public onSuccess(response: ResponseWrapper<Filter[]>) {
        this.filterAmount = response.totalCount();
        this.filters = response.body;
    }

    public getVisualizerPath(): string {
        const config = this.configService.getConfig();
        return config.metamac.visualizerPath;
    }
}
