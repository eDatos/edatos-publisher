import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { PageTitleService } from '@app/core/service';
import { filter, map } from 'rxjs/operators';
import { ERROR_ALERT_KEY } from './app.constants';
import 'reflect-metadata';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
    public errorAlertKey = ERROR_ALERT_KEY;

    constructor(private pageTitleService: PageTitleService, private router: Router) {}

    public ngOnInit() {
        this.router.events
            .pipe(
                filter((event) => event instanceof NavigationEnd),
                map(() => this.router)
            )
            .subscribe((_) => {
                const titleKey = this.getTitle(this.router.routerState, this.router.routerState.root).join(' - ');
                this.pageTitleService.update(titleKey);
            });
    }

    private getTitle(state, parent) {
        const data: string[] = [];
        if (parent?.snapshot.data?.pageTitle) {
            data.push(parent.snapshot.data.pageTitle);
        }

        if (state && parent) {
            data.push(...this.getTitle(state, state.firstChild(parent)));
        }
        return data;
    }
}
