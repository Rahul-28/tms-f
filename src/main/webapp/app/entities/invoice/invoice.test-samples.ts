import dayjs from 'dayjs/esm';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 29050,
  invoiceNumber: 'search why',
  transactionId: 'secularize whose exacerbate',
  receiptNumber: 'usefully of',
  invoiceDate: dayjs('2025-07-11T21:40'),
  transactionType: 'CREDIT',
  transactionAmount: 4229.05,
  transactionStatus: 'FAILED',
  customerDetails: 'gee muddy',
};

export const sampleWithPartialData: IInvoice = {
  id: 18304,
  invoiceNumber: 'softly',
  transactionId: 'verve er',
  receiptNumber: 'perky however',
  invoiceDate: dayjs('2025-07-11T18:23'),
  transactionType: 'CREDIT',
  transactionAmount: 1557.13,
  transactionStatus: 'FAILED',
  customerDetails: 'and',
};

export const sampleWithFullData: IInvoice = {
  id: 19628,
  invoiceNumber: 'yesterday times',
  transactionId: 'on',
  receiptNumber: 'shampoo uh-huh gorgeous',
  invoiceDate: dayjs('2025-07-11T17:38'),
  transactionType: 'DEBIT',
  transactionAmount: 9832.22,
  transactionStatus: 'PENDING',
  customerDetails: 'hmph brr frightfully',
};

export const sampleWithNewData: NewInvoice = {
  invoiceNumber: 'within about',
  transactionId: 'forenenst apud',
  receiptNumber: 'partially helpfully',
  invoiceDate: dayjs('2025-07-12T05:17'),
  transactionType: 'DEBIT',
  transactionAmount: 18558.78,
  transactionStatus: 'SUCCESS',
  customerDetails: 'duh lest fatally',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
