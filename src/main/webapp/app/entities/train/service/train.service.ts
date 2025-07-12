import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrain, NewTrain } from '../train.model';

export type PartialUpdateTrain = Partial<ITrain> & Pick<ITrain, 'id'>;

type RestOf<T extends ITrain | NewTrain> = Omit<T, 'serviceStartDate' | 'serviceEndDate' | 'departureTime' | 'arrivalTime'> & {
  serviceStartDate?: string | null;
  serviceEndDate?: string | null;
  departureTime?: string | null;
  arrivalTime?: string | null;
};

export type RestTrain = RestOf<ITrain>;

export type NewRestTrain = RestOf<NewTrain>;

export type PartialUpdateRestTrain = RestOf<PartialUpdateTrain>;

export type EntityResponseType = HttpResponse<ITrain>;
export type EntityArrayResponseType = HttpResponse<ITrain[]>;

@Injectable({ providedIn: 'root' })
export class TrainService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trains');

  create(train: NewTrain): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(train);
    return this.http.post<RestTrain>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(train: ITrain): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(train);
    return this.http
      .put<RestTrain>(`${this.resourceUrl}/${this.getTrainIdentifier(train)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(train: PartialUpdateTrain): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(train);
    return this.http
      .patch<RestTrain>(`${this.resourceUrl}/${this.getTrainIdentifier(train)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTrain>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTrain[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrainIdentifier(train: Pick<ITrain, 'id'>): number {
    return train.id;
  }

  compareTrain(o1: Pick<ITrain, 'id'> | null, o2: Pick<ITrain, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrainIdentifier(o1) === this.getTrainIdentifier(o2) : o1 === o2;
  }

  addTrainToCollectionIfMissing<Type extends Pick<ITrain, 'id'>>(
    trainCollection: Type[],
    ...trainsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trains: Type[] = trainsToCheck.filter(isPresent);
    if (trains.length > 0) {
      const trainCollectionIdentifiers = trainCollection.map(trainItem => this.getTrainIdentifier(trainItem));
      const trainsToAdd = trains.filter(trainItem => {
        const trainIdentifier = this.getTrainIdentifier(trainItem);
        if (trainCollectionIdentifiers.includes(trainIdentifier)) {
          return false;
        }
        trainCollectionIdentifiers.push(trainIdentifier);
        return true;
      });
      return [...trainsToAdd, ...trainCollection];
    }
    return trainCollection;
  }

  protected convertDateFromClient<T extends ITrain | NewTrain | PartialUpdateTrain>(train: T): RestOf<T> {
    return {
      ...train,
      serviceStartDate: train.serviceStartDate?.format(DATE_FORMAT) ?? null,
      serviceEndDate: train.serviceEndDate?.format(DATE_FORMAT) ?? null,
      departureTime: train.departureTime?.toJSON() ?? null,
      arrivalTime: train.arrivalTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTrain: RestTrain): ITrain {
    return {
      ...restTrain,
      serviceStartDate: restTrain.serviceStartDate ? dayjs(restTrain.serviceStartDate) : undefined,
      serviceEndDate: restTrain.serviceEndDate ? dayjs(restTrain.serviceEndDate) : undefined,
      departureTime: restTrain.departureTime ? dayjs(restTrain.departureTime) : undefined,
      arrivalTime: restTrain.arrivalTime ? dayjs(restTrain.arrivalTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTrain>): HttpResponse<ITrain> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTrain[]>): HttpResponse<ITrain[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
