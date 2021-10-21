import { Component, OnInit } from '@angular/core';
import { Log } from './log.model';
import { LogsService } from './logs.service';

@Component({
    selector: 'app-logs',
    templateUrl: './logs.component.html',
    styleUrls: ['./logs.component.scss'],
})
export class LogsComponent implements OnInit {
    loggers: Log[];
    filter: string;
    orderProp: string;
    reverse: boolean;
    public hiddenFilters: boolean = true;

    constructor(private logsService: LogsService) {
        this.filter = '';
        this.orderProp = 'name';
        this.reverse = false;
    }

    ngOnInit() {
        this.logsService.findAll().subscribe((loggers) => (this.loggers = loggers));
    }

    changeLevel(name: string, level: string) {
        const log = new Log(name, level);
        this.logsService.changeLevel(log).subscribe(() => {
            this.logsService.findAll().subscribe((loggers) => (this.loggers = loggers));
        });
    }
}
