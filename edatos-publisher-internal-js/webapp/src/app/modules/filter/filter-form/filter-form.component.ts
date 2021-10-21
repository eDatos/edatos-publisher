import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { ExternalUser } from '@app/core/model';
import { ExternalUserService } from '@app/core/service';
import { Filter } from '@app/shared/model';
import { FilterService } from '@app/shared/service';
import { finalize } from 'rxjs/operators';
import { ConfigService } from '@app/config';

@Component({
    selector: 'app-filter-form',
    templateUrl: './filter-form.component.html',
})
export class FilterFormComponent implements OnInit {
    public inEditMode = false;
    public isSaving = false;
    public isLoading = false;
    public filter: Filter;
    public externalUserList: ExternalUser[];
    public userId: number;

    constructor(
        private filterService: FilterService,
        private activatedRoute: ActivatedRoute,
        private titleService: Title,
        private router: Router,
        private externalUserService: ExternalUserService,
        private configService: ConfigService
    ) {
        this.filter = this.activatedRoute.snapshot.data['filter'] ?? new Filter();
        this.userId = parseInt(this.activatedRoute.snapshot.queryParams['id'], 10);
        this.loadExternalUsersList();
        this.activatedRoute.url.subscribe((segments) => {
            const lastUrlSegment = segments[segments.length - 1].path;
            this.inEditMode = this.inEditMode || lastUrlSegment === 'new';
        });
    }

    public ngOnInit() {
        if (!this.inEditMode) {
            this.titleService.setTitle(this.titleService.getTitle() + ' ' + this.filter.id);
        }
    }

    public goBack(): void {
        window.history.back();
    }

    public toggleEditMode(): void {
        this.inEditMode = !this.inEditMode;
    }

    public cancel(): void {
        this.updateData();
    }

    public edit(): void {
        this.toggleEditMode();
    }

    public delete() {
        this.showSpinner();
        this.filterService
            .delete(this.filter.id)
            .pipe(finalize(() => this.hideSpinner()))
            .subscribe((response) => {
                if (response.status >= 200 && response.status < 300) {
                    this.router.navigate([`..`], { relativeTo: this.activatedRoute });
                }
            });
    }

    public submit(): void {
        this.toggleIsSaving();
        if (this.filter.id == null) {
            this.filterService
                .save(this.filter)
                .pipe(finalize(() => this.toggleIsSaving()))
                .subscribe((filter) => {
                    this.router.navigate([`../${filter.id}`], { relativeTo: this.activatedRoute });
                });
        } else {
            this.filterService
                .update(this.filter)
                .pipe(finalize(() => this.toggleIsSaving()))
                .subscribe((filter) => {
                    this.filter = filter;
                    this.toggleEditMode();
                });
        }
    }

    public updateExternalUserSuggestions(event) {
        this.loadExternalUsersList(event);
    }

    public getVisualizerPath(): string {
        const config = this.configService.getConfig();
        return config.metamac.visualizerPath;
    }

    private loadExternalUsersList(event?) {
        this.externalUserService.find(event ? { query: `NAME ILIKE '%${event.query}%' OR EMAIL ILIKE '%${event.query}%'` } : undefined).subscribe((results) => {
            this.externalUserList = results.body;
            if (this.userId) {
                this.filter.externalUser = this.externalUserList.filter((user) => user.id === this.userId)?.[0];
            }
        });
    }

    private toggleIsSaving(): void {
        this.isSaving = !this.isSaving;
    }

    private updateData(): void {
        this.showSpinner();
        this.filterService.get(this.filter.id).subscribe((filter) => {
            this.filter = filter;
            this.hideSpinner();
            this.toggleEditMode();
        });
    }

    private showSpinner(): void {
        this.isLoading = true;
    }

    private hideSpinner(): void {
        this.isLoading = false;
    }
}
