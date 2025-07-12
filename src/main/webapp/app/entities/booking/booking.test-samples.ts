import dayjs from 'dayjs/esm';

import { IBooking, NewBooking } from './booking.model';

export const sampleWithRequiredData: IBooking = {
  id: 4955,
  pnrNumber: 'annually',
  bookingDate: dayjs('2025-07-11T22:39'),
  travellingDate: dayjs('2025-07-11'),
  boardingStation: 'minus',
  destinationStation: 'hot outsource consign',
  boardingTime: dayjs('2025-07-11T14:47'),
  arrivalTime: dayjs('2025-07-12T00:23'),
  totalFare: 7636.55,
  bookingStatus: 'PENDING',
  modeOfPayment: 'CREDIT_CARD',
  additionalServices: false,
  coachNumber: 'yet though',
  seatNumber: 'rigidly',
};

export const sampleWithPartialData: IBooking = {
  id: 76,
  pnrNumber: 'circa governance lovingly',
  bookingDate: dayjs('2025-07-11T18:08'),
  travellingDate: dayjs('2025-07-11'),
  boardingStation: 'coaxingly',
  destinationStation: 'revitalise',
  boardingTime: dayjs('2025-07-12T01:46'),
  arrivalTime: dayjs('2025-07-12T04:47'),
  totalFare: 2008.42,
  bookingStatus: 'COMPLETED',
  modeOfPayment: 'CREDIT_CARD',
  additionalServices: true,
  coachNumber: 'yuppify steeple',
  seatNumber: 'yum cautiously huzzah',
};

export const sampleWithFullData: IBooking = {
  id: 28482,
  pnrNumber: 'pish despite',
  bookingDate: dayjs('2025-07-11T13:41'),
  travellingDate: dayjs('2025-07-11'),
  boardingStation: 'indeed purse',
  destinationStation: 'slimy wherever',
  boardingTime: dayjs('2025-07-11T20:45'),
  arrivalTime: dayjs('2025-07-12T03:29'),
  totalFare: 4505.46,
  bookingStatus: 'CONFIRMED',
  modeOfPayment: 'DEBIT_CARD',
  additionalServices: true,
  coachNumber: 'after',
  seatNumber: 'regarding vague',
};

export const sampleWithNewData: NewBooking = {
  pnrNumber: 'glossy',
  bookingDate: dayjs('2025-07-11T18:00'),
  travellingDate: dayjs('2025-07-12'),
  boardingStation: 'quick-witted',
  destinationStation: 'oddly',
  boardingTime: dayjs('2025-07-11T17:16'),
  arrivalTime: dayjs('2025-07-11T21:23'),
  totalFare: 26090.73,
  bookingStatus: 'COMPLETED',
  modeOfPayment: 'CREDIT_CARD',
  additionalServices: true,
  coachNumber: 'from scamper duh',
  seatNumber: 'that leading',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
