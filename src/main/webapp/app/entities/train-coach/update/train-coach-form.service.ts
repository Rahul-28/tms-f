import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITrainCoach, NewTrainCoach } from '../train-coach.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrainCoach for edit and NewTrainCoachFormGroupInput for create.
 */
type TrainCoachFormGroupInput = ITrainCoach | PartialWithRequiredKeyOf<NewTrainCoach>;

type TrainCoachFormDefaults = Pick<NewTrainCoach, 'id'>;

type TrainCoachFormGroupContent = {
  id: FormControl<ITrainCoach['id'] | NewTrainCoach['id']>;
  trainNumber: FormControl<ITrainCoach['trainNumber']>;
  seatCapacity: FormControl<ITrainCoach['seatCapacity']>;
  availableSeats: FormControl<ITrainCoach['availableSeats']>;
  farePrice: FormControl<ITrainCoach['farePrice']>;
  coachType: FormControl<ITrainCoach['coachType']>;
  train: FormControl<ITrainCoach['train']>;
};

export type TrainCoachFormGroup = FormGroup<TrainCoachFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrainCoachFormService {
  createTrainCoachFormGroup(trainCoach: TrainCoachFormGroupInput = { id: null }): TrainCoachFormGroup {
    const trainCoachRawValue = {
      ...this.getFormDefaults(),
      ...trainCoach,
    };
    return new FormGroup<TrainCoachFormGroupContent>({
      id: new FormControl(
        { value: trainCoachRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      trainNumber: new FormControl(trainCoachRawValue.trainNumber, {
        validators: [Validators.required],
      }),
      seatCapacity: new FormControl(trainCoachRawValue.seatCapacity, {
        validators: [Validators.required],
      }),
      availableSeats: new FormControl(trainCoachRawValue.availableSeats, {
        validators: [Validators.required],
      }),
      farePrice: new FormControl(trainCoachRawValue.farePrice, {
        validators: [Validators.required],
      }),
      coachType: new FormControl(trainCoachRawValue.coachType),
      train: new FormControl(trainCoachRawValue.train),
    });
  }

  getTrainCoach(form: TrainCoachFormGroup): ITrainCoach | NewTrainCoach {
    return form.getRawValue() as ITrainCoach | NewTrainCoach;
  }

  resetForm(form: TrainCoachFormGroup, trainCoach: TrainCoachFormGroupInput): void {
    const trainCoachRawValue = { ...this.getFormDefaults(), ...trainCoach };
    form.reset(
      {
        ...trainCoachRawValue,
        id: { value: trainCoachRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TrainCoachFormDefaults {
    return {
      id: null,
    };
  }
}
