import { Component, OnInit } from '@angular/core';
import { AuditInfoComponent } from 'arte-ng';

@Component({
    selector: 'app-audit-info',
    templateUrl: './audit-info.component.html',
    styleUrls: ['./audit-info.component.scss'],
})
export class AppAuditInfoComponent extends AuditInfoComponent implements OnInit {
    constructor() {
        super();
    }

    public ngOnInit(): void {}
}
