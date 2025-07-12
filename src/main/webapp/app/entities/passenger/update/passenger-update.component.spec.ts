import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IBooking } from 'app/entities/booking/booking.model';
import { BookingService } from 'app/entities/booking/service/booking.service';
import { PassengerService } from '../service/passenger.service';
import { IPassenger } from '../passenger.model';
import { PassengerFormService } from './passenger-form.service';

import { PassengerUpdateComponent } from './passenger-update.component';

describe('Passenger Management Update Component', () => {
  let comp: PassengerUpdateComponent;
  let fixture: ComponentFixture<PassengerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let passengerFormService: PassengerFormService;
  let passengerService: PassengerService;
  let bookingService: BookingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PassengerUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PassengerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PassengerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    passengerFormService = TestBed.inject(PassengerFormService);
    passengerService = TestBed.inject(PassengerService);
    bookingService = TestBed.inject(BookingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Booking query and add missing value', () => {
      const passenger: IPassenger = { id: 30166 };
      const booking: IBooking = { id: 1408 };
      passenger.booking = booking;

      const bookingCollection: IBooking[] = [{ id: 1408 }];
      jest.spyOn(bookingService, 'query').mockReturnValue(of(new HttpResponse({ body: bookingCollection })));
      const additionalBookings = [booking];
      const expectedCollection: IBooking[] = [...additionalBookings, ...bookingCollection];
      jest.spyOn(bookingService, 'addBookingToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ passenger });
      comp.ngOnInit();

      expect(bookingService.query).toHaveBeenCalled();
      expect(bookingService.addBookingToCollectionIfMissing).toHaveBeenCalledWith(
        bookingCollection,
        ...additionalBookings.map(expect.objectContaining),
      );
      expect(comp.bookingsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const passenger: IPassenger = { id: 30166 };
      const booking: IBooking = { id: 1408 };
      passenger.booking = booking;

      activatedRoute.data = of({ passenger });
      comp.ngOnInit();

      expect(comp.bookingsSharedCollection).toContainEqual(booking);
      expect(comp.passenger).toEqual(passenger);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPassenger>>();
      const passenger = { id: 22165 };
      jest.spyOn(passengerFormService, 'getPassenger').mockReturnValue(passenger);
      jest.spyOn(passengerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ passenger });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: passenger }));
      saveSubject.complete();

      // THEN
      expect(passengerFormService.getPassenger).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(passengerService.update).toHaveBeenCalledWith(expect.objectContaining(passenger));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPassenger>>();
      const passenger = { id: 22165 };
      jest.spyOn(passengerFormService, 'getPassenger').mockReturnValue({ id: null });
      jest.spyOn(passengerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ passenger: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: passenger }));
      saveSubject.complete();

      // THEN
      expect(passengerFormService.getPassenger).toHaveBeenCalled();
      expect(passengerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPassenger>>();
      const passenger = { id: 22165 };
      jest.spyOn(passengerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ passenger });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(passengerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBooking', () => {
      it('should forward to bookingService', () => {
        const entity = { id: 1408 };
        const entity2 = { id: 4697 };
        jest.spyOn(bookingService, 'compareBooking');
        comp.compareBooking(entity, entity2);
        expect(bookingService.compareBooking).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
