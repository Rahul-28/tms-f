import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PassengerResolve from './route/passenger-routing-resolve.service';

const passengerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/passenger.component').then(m => m.PassengerComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/passenger-detail.component').then(m => m.PassengerDetailComponent),
    resolve: {
      passenger: PassengerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/passenger-update.component').then(m => m.PassengerUpdateComponent),
    resolve: {
      passenger: PassengerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/passenger-update.component').then(m => m.PassengerUpdateComponent),
    resolve: {
      passenger: PassengerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default passengerRoute;
