import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITrain, NewTrain } from '../train.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrain for edit and NewTrainFormGroupInput for create.
 */
type TrainFormGroupInput = ITrain | PartialWithRequiredKeyOf<NewTrain>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITrain | NewTrain> = Omit<T, 'departureTime' | 'arrivalTime'> & {
  departureTime?: string | null;
  arrivalTime?: string | null;
};

type TrainFormRawValue = FormValueOf<ITrain>;

type NewTrainFormRawValue = FormValueOf<NewTrain>;

type TrainFormDefaults = Pick<NewTrain, 'id' | 'departureTime' | 'arrivalTime' | 'isActive'>;

type TrainFormGroupContent = {
  id: FormControl<TrainFormRawValue['id'] | NewTrain['id']>;
  trainNumber: FormControl<TrainFormRawValue['trainNumber']>;
  trainName: FormControl<TrainFormRawValue['trainName']>;
  origin: FormControl<TrainFormRawValue['origin']>;
  destination: FormControl<TrainFormRawValue['destination']>;
  intermediateStop: FormControl<TrainFormRawValue['intermediateStop']>;
  serviceStartDate: FormControl<TrainFormRawValue['serviceStartDate']>;
  serviceEndDate: FormControl<TrainFormRawValue['serviceEndDate']>;
  serviceType: FormControl<TrainFormRawValue['serviceType']>;
  departureTime: FormControl<TrainFormRawValue['departureTime']>;
  arrivalTime: FormControl<TrainFormRawValue['arrivalTime']>;
  basicPrice: FormControl<TrainFormRawValue['basicPrice']>;
  isActive: FormControl<TrainFormRawValue['isActive']>;
};

export type TrainFormGroup = FormGroup<TrainFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrainFormService {
  createTrainFormGroup(train: TrainFormGroupInput = { id: null }): TrainFormGroup {
    const trainRawValue = this.convertTrainToTrainRawValue({
      ...this.getFormDefaults(),
      ...train,
    });
    return new FormGroup<TrainFormGroupContent>({
      id: new FormControl(
        { value: trainRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      trainNumber: new FormControl(trainRawValue.trainNumber, {
        validators: [Validators.required],
      }),
      trainName: new FormControl(trainRawValue.trainName, {
        validators: [Validators.required],
      }),
      origin: new FormControl(trainRawValue.origin, {
        validators: [Validators.required],
      }),
      destination: new FormControl(trainRawValue.destination, {
        validators: [Validators.required],
      }),
      intermediateStop: new FormControl(trainRawValue.intermediateStop, {
        validators: [Validators.required],
      }),
      serviceStartDate: new FormControl(trainRawValue.serviceStartDate, {
        validators: [Validators.required],
      }),
      serviceEndDate: new FormControl(trainRawValue.serviceEndDate, {
        validators: [Validators.required],
      }),
      serviceType: new FormControl(trainRawValue.serviceType, {
        validators: [Validators.required],
      }),
      departureTime: new FormControl(trainRawValue.departureTime, {
        validators: [Validators.required],
      }),
      arrivalTime: new FormControl(trainRawValue.arrivalTime, {
        validators: [Validators.required],
      }),
      basicPrice: new FormControl(trainRawValue.basicPrice, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(trainRawValue.isActive, {
        validators: [Validators.required],
      }),
    });
  }

  getTrain(form: TrainFormGroup): ITrain | NewTrain {
    return this.convertTrainRawValueToTrain(form.getRawValue() as TrainFormRawValue | NewTrainFormRawValue);
  }

  resetForm(form: TrainFormGroup, train: TrainFormGroupInput): void {
    const trainRawValue = this.convertTrainToTrainRawValue({ ...this.getFormDefaults(), ...train });
    form.reset(
      {
        ...trainRawValue,
        id: { value: trainRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TrainFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      departureTime: currentTime,
      arrivalTime: currentTime,
      isActive: false,
    };
  }

  private convertTrainRawValueToTrain(rawTrain: TrainFormRawValue | NewTrainFormRawValue): ITrain | NewTrain {
    return {
      ...rawTrain,
      departureTime: dayjs(rawTrain.departureTime, DATE_TIME_FORMAT),
      arrivalTime: dayjs(rawTrain.arrivalTime, DATE_TIME_FORMAT),
    };
  }

  private convertTrainToTrainRawValue(
    train: ITrain | (Partial<NewTrain> & TrainFormDefaults),
  ): TrainFormRawValue | PartialWithRequiredKeyOf<NewTrainFormRawValue> {
    return {
      ...train,
      departureTime: train.departureTime ? train.departureTime.format(DATE_TIME_FORMAT) : undefined,
      arrivalTime: train.arrivalTime ? train.arrivalTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
