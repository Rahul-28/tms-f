import dayjs from 'dayjs/esm';

import { IPayment, NewPayment } from './payment.model';

export const sampleWithRequiredData: IPayment = {
  id: 4942,
  paymentId: 'on huddle',
  transactionId: 'after',
  receiptNumber: 'guilt shudder',
  transactionDate: dayjs('2025-07-11T14:13'),
  transactionType: 'CREDIT',
  transactionAmount: 1534.92,
  transactionStatus: 'PENDING',
};

export const sampleWithPartialData: IPayment = {
  id: 11071,
  paymentId: 'roughly boo abaft',
  transactionId: 'redact hunt',
  receiptNumber: 'oof throbbing astride',
  transactionDate: dayjs('2025-07-12T02:19'),
  transactionType: 'DEBIT',
  transactionAmount: 18319.26,
  transactionStatus: 'SUCCESS',
  expiryDate: 'ski',
};

export const sampleWithFullData: IPayment = {
  id: 28239,
  paymentId: 'hourly unwieldy',
  transactionId: 'because',
  receiptNumber: 'eke',
  transactionDate: dayjs('2025-07-11T07:47'),
  transactionType: 'CREDIT',
  transactionAmount: 9973.96,
  transactionStatus: 'SUCCESS',
  cardNumber: '8866586226400293',
  expiryDate: 'wherever whose though',
  cvv: '311',
  cardholderName: 'puritan haunting spotless',
};

export const sampleWithNewData: NewPayment = {
  paymentId: 'within',
  transactionId: 'hmph',
  receiptNumber: 'barring celebrated',
  transactionDate: dayjs('2025-07-11T08:58'),
  transactionType: 'CREDIT',
  transactionAmount: 28064.8,
  transactionStatus: 'FAILED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
