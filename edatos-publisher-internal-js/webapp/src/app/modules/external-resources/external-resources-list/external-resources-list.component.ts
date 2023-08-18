import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ITEMS_PER_PAGE, PAGINATION_OPTIONS } from '@app/app.constants';
import { ResponseWrapper } from '@app/core/utils/response-utils';
import { ArteEventManager } from 'arte-ng/services';
import { LazyLoadEvent } from 'primeng/api';
import { Subscription } from 'rxjs';
import { FilterFilter } from "@app/modules/external-resources/external-resources-search/external-resources-search";
import { ExternalCkanItemEntity } from "@app/shared/model/external-resource.model";
import { ExternalResourceService } from "@app/shared/service/external-resource.service";

@Component({
  selector: 'app-external-resources-list',
  templateUrl: './external-resources-list.component.html',
  styleUrls: ['./external-resources-list.component.scss']
})
export class ExternalResourcesListComponent implements OnInit {
    public externalCkanItems?: ExternalCkanItemEntity[];
    public filterSearch = new FilterFilter();
    public totalItems: number | null = null;
    public itemsPerPage?: number;
    public columns: any = [
        {
            fieldName: 'type',
            sortable: true,
            header: {
                handler: 'translate',
                translatePath: 'filter.entity.type', // TODO ¿internacionalizar?
            },
        },
        {
            fieldName: 'name',
            sortable: true,
            header: {
                handler: 'translate',
                translatePath: 'filter.entity.name',
            },
        },
        {
            fieldName: 'code',
            sortable: true,
            header: {
                handler: 'translate',
                translatePath: 'filter.entity.code',
            },
        },
        {
            fieldName: 'createdDate',
            sortable: true,
            header: {
                handler: 'translate',
                translatePath: 'entity.audits.createdDate',
            },
        },
    ];
    private eventSubscriber?: Subscription;
    private searchSubscription?: Subscription;
    private page: any;
    private reverse?: boolean;
    private predicate: any;

    constructor(private externalResourceService: ExternalResourceService, private activatedRoute: ActivatedRoute, private router: Router, private eventManager: ArteEventManager) {
        this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
            this.itemsPerPage = data['pagingParams'].itemsPerPage;
        });
    }

    public ngOnInit(): void {
        this.processUrlParams();
        this.loadAll();
        this.registerChangeInUsers();
    }

    public transition() {
        const transitionParams = {
            page: this.page,
            size: PAGINATION_OPTIONS.indexOf(Number(this.itemsPerPage)) > -1 ? this.itemsPerPage : ITEMS_PER_PAGE,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
        };
        const queryParams = {
            ...this.filterSearch.toUrl(this.activatedRoute.snapshot.queryParams),
            ...transitionParams,
        };
        this.router.navigate(['/external-resources'], { replaceUrl: true, queryParams });
        this.processUrlParams();
        this.loadAll();
    }

    public loadData(e: LazyLoadEvent) {
        this.page = e.first! / e.rows! + 1;
        this.itemsPerPage = e.rows;
        if (e.sortField != null) {
            this.predicate = this.filterSearch.fromCamelCaseToSnakeCase(e.sortField);
            this.reverse = e.sortOrder === 1;
        }
        this.transition();
    }

    public sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    public clear() {
        this.page = 0;
        this.router.navigate([
            '/external-resources',
            {
                page: this.page,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
            },
        ]);
    }

    public onSuccess(response: ResponseWrapper<ExternalCkanItemEntity[]>) {
        this.totalItems = response.totalCount();
        this.externalCkanItems = response.body;
    }

    public loadAll() {
        this.externalResourceService
            .find({
                page: this.page - 1,
                size: PAGINATION_OPTIONS.indexOf(Number(this.itemsPerPage)) > -1 ? this.itemsPerPage : ITEMS_PER_PAGE,
                sort: this.sort(),
                query: this.filterSearch.toQuery(),
            })
            .subscribe((res) => this.onSuccess(res));
    }

    public registerChangeInUsers() {
        this.eventSubscriber = this.eventManager.subscribe('filterListModification', () => this.loadAll());
        this.searchSubscription = this.eventManager.subscribe('filterSearch', () => {
            this.page = 1;
            const queryParams = Object.assign({}, this.filterSearch.toUrl(this.activatedRoute.snapshot.queryParams));
            this.router.navigate([], { relativeTo: this.activatedRoute, queryParams });
            this.loadAll();
        });
    }

    private processUrlParams(): void {
        // TODO complete or remove method
    }
}
