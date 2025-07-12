import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICoachType } from '../coach-type.model';
import { CoachTypeService } from '../service/coach-type.service';

const coachTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | ICoachType> => {
  const id = route.params.id;
  if (id) {
    return inject(CoachTypeService)
      .find(id)
      .pipe(
        mergeMap((coachType: HttpResponse<ICoachType>) => {
          if (coachType.body) {
            return of(coachType.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default coachTypeResolve;
