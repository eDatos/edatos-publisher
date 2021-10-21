import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { convert, ResponseWrapper } from '@app/core/utils/response-utils';
import { ExternalOperation } from '@app/shared/model';
import { createRequestOption } from 'arte-ng/utils';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
    providedIn: 'root',
})
export class ExternalOperationService {
    public readonly resourceUrl = 'api/external-operations';

    constructor(private http: HttpClient) {}

    public find(req?: any): Observable<ResponseWrapper<ExternalOperation[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<ExternalOperation[]>(this.resourceUrl, { ...options, observe: 'response' })
            .pipe(map((res) => convert(ExternalOperation, res)));
    }

    public update(externalOperation: ExternalOperation): Observable<ExternalOperation> {
        return this.http.put<ExternalOperation>(this.resourceUrl, externalOperation).pipe(map((externalOp) => convert(ExternalOperation, externalOp)));
    }
}
