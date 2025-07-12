import { IPassenger, NewPassenger } from './passenger.model';

export const sampleWithRequiredData: IPassenger = {
  id: 22585,
  passengerName: 'scope edible simple',
  age: 96,
  coachNumber: 'um victoriously',
  seatNumber: 'despite likewise',
};

export const sampleWithPartialData: IPassenger = {
  id: 1233,
  passengerName: 'ugh',
  age: 99,
  coachNumber: 'monthly shush',
  seatNumber: 'mouser mockingly',
};

export const sampleWithFullData: IPassenger = {
  id: 9144,
  passengerName: 'ah',
  age: 8,
  coachNumber: 'possession lively',
  seatNumber: 'legend christen',
};

export const sampleWithNewData: NewPassenger = {
  passengerName: 'consequently',
  age: 50,
  coachNumber: 'swerve',
  seatNumber: 'below when urgently',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
