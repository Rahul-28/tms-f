import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPassenger, NewPassenger } from '../passenger.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPassenger for edit and NewPassengerFormGroupInput for create.
 */
type PassengerFormGroupInput = IPassenger | PartialWithRequiredKeyOf<NewPassenger>;

type PassengerFormDefaults = Pick<NewPassenger, 'id'>;

type PassengerFormGroupContent = {
  id: FormControl<IPassenger['id'] | NewPassenger['id']>;
  passengerName: FormControl<IPassenger['passengerName']>;
  age: FormControl<IPassenger['age']>;
  coachNumber: FormControl<IPassenger['coachNumber']>;
  seatNumber: FormControl<IPassenger['seatNumber']>;
  booking: FormControl<IPassenger['booking']>;
};

export type PassengerFormGroup = FormGroup<PassengerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PassengerFormService {
  createPassengerFormGroup(passenger: PassengerFormGroupInput = { id: null }): PassengerFormGroup {
    const passengerRawValue = {
      ...this.getFormDefaults(),
      ...passenger,
    };
    return new FormGroup<PassengerFormGroupContent>({
      id: new FormControl(
        { value: passengerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      passengerName: new FormControl(passengerRawValue.passengerName, {
        validators: [Validators.required],
      }),
      age: new FormControl(passengerRawValue.age, {
        validators: [Validators.required, Validators.min(1), Validators.max(120)],
      }),
      coachNumber: new FormControl(passengerRawValue.coachNumber, {
        validators: [Validators.required],
      }),
      seatNumber: new FormControl(passengerRawValue.seatNumber, {
        validators: [Validators.required],
      }),
      booking: new FormControl(passengerRawValue.booking),
    });
  }

  getPassenger(form: PassengerFormGroup): IPassenger | NewPassenger {
    return form.getRawValue() as IPassenger | NewPassenger;
  }

  resetForm(form: PassengerFormGroup, passenger: PassengerFormGroupInput): void {
    const passengerRawValue = { ...this.getFormDefaults(), ...passenger };
    form.reset(
      {
        ...passengerRawValue,
        id: { value: passengerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PassengerFormDefaults {
    return {
      id: null,
    };
  }
}
