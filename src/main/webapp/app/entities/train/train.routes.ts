import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TrainResolve from './route/train-routing-resolve.service';

const trainRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/train.component').then(m => m.TrainComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/train-detail.component').then(m => m.TrainDetailComponent),
    resolve: {
      train: TrainResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/train-update.component').then(m => m.TrainUpdateComponent),
    resolve: {
      train: TrainResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/train-update.component').then(m => m.TrainUpdateComponent),
    resolve: {
      train: TrainResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trainRoute;
