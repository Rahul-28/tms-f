import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CoachTypeResolve from './route/coach-type-routing-resolve.service';

const coachTypeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/coach-type.component').then(m => m.CoachTypeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/coach-type-detail.component').then(m => m.CoachTypeDetailComponent),
    resolve: {
      coachType: CoachTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/coach-type-update.component').then(m => m.CoachTypeUpdateComponent),
    resolve: {
      coachType: CoachTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/coach-type-update.component').then(m => m.CoachTypeUpdateComponent),
    resolve: {
      coachType: CoachTypeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default coachTypeRoute;
