import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProfileInfo } from '@app/core/model';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

@Injectable()
export class ProfileService {
    private profileInfoUrl = 'api/profile-info';

    constructor(private http: HttpClient) {}

    getProfileInfo(): Observable<ProfileInfo> {
        return this.http.get(this.profileInfoUrl).pipe(
            map((data: any) => {
                const pi = new ProfileInfo();
                pi.activeProfiles = data.activeProfiles;
                pi.ribbonEnv = data.ribbonEnv;
                pi.inProduction = data.activeProfiles.indexOf('prod') !== -1;
                return pi;
            })
        );
    }
}
