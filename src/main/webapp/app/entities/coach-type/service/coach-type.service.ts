import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICoachType, NewCoachType } from '../coach-type.model';

export type PartialUpdateCoachType = Partial<ICoachType> & Pick<ICoachType, 'id'>;

export type EntityResponseType = HttpResponse<ICoachType>;
export type EntityArrayResponseType = HttpResponse<ICoachType[]>;

@Injectable({ providedIn: 'root' })
export class CoachTypeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/coach-types');

  create(coachType: NewCoachType): Observable<EntityResponseType> {
    return this.http.post<ICoachType>(this.resourceUrl, coachType, { observe: 'response' });
  }

  update(coachType: ICoachType): Observable<EntityResponseType> {
    return this.http.put<ICoachType>(`${this.resourceUrl}/${this.getCoachTypeIdentifier(coachType)}`, coachType, { observe: 'response' });
  }

  partialUpdate(coachType: PartialUpdateCoachType): Observable<EntityResponseType> {
    return this.http.patch<ICoachType>(`${this.resourceUrl}/${this.getCoachTypeIdentifier(coachType)}`, coachType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICoachType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICoachType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCoachTypeIdentifier(coachType: Pick<ICoachType, 'id'>): number {
    return coachType.id;
  }

  compareCoachType(o1: Pick<ICoachType, 'id'> | null, o2: Pick<ICoachType, 'id'> | null): boolean {
    return o1 && o2 ? this.getCoachTypeIdentifier(o1) === this.getCoachTypeIdentifier(o2) : o1 === o2;
  }

  addCoachTypeToCollectionIfMissing<Type extends Pick<ICoachType, 'id'>>(
    coachTypeCollection: Type[],
    ...coachTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const coachTypes: Type[] = coachTypesToCheck.filter(isPresent);
    if (coachTypes.length > 0) {
      const coachTypeCollectionIdentifiers = coachTypeCollection.map(coachTypeItem => this.getCoachTypeIdentifier(coachTypeItem));
      const coachTypesToAdd = coachTypes.filter(coachTypeItem => {
        const coachTypeIdentifier = this.getCoachTypeIdentifier(coachTypeItem);
        if (coachTypeCollectionIdentifiers.includes(coachTypeIdentifier)) {
          return false;
        }
        coachTypeCollectionIdentifiers.push(coachTypeIdentifier);
        return true;
      });
      return [...coachTypesToAdd, ...coachTypeCollection];
    }
    return coachTypeCollection;
  }
}
