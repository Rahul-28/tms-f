import dayjs from 'dayjs/esm';
import { IPayment } from 'app/entities/payment/payment.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { TransactionStatus } from 'app/entities/enumerations/transaction-status.model';

export interface IInvoice {
  id: number;
  invoiceNumber?: string | null;
  transactionId?: string | null;
  receiptNumber?: string | null;
  invoiceDate?: dayjs.Dayjs | null;
  transactionType?: keyof typeof TransactionType | null;
  transactionAmount?: number | null;
  transactionStatus?: keyof typeof TransactionStatus | null;
  customerDetails?: string | null;
  payment?: Pick<IPayment, 'id'> | null;
}

export type NewInvoice = Omit<IInvoice, 'id'> & { id: null };
