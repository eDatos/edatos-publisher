import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FilterFilter } from '@app/modules/filter';
import { ArteEventManager } from 'arte-ng/services';
import { Subject, Subscription } from 'rxjs';
import { debounceTime } from 'rxjs/operators';

@Component({
    selector: 'app-filter-search',
    templateUrl: 'filter-search.component.html',
})
export class FilterSearchComponent implements OnInit, OnDestroy {
    @Input()
    public filters: FilterFilter;

    public subscription: Subscription;

    private filterChangesSubject: Subject<any> = new Subject<any>();

    constructor(private eventManager: ArteEventManager) {}

    public ngOnInit() {
        this.subscription = this.filterChangesSubject.pipe(debounceTime(300)).subscribe(() =>
            this.eventManager.broadcast({
                name: 'filterSearch',
                content: this.filters,
            })
        );
    }

    public ngOnDestroy() {
        this.eventManager.destroy(this.subscription);
    }

    public filter(): void {
        this.filterChangesSubject.next();
    }

    public resetFilters(): void {
        this.filters.reset();
        this.filter();
    }
}
