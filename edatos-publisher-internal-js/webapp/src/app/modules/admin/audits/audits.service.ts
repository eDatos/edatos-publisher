import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Audit } from './audit.model';
import { ResponseWrapper } from 'arte-ng/model';
import { ResponseUtils } from 'arte-ng/utils';

@Injectable()
export class AuditsService {
    constructor(private http: HttpClient) {}

    find(req: any): Observable<ResponseWrapper> {
        let params: HttpParams = new HttpParams();
        params = params.set('fromDate', req.fromDate);
        params = params.set('toDate', req.toDate);
        params = params.set('page', req.page);
        params = params.set('size', req.size);
        params = params.set('sort', req.sort);

        return this.http.get('management/audits', { params, observe: 'response' }).pipe(map((res) => ResponseUtils.convertToResponseWrapper(res, Audit)));
    }
}
