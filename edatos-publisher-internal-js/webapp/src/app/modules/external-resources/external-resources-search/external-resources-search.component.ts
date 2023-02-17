import { Component, Input, OnInit } from '@angular/core';
import { ArteEventManager } from 'arte-ng/services';
import { Subject, Subscription } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import {FilterFilter} from "@app/modules/external-resources/external-resources-search/external-resources-search";

@Component({
  selector: 'app-external-resources-search',
  templateUrl: './external-resources-search.component.html',
  styleUrls: ['./external-resources-search.component.scss']
})
export class ExternalResourcesSearchComponent implements OnInit {
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
