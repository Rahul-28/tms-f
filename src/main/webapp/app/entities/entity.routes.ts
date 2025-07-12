import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'jhipsterApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'jhipsterApp.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'admin',
    data: { pageTitle: 'jhipsterApp.admin.home.title' },
    loadChildren: () => import('./admin/admin.routes'),
  },
  {
    path: 'train',
    data: { pageTitle: 'jhipsterApp.train.home.title' },
    loadChildren: () => import('./train/train.routes'),
  },
  {
    path: 'coach-type',
    data: { pageTitle: 'jhipsterApp.coachType.home.title' },
    loadChildren: () => import('./coach-type/coach-type.routes'),
  },
  {
    path: 'train-coach',
    data: { pageTitle: 'jhipsterApp.trainCoach.home.title' },
    loadChildren: () => import('./train-coach/train-coach.routes'),
  },
  {
    path: 'booking',
    data: { pageTitle: 'jhipsterApp.booking.home.title' },
    loadChildren: () => import('./booking/booking.routes'),
  },
  {
    path: 'passenger',
    data: { pageTitle: 'jhipsterApp.passenger.home.title' },
    loadChildren: () => import('./passenger/passenger.routes'),
  },
  {
    path: 'payment',
    data: { pageTitle: 'jhipsterApp.payment.home.title' },
    loadChildren: () => import('./payment/payment.routes'),
  },
  {
    path: 'invoice',
    data: { pageTitle: 'jhipsterApp.invoice.home.title' },
    loadChildren: () => import('./invoice/invoice.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
