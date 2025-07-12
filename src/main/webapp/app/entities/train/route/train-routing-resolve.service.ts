import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrain } from '../train.model';
import { TrainService } from '../service/train.service';

const trainResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrain> => {
  const id = route.params.id;
  if (id) {
    return inject(TrainService)
      .find(id)
      .pipe(
        mergeMap((train: HttpResponse<ITrain>) => {
          if (train.body) {
            return of(train.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default trainResolve;
