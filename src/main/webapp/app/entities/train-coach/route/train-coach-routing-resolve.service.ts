import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrainCoach } from '../train-coach.model';
import { TrainCoachService } from '../service/train-coach.service';

const trainCoachResolve = (route: ActivatedRouteSnapshot): Observable<null | ITrainCoach> => {
  const id = route.params.id;
  if (id) {
    return inject(TrainCoachService)
      .find(id)
      .pipe(
        mergeMap((trainCoach: HttpResponse<ITrainCoach>) => {
          if (trainCoach.body) {
            return of(trainCoach.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default trainCoachResolve;
