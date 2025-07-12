import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../coach-type.test-samples';

import { CoachTypeFormService } from './coach-type-form.service';

describe('CoachType Form Service', () => {
  let service: CoachTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CoachTypeFormService);
  });

  describe('Service methods', () => {
    describe('createCoachTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCoachTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            coachId: expect.any(Object),
            coachName: expect.any(Object),
            seatCapacity: expect.any(Object),
            fareMultiplier: expect.any(Object),
          }),
        );
      });

      it('passing ICoachType should create a new form with FormGroup', () => {
        const formGroup = service.createCoachTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            coachId: expect.any(Object),
            coachName: expect.any(Object),
            seatCapacity: expect.any(Object),
            fareMultiplier: expect.any(Object),
          }),
        );
      });
    });

    describe('getCoachType', () => {
      it('should return NewCoachType for default CoachType initial value', () => {
        const formGroup = service.createCoachTypeFormGroup(sampleWithNewData);

        const coachType = service.getCoachType(formGroup) as any;

        expect(coachType).toMatchObject(sampleWithNewData);
      });

      it('should return NewCoachType for empty CoachType initial value', () => {
        const formGroup = service.createCoachTypeFormGroup();

        const coachType = service.getCoachType(formGroup) as any;

        expect(coachType).toMatchObject({});
      });

      it('should return ICoachType', () => {
        const formGroup = service.createCoachTypeFormGroup(sampleWithRequiredData);

        const coachType = service.getCoachType(formGroup) as any;

        expect(coachType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICoachType should not enable id FormControl', () => {
        const formGroup = service.createCoachTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCoachType should disable id FormControl', () => {
        const formGroup = service.createCoachTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
