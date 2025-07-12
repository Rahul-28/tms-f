import dayjs from 'dayjs/esm';
import { IBooking } from 'app/entities/booking/booking.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { TransactionStatus } from 'app/entities/enumerations/transaction-status.model';

export interface IPayment {
  id: number;
  paymentId?: string | null;
  transactionId?: string | null;
  receiptNumber?: string | null;
  transactionDate?: dayjs.Dayjs | null;
  transactionType?: keyof typeof TransactionType | null;
  transactionAmount?: number | null;
  transactionStatus?: keyof typeof TransactionStatus | null;
  cardNumber?: string | null;
  expiryDate?: string | null;
  cvv?: string | null;
  cardholderName?: string | null;
  booking?: Pick<IBooking, 'id'> | null;
}

export type NewPayment = Omit<IPayment, 'id'> & { id: null };
