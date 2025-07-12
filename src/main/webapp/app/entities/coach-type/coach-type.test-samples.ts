import { ICoachType, NewCoachType } from './coach-type.model';

export const sampleWithRequiredData: ICoachType = {
  id: 19016,
  coachId: 'since oof desecrate',
  coachName: 'tooth boo uh-huh',
  seatCapacity: 21143,
  fareMultiplier: 14285.67,
};

export const sampleWithPartialData: ICoachType = {
  id: 9186,
  coachId: 'yearly',
  coachName: 'save as ah',
  seatCapacity: 3104,
  fareMultiplier: 31140.83,
};

export const sampleWithFullData: ICoachType = {
  id: 5481,
  coachId: 'nor',
  coachName: 'heating',
  seatCapacity: 24866,
  fareMultiplier: 20025.96,
};

export const sampleWithNewData: NewCoachType = {
  coachId: 'mild adventurously uh-huh',
  coachName: 'loyalty since zowie',
  seatCapacity: 13594,
  fareMultiplier: 28462.78,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
