import { IBooking } from 'app/entities/booking/booking.model';

export interface IPassenger {
  id: number;
  passengerName?: string | null;
  age?: number | null;
  coachNumber?: string | null;
  seatNumber?: string | null;
  booking?: Pick<IBooking, 'id'> | null;
}

export type NewPassenger = Omit<IPassenger, 'id'> & { id: null };
