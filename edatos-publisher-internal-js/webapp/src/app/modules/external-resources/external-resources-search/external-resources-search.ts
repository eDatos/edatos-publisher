import { DatePipe } from '@angular/common';
import { Injectable } from '@angular/core';
import { BaseEntityFilter, EntityFilter } from 'arte-ng/model';
import * as _ from 'lodash';

@Injectable()
export class FilterFilter extends BaseEntityFilter implements EntityFilter {

    constructor(public datePipe?: DatePipe) {
        super(datePipe);
    }

    public getCriterias() {
        const criterias = [];
        /*if (this.name) {
            criterias.push(`NAME ILIKE '%${this.escapeSingleQuotes(this.name)}%'`);
        }*/
        return criterias;
    }

    protected registerParameters() {
        /*this.registerParam({
            paramName: 'name',
            updateFilterFromParam: (param) => (this.name = param),
            clearFilter: () => (this.name = null),
        });*/
    }

    public fromCamelCaseToSnakeCase(param: string): string {
        return param.replace(/([A-Z])/g, '_$1').toLowerCase();
    }
}
