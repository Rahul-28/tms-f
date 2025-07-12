import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPassenger } from '../passenger.model';
import { PassengerService } from '../service/passenger.service';

const passengerResolve = (route: ActivatedRouteSnapshot): Observable<null | IPassenger> => {
  const id = route.params.id;
  if (id) {
    return inject(PassengerService)
      .find(id)
      .pipe(
        mergeMap((passenger: HttpResponse<IPassenger>) => {
          if (passenger.body) {
            return of(passenger.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default passengerResolve;
