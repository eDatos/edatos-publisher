import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class MetricsService {
    constructor(private http: HttpClient) {}

    getMetrics(): Observable<any> {
        return this.http.get('management/metrics');
    }

    threadDump(): Observable<any> {
        return this.http.get('management/dump');
    }
}
