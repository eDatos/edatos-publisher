import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ExternalUser } from '@app/core/model';
import { convert, ResponseWrapper } from '@app/core/utils/response-utils';
import { Favorite } from '@app/shared/model';
import { createRequestOption } from 'arte-ng/utils';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
    providedIn: 'root',
})
export class FavoriteService {
    public resourceUrl = 'api/favorites';

    constructor(private http: HttpClient) {}

    public get(id: number): Observable<Favorite> {
        return this.http.get<Favorite>(`${this.resourceUrl}/${id}`).pipe(map((favorite) => convert(Favorite, favorite)));
    }

    public find(req?: any): Observable<ResponseWrapper<Favorite[]>> {
        const options = createRequestOption(req);
        return this.http
            .get<Favorite[]>(this.resourceUrl, { ...options, observe: 'response' })
            .pipe(map((favorite) => convert(Favorite, favorite)));
    }

    public findByUserId(id: number): Observable<Favorite[]> {
        return this.http
            .get<Favorite[]>(this.resourceUrl, {
                params: {
                    query: `USER_ID EQ ${id}`,
                    size: '100000',
                },
            })
            .pipe(map((favorite) => convert(Favorite, favorite)));
    }

    public save(favorite: Favorite): Observable<Favorite> {
        return this.http.post<Favorite>(this.resourceUrl, favorite).pipe(map((fav) => convert(Favorite, fav)));
    }

    public delete(id: number): Observable<Favorite> {
        return this.http.delete<Favorite>(`${this.resourceUrl}/${id}`).pipe(map((fav) => convert(Favorite, fav)));
    }

    public updateFavorites(favorites: Favorite[], externalUser: ExternalUser): Observable<Favorite[]> {
        return this.http
            .put<Favorite[]>(this.resourceUrl, favorites, { params: { externalUserId: externalUser.id.toString() } })
            .pipe(map((fav) => convert(Favorite, fav)));
    }
}
