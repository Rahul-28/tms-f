import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 29050,
  invoiceNumber: 'search why',
  paymentId: 'secularize whose exacerbate',
  transactionId: 'usefully of',
  receiptNumber: 'fooey stealthily',
  invoiceDate: dayjs('2025-07-12T01:06'),
  transactionType: 'DEBIT',
  transactionAmount: 18449.91,
  transactionStatus: 'FAILED',
  customerDetails: 'so beard oof',
};

export const sampleWithPartialData: IInvoice = {
  id: 18304,
  invoiceNumber: 'softly',
  paymentId: 'verve er',
  transactionId: 'perky however',
  receiptNumber: 'tomorrow angrily',
  invoiceDate: dayjs('2025-07-12T04:41'),
  transactionType: 'CREDIT',
  transactionAmount: 27495.9,
  transactionStatus: 'PENDING',
  customerDetails: 'kaleidoscopic',
};

export const sampleWithFullData: IInvoice = {
  id: 19628,
  invoiceNumber: 'yesterday times',
  paymentId: 'on',
  transactionId: 'shampoo uh-huh gorgeous',
  receiptNumber: 'concerned meanwhile',
  invoiceDate: dayjs('2025-07-11T20:59'),
  transactionType: 'CREDIT',
  transactionAmount: 3389.42,
  transactionStatus: 'SUCCESS',
  customerDetails: 'indeed',
};

export const sampleWithNewData: NewInvoice = {
  invoiceNumber: 'within about',
  paymentId: 'forenenst apud',
  transactionId: 'partially helpfully',
  receiptNumber: 'steeple reproachfully whoa',
  invoiceDate: dayjs('2025-07-11T23:39'),
  transactionType: 'DEBIT',
  transactionAmount: 1458.94,
  transactionStatus: 'SUCCESS',
  customerDetails: 'aha uh-huh',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
