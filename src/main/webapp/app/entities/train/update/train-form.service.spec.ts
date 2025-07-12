import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../train.test-samples';

import { TrainFormService } from './train-form.service';

describe('Train Form Service', () => {
  let service: TrainFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrainFormService);
  });

  describe('Service methods', () => {
    describe('createTrainFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrainFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            trainNumber: expect.any(Object),
            trainName: expect.any(Object),
            origin: expect.any(Object),
            destination: expect.any(Object),
            intermediateStop: expect.any(Object),
            serviceStartDate: expect.any(Object),
            serviceEndDate: expect.any(Object),
            serviceType: expect.any(Object),
            departureTime: expect.any(Object),
            arrivalTime: expect.any(Object),
            basicPrice: expect.any(Object),
            isActive: expect.any(Object),
          }),
        );
      });

      it('passing ITrain should create a new form with FormGroup', () => {
        const formGroup = service.createTrainFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            trainNumber: expect.any(Object),
            trainName: expect.any(Object),
            origin: expect.any(Object),
            destination: expect.any(Object),
            intermediateStop: expect.any(Object),
            serviceStartDate: expect.any(Object),
            serviceEndDate: expect.any(Object),
            serviceType: expect.any(Object),
            departureTime: expect.any(Object),
            arrivalTime: expect.any(Object),
            basicPrice: expect.any(Object),
            isActive: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrain', () => {
      it('should return NewTrain for default Train initial value', () => {
        const formGroup = service.createTrainFormGroup(sampleWithNewData);

        const train = service.getTrain(formGroup) as any;

        expect(train).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrain for empty Train initial value', () => {
        const formGroup = service.createTrainFormGroup();

        const train = service.getTrain(formGroup) as any;

        expect(train).toMatchObject({});
      });

      it('should return ITrain', () => {
        const formGroup = service.createTrainFormGroup(sampleWithRequiredData);

        const train = service.getTrain(formGroup) as any;

        expect(train).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrain should not enable id FormControl', () => {
        const formGroup = service.createTrainFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrain should disable id FormControl', () => {
        const formGroup = service.createTrainFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
