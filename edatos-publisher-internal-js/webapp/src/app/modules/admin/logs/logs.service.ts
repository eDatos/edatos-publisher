import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Log } from './log.model';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class LogsService {
    constructor(private http: HttpClient) {}

    changeLevel(log: Log): Observable<any> {
        return this.http.put('management/logs', log);
    }

    findAll(): Observable<Log[]> {
        return this.http.get<Log[]>('management/logs');
    }
}
