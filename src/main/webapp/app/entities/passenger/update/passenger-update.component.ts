import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IBooking } from 'app/entities/booking/booking.model';
import { BookingService } from 'app/entities/booking/service/booking.service';
import { IPassenger } from '../passenger.model';
import { PassengerService } from '../service/passenger.service';
import { PassengerFormGroup, PassengerFormService } from './passenger-form.service';

@Component({
  selector: 'jhi-passenger-update',
  templateUrl: './passenger-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PassengerUpdateComponent implements OnInit {
  isSaving = false;
  passenger: IPassenger | null = null;

  bookingsSharedCollection: IBooking[] = [];

  protected passengerService = inject(PassengerService);
  protected passengerFormService = inject(PassengerFormService);
  protected bookingService = inject(BookingService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PassengerFormGroup = this.passengerFormService.createPassengerFormGroup();

  compareBooking = (o1: IBooking | null, o2: IBooking | null): boolean => this.bookingService.compareBooking(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ passenger }) => {
      this.passenger = passenger;
      if (passenger) {
        this.updateForm(passenger);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const passenger = this.passengerFormService.getPassenger(this.editForm);
    if (passenger.id !== null) {
      this.subscribeToSaveResponse(this.passengerService.update(passenger));
    } else {
      this.subscribeToSaveResponse(this.passengerService.create(passenger));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPassenger>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(passenger: IPassenger): void {
    this.passenger = passenger;
    this.passengerFormService.resetForm(this.editForm, passenger);

    this.bookingsSharedCollection = this.bookingService.addBookingToCollectionIfMissing<IBooking>(
      this.bookingsSharedCollection,
      passenger.booking,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bookingService
      .query()
      .pipe(map((res: HttpResponse<IBooking[]>) => res.body ?? []))
      .pipe(map((bookings: IBooking[]) => this.bookingService.addBookingToCollectionIfMissing<IBooking>(bookings, this.passenger?.booking)))
      .subscribe((bookings: IBooking[]) => (this.bookingsSharedCollection = bookings));
  }
}
