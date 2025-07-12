import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';
import { ITrain } from 'app/entities/train/train.model';
import { TrainService } from 'app/entities/train/service/train.service';
import { BookingStatus } from 'app/entities/enumerations/booking-status.model';
import { PaymentMode } from 'app/entities/enumerations/payment-mode.model';
import { BookingService } from '../service/booking.service';
import { IBooking } from '../booking.model';
import { BookingFormGroup, BookingFormService } from './booking-form.service';

@Component({
  selector: 'jhi-booking-update',
  templateUrl: './booking-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BookingUpdateComponent implements OnInit {
  isSaving = false;
  booking: IBooking | null = null;
  bookingStatusValues = Object.keys(BookingStatus);
  paymentModeValues = Object.keys(PaymentMode);

  customersSharedCollection: ICustomer[] = [];
  trainsSharedCollection: ITrain[] = [];

  protected bookingService = inject(BookingService);
  protected bookingFormService = inject(BookingFormService);
  protected customerService = inject(CustomerService);
  protected trainService = inject(TrainService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BookingFormGroup = this.bookingFormService.createBookingFormGroup();

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  compareTrain = (o1: ITrain | null, o2: ITrain | null): boolean => this.trainService.compareTrain(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ booking }) => {
      this.booking = booking;
      if (booking) {
        this.updateForm(booking);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const booking = this.bookingFormService.getBooking(this.editForm);
    if (booking.id !== null) {
      this.subscribeToSaveResponse(this.bookingService.update(booking));
    } else {
      this.subscribeToSaveResponse(this.bookingService.create(booking));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBooking>>): void {
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

  protected updateForm(booking: IBooking): void {
    this.booking = booking;
    this.bookingFormService.resetForm(this.editForm, booking);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      booking.customer,
    );
    this.trainsSharedCollection = this.trainService.addTrainToCollectionIfMissing<ITrain>(this.trainsSharedCollection, booking.train);
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.booking?.customer),
        ),
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));

    this.trainService
      .query()
      .pipe(map((res: HttpResponse<ITrain[]>) => res.body ?? []))
      .pipe(map((trains: ITrain[]) => this.trainService.addTrainToCollectionIfMissing<ITrain>(trains, this.booking?.train)))
      .subscribe((trains: ITrain[]) => (this.trainsSharedCollection = trains));
  }
}
