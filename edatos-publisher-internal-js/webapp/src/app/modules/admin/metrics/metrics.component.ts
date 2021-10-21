import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { MetricsMonitoringModalComponent } from './metrics-modal.component';
import { MetricsService } from './metrics.service';

@Component({
    selector: 'app-metrics',
    templateUrl: './metrics.component.html',
})
export class MetricsMonitoringComponent implements OnInit {
    metrics: any = {};
    servicesStats: any = {};
    updatingMetrics = true;

    constructor(private modalService: NgbModal, private metricsService: MetricsService) {}

    ngOnInit() {
        this.refresh();
    }

    refresh() {
        this.updatingMetrics = true;
        this.metricsService.getMetrics().subscribe((metrics) => {
            this.metrics = metrics;
            this.updatingMetrics = false;
            this.servicesStats = {};
            Object.keys(metrics.timers).forEach((key) => {
                const value = metrics.timers[key];
                if (key.indexOf('web.rest') !== -1 || key.indexOf('service') !== -1 || key.indexOf('rest.internal.resources') !== -1) {
                    this.servicesStats[key] = value;
                }
            });
        });
    }

    refreshThreadDumpData() {
        this.metricsService.threadDump().subscribe((data) => {
            const modalRef = this.modalService.open(MetricsMonitoringModalComponent, { size: 'lg', container: '.page' });
            modalRef.componentInstance.threadDump = data;
            modalRef.result.then(
                (result) => {
                    // Left blank intentionally, nothing to do here
                },
                (reason) => {
                    // Left blank intentionally, nothing to do here
                }
            );
        });
    }

    filterNaN(input) {
        if (isNaN(input)) {
            return 0;
        }
        return input;
    }
}
