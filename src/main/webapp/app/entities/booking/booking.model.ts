import dayjs from 'dayjs/esm';
import { ICustomer } from 'app/entities/customer/customer.model';
import { ITrain } from 'app/entities/train/train.model';
import { BookingStatus } from 'app/entities/enumerations/booking-status.model';
import { PaymentMode } from 'app/entities/enumerations/payment-mode.model';

export interface IBooking {
  id: number;
  pnrNumber?: string | null;
  bookingDate?: dayjs.Dayjs | null;
  travellingDate?: dayjs.Dayjs | null;
  boardingStation?: string | null;
  destinationStation?: string | null;
  boardingTime?: dayjs.Dayjs | null;
  arrivalTime?: dayjs.Dayjs | null;
  totalFare?: number | null;
  bookingStatus?: keyof typeof BookingStatus | null;
  modeOfPayment?: keyof typeof PaymentMode | null;
  additionalServices?: boolean | null;
  coachNumber?: string | null;
  seatNumber?: string | null;
  customer?: Pick<ICustomer, 'id'> | null;
  train?: Pick<ITrain, 'id'> | null;
}

export type NewBooking = Omit<IBooking, 'id'> & { id: null };
