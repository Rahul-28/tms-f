import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICoachType, NewCoachType } from '../coach-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICoachType for edit and NewCoachTypeFormGroupInput for create.
 */
type CoachTypeFormGroupInput = ICoachType | PartialWithRequiredKeyOf<NewCoachType>;

type CoachTypeFormDefaults = Pick<NewCoachType, 'id'>;

type CoachTypeFormGroupContent = {
  id: FormControl<ICoachType['id'] | NewCoachType['id']>;
  coachId: FormControl<ICoachType['coachId']>;
  coachName: FormControl<ICoachType['coachName']>;
  seatCapacity: FormControl<ICoachType['seatCapacity']>;
  fareMultiplier: FormControl<ICoachType['fareMultiplier']>;
};

export type CoachTypeFormGroup = FormGroup<CoachTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CoachTypeFormService {
  createCoachTypeFormGroup(coachType: CoachTypeFormGroupInput = { id: null }): CoachTypeFormGroup {
    const coachTypeRawValue = {
      ...this.getFormDefaults(),
      ...coachType,
    };
    return new FormGroup<CoachTypeFormGroupContent>({
      id: new FormControl(
        { value: coachTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      coachId: new FormControl(coachTypeRawValue.coachId, {
        validators: [Validators.required],
      }),
      coachName: new FormControl(coachTypeRawValue.coachName, {
        validators: [Validators.required],
      }),
      seatCapacity: new FormControl(coachTypeRawValue.seatCapacity, {
        validators: [Validators.required],
      }),
      fareMultiplier: new FormControl(coachTypeRawValue.fareMultiplier, {
        validators: [Validators.required],
      }),
    });
  }

  getCoachType(form: CoachTypeFormGroup): ICoachType | NewCoachType {
    return form.getRawValue() as ICoachType | NewCoachType;
  }

  resetForm(form: CoachTypeFormGroup, coachType: CoachTypeFormGroupInput): void {
    const coachTypeRawValue = { ...this.getFormDefaults(), ...coachType };
    form.reset(
      {
        ...coachTypeRawValue,
        id: { value: coachTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CoachTypeFormDefaults {
    return {
      id: null,
    };
  }
}
