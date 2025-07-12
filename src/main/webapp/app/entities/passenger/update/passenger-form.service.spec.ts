import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../passenger.test-samples';

import { PassengerFormService } from './passenger-form.service';

describe('Passenger Form Service', () => {
  let service: PassengerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PassengerFormService);
  });

  describe('Service methods', () => {
    describe('createPassengerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPassengerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            passengerName: expect.any(Object),
            age: expect.any(Object),
            coachNumber: expect.any(Object),
            seatNumber: expect.any(Object),
            booking: expect.any(Object),
          }),
        );
      });

      it('passing IPassenger should create a new form with FormGroup', () => {
        const formGroup = service.createPassengerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            passengerName: expect.any(Object),
            age: expect.any(Object),
            coachNumber: expect.any(Object),
            seatNumber: expect.any(Object),
            booking: expect.any(Object),
          }),
        );
      });
    });

    describe('getPassenger', () => {
      it('should return NewPassenger for default Passenger initial value', () => {
        const formGroup = service.createPassengerFormGroup(sampleWithNewData);

        const passenger = service.getPassenger(formGroup) as any;

        expect(passenger).toMatchObject(sampleWithNewData);
      });

      it('should return NewPassenger for empty Passenger initial value', () => {
        const formGroup = service.createPassengerFormGroup();

        const passenger = service.getPassenger(formGroup) as any;

        expect(passenger).toMatchObject({});
      });

      it('should return IPassenger', () => {
        const formGroup = service.createPassengerFormGroup(sampleWithRequiredData);

        const passenger = service.getPassenger(formGroup) as any;

        expect(passenger).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPassenger should not enable id FormControl', () => {
        const formGroup = service.createPassengerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPassenger should disable id FormControl', () => {
        const formGroup = service.createPassengerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
