import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBooking, NewBooking } from '../booking.model';

export type PartialUpdateBooking = Partial<IBooking> & Pick<IBooking, 'id'>;

type RestOf<T extends IBooking | NewBooking> = Omit<T, 'bookingDate' | 'travellingDate' | 'boardingTime' | 'arrivalTime'> & {
  bookingDate?: string | null;
  travellingDate?: string | null;
  boardingTime?: string | null;
  arrivalTime?: string | null;
};

export type RestBooking = RestOf<IBooking>;

export type NewRestBooking = RestOf<NewBooking>;

export type PartialUpdateRestBooking = RestOf<PartialUpdateBooking>;

export type EntityResponseType = HttpResponse<IBooking>;
export type EntityArrayResponseType = HttpResponse<IBooking[]>;

@Injectable({ providedIn: 'root' })
export class BookingService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bookings');

  create(booking: NewBooking): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(booking);
    return this.http
      .post<RestBooking>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(booking: IBooking): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(booking);
    return this.http
      .put<RestBooking>(`${this.resourceUrl}/${this.getBookingIdentifier(booking)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(booking: PartialUpdateBooking): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(booking);
    return this.http
      .patch<RestBooking>(`${this.resourceUrl}/${this.getBookingIdentifier(booking)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBooking>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBooking[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBookingIdentifier(booking: Pick<IBooking, 'id'>): number {
    return booking.id;
  }

  compareBooking(o1: Pick<IBooking, 'id'> | null, o2: Pick<IBooking, 'id'> | null): boolean {
    return o1 && o2 ? this.getBookingIdentifier(o1) === this.getBookingIdentifier(o2) : o1 === o2;
  }

  addBookingToCollectionIfMissing<Type extends Pick<IBooking, 'id'>>(
    bookingCollection: Type[],
    ...bookingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bookings: Type[] = bookingsToCheck.filter(isPresent);
    if (bookings.length > 0) {
      const bookingCollectionIdentifiers = bookingCollection.map(bookingItem => this.getBookingIdentifier(bookingItem));
      const bookingsToAdd = bookings.filter(bookingItem => {
        const bookingIdentifier = this.getBookingIdentifier(bookingItem);
        if (bookingCollectionIdentifiers.includes(bookingIdentifier)) {
          return false;
        }
        bookingCollectionIdentifiers.push(bookingIdentifier);
        return true;
      });
      return [...bookingsToAdd, ...bookingCollection];
    }
    return bookingCollection;
  }

  protected convertDateFromClient<T extends IBooking | NewBooking | PartialUpdateBooking>(booking: T): RestOf<T> {
    return {
      ...booking,
      bookingDate: booking.bookingDate?.toJSON() ?? null,
      travellingDate: booking.travellingDate?.format(DATE_FORMAT) ?? null,
      boardingTime: booking.boardingTime?.toJSON() ?? null,
      arrivalTime: booking.arrivalTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBooking: RestBooking): IBooking {
    return {
      ...restBooking,
      bookingDate: restBooking.bookingDate ? dayjs(restBooking.bookingDate) : undefined,
      travellingDate: restBooking.travellingDate ? dayjs(restBooking.travellingDate) : undefined,
      boardingTime: restBooking.boardingTime ? dayjs(restBooking.boardingTime) : undefined,
      arrivalTime: restBooking.arrivalTime ? dayjs(restBooking.arrivalTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBooking>): HttpResponse<IBooking> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBooking[]>): HttpResponse<IBooking[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
