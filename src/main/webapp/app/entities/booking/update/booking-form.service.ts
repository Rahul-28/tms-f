import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBooking, NewBooking } from '../booking.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBooking for edit and NewBookingFormGroupInput for create.
 */
type BookingFormGroupInput = IBooking | PartialWithRequiredKeyOf<NewBooking>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBooking | NewBooking> = Omit<T, 'bookingDate' | 'boardingTime' | 'arrivalTime'> & {
  bookingDate?: string | null;
  boardingTime?: string | null;
  arrivalTime?: string | null;
};

type BookingFormRawValue = FormValueOf<IBooking>;

type NewBookingFormRawValue = FormValueOf<NewBooking>;

type BookingFormDefaults = Pick<NewBooking, 'id' | 'bookingDate' | 'boardingTime' | 'arrivalTime' | 'additionalServices'>;

type BookingFormGroupContent = {
  id: FormControl<BookingFormRawValue['id'] | NewBooking['id']>;
  pnrNumber: FormControl<BookingFormRawValue['pnrNumber']>;
  bookingDate: FormControl<BookingFormRawValue['bookingDate']>;
  travellingDate: FormControl<BookingFormRawValue['travellingDate']>;
  boardingStation: FormControl<BookingFormRawValue['boardingStation']>;
  destinationStation: FormControl<BookingFormRawValue['destinationStation']>;
  boardingTime: FormControl<BookingFormRawValue['boardingTime']>;
  arrivalTime: FormControl<BookingFormRawValue['arrivalTime']>;
  totalFare: FormControl<BookingFormRawValue['totalFare']>;
  bookingStatus: FormControl<BookingFormRawValue['bookingStatus']>;
  modeOfPayment: FormControl<BookingFormRawValue['modeOfPayment']>;
  additionalServices: FormControl<BookingFormRawValue['additionalServices']>;
  coachNumber: FormControl<BookingFormRawValue['coachNumber']>;
  seatNumber: FormControl<BookingFormRawValue['seatNumber']>;
  customer: FormControl<BookingFormRawValue['customer']>;
  train: FormControl<BookingFormRawValue['train']>;
};

export type BookingFormGroup = FormGroup<BookingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BookingFormService {
  createBookingFormGroup(booking: BookingFormGroupInput = { id: null }): BookingFormGroup {
    const bookingRawValue = this.convertBookingToBookingRawValue({
      ...this.getFormDefaults(),
      ...booking,
    });
    return new FormGroup<BookingFormGroupContent>({
      id: new FormControl(
        { value: bookingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      pnrNumber: new FormControl(bookingRawValue.pnrNumber, {
        validators: [Validators.required],
      }),
      bookingDate: new FormControl(bookingRawValue.bookingDate, {
        validators: [Validators.required],
      }),
      travellingDate: new FormControl(bookingRawValue.travellingDate, {
        validators: [Validators.required],
      }),
      boardingStation: new FormControl(bookingRawValue.boardingStation, {
        validators: [Validators.required],
      }),
      destinationStation: new FormControl(bookingRawValue.destinationStation, {
        validators: [Validators.required],
      }),
      boardingTime: new FormControl(bookingRawValue.boardingTime, {
        validators: [Validators.required],
      }),
      arrivalTime: new FormControl(bookingRawValue.arrivalTime, {
        validators: [Validators.required],
      }),
      totalFare: new FormControl(bookingRawValue.totalFare, {
        validators: [Validators.required],
      }),
      bookingStatus: new FormControl(bookingRawValue.bookingStatus, {
        validators: [Validators.required],
      }),
      modeOfPayment: new FormControl(bookingRawValue.modeOfPayment, {
        validators: [Validators.required],
      }),
      additionalServices: new FormControl(bookingRawValue.additionalServices, {
        validators: [Validators.required],
      }),
      coachNumber: new FormControl(bookingRawValue.coachNumber, {
        validators: [Validators.required],
      }),
      seatNumber: new FormControl(bookingRawValue.seatNumber, {
        validators: [Validators.required],
      }),
      customer: new FormControl(bookingRawValue.customer),
      train: new FormControl(bookingRawValue.train),
    });
  }

  getBooking(form: BookingFormGroup): IBooking | NewBooking {
    return this.convertBookingRawValueToBooking(form.getRawValue() as BookingFormRawValue | NewBookingFormRawValue);
  }

  resetForm(form: BookingFormGroup, booking: BookingFormGroupInput): void {
    const bookingRawValue = this.convertBookingToBookingRawValue({ ...this.getFormDefaults(), ...booking });
    form.reset(
      {
        ...bookingRawValue,
        id: { value: bookingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BookingFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      bookingDate: currentTime,
      boardingTime: currentTime,
      arrivalTime: currentTime,
      additionalServices: false,
    };
  }

  private convertBookingRawValueToBooking(rawBooking: BookingFormRawValue | NewBookingFormRawValue): IBooking | NewBooking {
    return {
      ...rawBooking,
      bookingDate: dayjs(rawBooking.bookingDate, DATE_TIME_FORMAT),
      boardingTime: dayjs(rawBooking.boardingTime, DATE_TIME_FORMAT),
      arrivalTime: dayjs(rawBooking.arrivalTime, DATE_TIME_FORMAT),
    };
  }

  private convertBookingToBookingRawValue(
    booking: IBooking | (Partial<NewBooking> & BookingFormDefaults),
  ): BookingFormRawValue | PartialWithRequiredKeyOf<NewBookingFormRawValue> {
    return {
      ...booking,
      bookingDate: booking.bookingDate ? booking.bookingDate.format(DATE_TIME_FORMAT) : undefined,
      boardingTime: booking.boardingTime ? booking.boardingTime.format(DATE_TIME_FORMAT) : undefined,
      arrivalTime: booking.arrivalTime ? booking.arrivalTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
