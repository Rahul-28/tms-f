import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TrainCoachResolve from './route/train-coach-routing-resolve.service';

const trainCoachRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/train-coach.component').then(m => m.TrainCoachComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/train-coach-detail.component').then(m => m.TrainCoachDetailComponent),
    resolve: {
      trainCoach: TrainCoachResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/train-coach-update.component').then(m => m.TrainCoachUpdateComponent),
    resolve: {
      trainCoach: TrainCoachResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/train-coach-update.component').then(m => m.TrainCoachUpdateComponent),
    resolve: {
      trainCoach: TrainCoachResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trainCoachRoute;
