import dayjs from 'dayjs/esm';

import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 3366,
  customerId: 'slake splurge',
  username: 'usu',
  password: 'inclineX',
  email: 'G!@6w?.8}',
  mobileNumber: '4920461463',
  aadhaarNumber: '636154113330',
  isActive: false,
};

export const sampleWithPartialData: ICustomer = {
  id: 24067,
  customerId: 'whoa',
  username: 'ysE',
  password: 'sinfulXX',
  email: 'endQlt@p_"s;.}gn',
  mobileNumber: '6912144929',
  aadhaarNumber: '620539602553',
  isActive: false,
};

export const sampleWithFullData: ICustomer = {
  id: 4149,
  customerId: 'where known',
  username: 'HXX',
  password: 'indeed astride outlandish',
  email: '4@ZC(?.4tQ"',
  mobileNumber: '0567580175',
  aadhaarNumber: '273272551210',
  address: 'secularize but sharply',
  contactInformation: 'card after jut',
  registrationDate: dayjs('2025-07-11T23:27'),
  isActive: false,
};

export const sampleWithNewData: NewCustomer = {
  customerId: 'thyme inasmuch',
  username: 'PXX',
  password: 'awXXXXXX',
  email: "?kl0p@9O'.xg",
  mobileNumber: '7925952681',
  aadhaarNumber: '786204462067',
  isActive: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
