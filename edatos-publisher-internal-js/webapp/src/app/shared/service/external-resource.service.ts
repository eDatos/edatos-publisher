import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { convert, ResponseWrapper } from '@app/core/utils/response-utils';
import { createRequestOption } from 'arte-ng/utils';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {ExternalCkanItemEntity} from "@app/shared/model/external-resource.model";

@Injectable({
    providedIn: 'root',
})
export class ExternalResourceService {
    public readonly resourceUrl = 'api/external-resources';

    constructor(private http: HttpClient) {}

    public find(req?: any): Observable<ResponseWrapper<ExternalCkanItemEntity[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<ExternalCkanItemEntity[]>(this.resourceUrl, { ...options, observe: 'response' })
            .pipe(map((res) => convert(ExternalCkanItemEntity, res)));
    }
}
