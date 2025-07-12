import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrainCoach, NewTrainCoach } from '../train-coach.model';

export type PartialUpdateTrainCoach = Partial<ITrainCoach> & Pick<ITrainCoach, 'id'>;

export type EntityResponseType = HttpResponse<ITrainCoach>;
export type EntityArrayResponseType = HttpResponse<ITrainCoach[]>;

@Injectable({ providedIn: 'root' })
export class TrainCoachService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/train-coaches');

  create(trainCoach: NewTrainCoach): Observable<EntityResponseType> {
    return this.http.post<ITrainCoach>(this.resourceUrl, trainCoach, { observe: 'response' });
  }

  update(trainCoach: ITrainCoach): Observable<EntityResponseType> {
    return this.http.put<ITrainCoach>(`${this.resourceUrl}/${this.getTrainCoachIdentifier(trainCoach)}`, trainCoach, {
      observe: 'response',
    });
  }

  partialUpdate(trainCoach: PartialUpdateTrainCoach): Observable<EntityResponseType> {
    return this.http.patch<ITrainCoach>(`${this.resourceUrl}/${this.getTrainCoachIdentifier(trainCoach)}`, trainCoach, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITrainCoach>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrainCoach[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrainCoachIdentifier(trainCoach: Pick<ITrainCoach, 'id'>): number {
    return trainCoach.id;
  }

  compareTrainCoach(o1: Pick<ITrainCoach, 'id'> | null, o2: Pick<ITrainCoach, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrainCoachIdentifier(o1) === this.getTrainCoachIdentifier(o2) : o1 === o2;
  }

  addTrainCoachToCollectionIfMissing<Type extends Pick<ITrainCoach, 'id'>>(
    trainCoachCollection: Type[],
    ...trainCoachesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trainCoaches: Type[] = trainCoachesToCheck.filter(isPresent);
    if (trainCoaches.length > 0) {
      const trainCoachCollectionIdentifiers = trainCoachCollection.map(trainCoachItem => this.getTrainCoachIdentifier(trainCoachItem));
      const trainCoachesToAdd = trainCoaches.filter(trainCoachItem => {
        const trainCoachIdentifier = this.getTrainCoachIdentifier(trainCoachItem);
        if (trainCoachCollectionIdentifiers.includes(trainCoachIdentifier)) {
          return false;
        }
        trainCoachCollectionIdentifiers.push(trainCoachIdentifier);
        return true;
      });
      return [...trainCoachesToAdd, ...trainCoachCollection];
    }
    return trainCoachCollection;
  }
}
