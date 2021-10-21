import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HasTitlesContainer } from 'arte-ng';

import { ConfigurationService } from './configuration.service';

@Component({
    selector: 'app-configuration',
    templateUrl: './configuration.component.html',
})
export class ConfigurationComponent implements OnInit, HasTitlesContainer {
    allConfiguration: any = [];
    configuration: any = [];
    configKeys: any[];
    filter: string;
    orderProp: string;
    reverse: boolean;
    public hiddenFilters: boolean = true;

    @ViewChild('titlesContainer') titlesContainer: ElementRef;
    public instance: ConfigurationComponent;

    constructor(private configurationService: ConfigurationService) {
        this.configKeys = [];
        this.filter = '';
        this.orderProp = 'prefix';
        this.reverse = false;
        this.instance = this;
    }

    keys(dict): Array<string> {
        return dict === undefined ? [] : Object.keys(dict);
    }

    ngOnInit() {
        this.configurationService.get().subscribe((configuration) => {
            this.configuration = configuration;

            for (const config of configuration) {
                if (config.properties !== undefined) {
                    this.configKeys.push(Object.keys(config.properties));
                }
            }
        });

        this.configurationService.getEnv().subscribe((configuration) => {
            this.allConfiguration = configuration;
        });
    }

    getTitlesContainer() {
        return this.titlesContainer;
    }
}
