import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../train-coach.test-samples';

import { TrainCoachFormService } from './train-coach-form.service';

describe('TrainCoach Form Service', () => {
  let service: TrainCoachFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrainCoachFormService);
  });

  describe('Service methods', () => {
    describe('createTrainCoachFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrainCoachFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            trainNumber: expect.any(Object),
            seatCapacity: expect.any(Object),
            availableSeats: expect.any(Object),
            farePrice: expect.any(Object),
            coachType: expect.any(Object),
            train: expect.any(Object),
          }),
        );
      });

      it('passing ITrainCoach should create a new form with FormGroup', () => {
        const formGroup = service.createTrainCoachFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            trainNumber: expect.any(Object),
            seatCapacity: expect.any(Object),
            availableSeats: expect.any(Object),
            farePrice: expect.any(Object),
            coachType: expect.any(Object),
            train: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrainCoach', () => {
      it('should return NewTrainCoach for default TrainCoach initial value', () => {
        const formGroup = service.createTrainCoachFormGroup(sampleWithNewData);

        const trainCoach = service.getTrainCoach(formGroup) as any;

        expect(trainCoach).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrainCoach for empty TrainCoach initial value', () => {
        const formGroup = service.createTrainCoachFormGroup();

        const trainCoach = service.getTrainCoach(formGroup) as any;

        expect(trainCoach).toMatchObject({});
      });

      it('should return ITrainCoach', () => {
        const formGroup = service.createTrainCoachFormGroup(sampleWithRequiredData);

        const trainCoach = service.getTrainCoach(formGroup) as any;

        expect(trainCoach).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrainCoach should not enable id FormControl', () => {
        const formGroup = service.createTrainCoachFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrainCoach should disable id FormControl', () => {
        const formGroup = service.createTrainCoachFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
