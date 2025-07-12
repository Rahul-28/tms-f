import dayjs from 'dayjs/esm';

import { ITrain, NewTrain } from './train.model';

export const sampleWithRequiredData: ITrain = {
  id: 32106,
  trainNumber: 'slowly pish',
  trainName: 'whether necklace barring',
  origin: 'orient owlishly responsibility',
  destination: 'ah faraway welcome',
  intermediateStop: 'extremely if huzzah',
  serviceStartDate: dayjs('2025-07-11'),
  serviceEndDate: dayjs('2025-07-11'),
  serviceType: 'WEEKLY',
  departureTime: dayjs('2025-07-11T09:08'),
  arrivalTime: dayjs('2025-07-11T12:06'),
  basicPrice: 23744.65,
  isActive: true,
};

export const sampleWithPartialData: ITrain = {
  id: 30584,
  trainNumber: 'knowledgeably disbar since',
  trainName: 'cross-contamination',
  origin: 'narrowcast account ornate',
  destination: 'er',
  intermediateStop: 'coal continually towards',
  serviceStartDate: dayjs('2025-07-11'),
  serviceEndDate: dayjs('2025-07-11'),
  serviceType: 'DAILY',
  departureTime: dayjs('2025-07-12T03:36'),
  arrivalTime: dayjs('2025-07-11T18:52'),
  basicPrice: 3172.99,
  isActive: true,
};

export const sampleWithFullData: ITrain = {
  id: 18007,
  trainNumber: 'mooch yum',
  trainName: 'heavily',
  origin: 'excepting personal',
  destination: 'perspire whose',
  intermediateStop: 'abacus however throughout',
  serviceStartDate: dayjs('2025-07-12'),
  serviceEndDate: dayjs('2025-07-11'),
  serviceType: 'DAILY',
  departureTime: dayjs('2025-07-11T18:32'),
  arrivalTime: dayjs('2025-07-12T04:52'),
  basicPrice: 9573.43,
  isActive: true,
};

export const sampleWithNewData: NewTrain = {
  trainNumber: 'gadzooks mid',
  trainName: 'if',
  origin: 'wealthy space reproachfully',
  destination: 'instruction',
  intermediateStop: 'mmm so tomography',
  serviceStartDate: dayjs('2025-07-12'),
  serviceEndDate: dayjs('2025-07-11'),
  serviceType: 'WEEKLY',
  departureTime: dayjs('2025-07-11T08:29'),
  arrivalTime: dayjs('2025-07-11T23:45'),
  basicPrice: 450.27,
  isActive: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
