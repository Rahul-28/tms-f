import { ITrainCoach, NewTrainCoach } from './train-coach.model';

export const sampleWithRequiredData: ITrainCoach = {
  id: 350,
  trainNumber: 'sate excluding',
  seatCapacity: 19051,
  availableSeats: 14361,
  farePrice: 2935.18,
};

export const sampleWithPartialData: ITrainCoach = {
  id: 16903,
  trainNumber: 'pace specific even',
  seatCapacity: 16298,
  availableSeats: 12961,
  farePrice: 1058.27,
};

export const sampleWithFullData: ITrainCoach = {
  id: 15229,
  trainNumber: 'after during',
  seatCapacity: 18134,
  availableSeats: 2962,
  farePrice: 16234.65,
};

export const sampleWithNewData: NewTrainCoach = {
  trainNumber: 'but',
  seatCapacity: 16855,
  availableSeats: 13376,
  farePrice: 24395.67,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
