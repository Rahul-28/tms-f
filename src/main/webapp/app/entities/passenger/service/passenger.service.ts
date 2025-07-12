import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPassenger, NewPassenger } from '../passenger.model';

export type PartialUpdatePassenger = Partial<IPassenger> & Pick<IPassenger, 'id'>;

export type EntityResponseType = HttpResponse<IPassenger>;
export type EntityArrayResponseType = HttpResponse<IPassenger[]>;

@Injectable({ providedIn: 'root' })
export class PassengerService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/passengers');

  create(passenger: NewPassenger): Observable<EntityResponseType> {
    return this.http.post<IPassenger>(this.resourceUrl, passenger, { observe: 'response' });
  }

  update(passenger: IPassenger): Observable<EntityResponseType> {
    return this.http.put<IPassenger>(`${this.resourceUrl}/${this.getPassengerIdentifier(passenger)}`, passenger, { observe: 'response' });
  }

  partialUpdate(passenger: PartialUpdatePassenger): Observable<EntityResponseType> {
    return this.http.patch<IPassenger>(`${this.resourceUrl}/${this.getPassengerIdentifier(passenger)}`, passenger, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPassenger>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPassenger[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPassengerIdentifier(passenger: Pick<IPassenger, 'id'>): number {
    return passenger.id;
  }

  comparePassenger(o1: Pick<IPassenger, 'id'> | null, o2: Pick<IPassenger, 'id'> | null): boolean {
    return o1 && o2 ? this.getPassengerIdentifier(o1) === this.getPassengerIdentifier(o2) : o1 === o2;
  }

  addPassengerToCollectionIfMissing<Type extends Pick<IPassenger, 'id'>>(
    passengerCollection: Type[],
    ...passengersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const passengers: Type[] = passengersToCheck.filter(isPresent);
    if (passengers.length > 0) {
      const passengerCollectionIdentifiers = passengerCollection.map(passengerItem => this.getPassengerIdentifier(passengerItem));
      const passengersToAdd = passengers.filter(passengerItem => {
        const passengerIdentifier = this.getPassengerIdentifier(passengerItem);
        if (passengerCollectionIdentifiers.includes(passengerIdentifier)) {
          return false;
        }
        passengerCollectionIdentifiers.push(passengerIdentifier);
        return true;
      });
      return [...passengersToAdd, ...passengerCollection];
    }
    return passengerCollection;
  }
}
