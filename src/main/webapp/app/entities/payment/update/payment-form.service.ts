import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPayment, NewPayment } from '../payment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPayment for edit and NewPaymentFormGroupInput for create.
 */
type PaymentFormGroupInput = IPayment | PartialWithRequiredKeyOf<NewPayment>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPayment | NewPayment> = Omit<T, 'transactionDate'> & {
  transactionDate?: string | null;
};

type PaymentFormRawValue = FormValueOf<IPayment>;

type NewPaymentFormRawValue = FormValueOf<NewPayment>;

type PaymentFormDefaults = Pick<NewPayment, 'id' | 'transactionDate'>;

type PaymentFormGroupContent = {
  id: FormControl<PaymentFormRawValue['id'] | NewPayment['id']>;
  paymentId: FormControl<PaymentFormRawValue['paymentId']>;
  transactionId: FormControl<PaymentFormRawValue['transactionId']>;
  receiptNumber: FormControl<PaymentFormRawValue['receiptNumber']>;
  transactionDate: FormControl<PaymentFormRawValue['transactionDate']>;
  transactionType: FormControl<PaymentFormRawValue['transactionType']>;
  transactionAmount: FormControl<PaymentFormRawValue['transactionAmount']>;
  transactionStatus: FormControl<PaymentFormRawValue['transactionStatus']>;
  cardNumber: FormControl<PaymentFormRawValue['cardNumber']>;
  expiryDate: FormControl<PaymentFormRawValue['expiryDate']>;
  cvv: FormControl<PaymentFormRawValue['cvv']>;
  cardholderName: FormControl<PaymentFormRawValue['cardholderName']>;
  booking: FormControl<PaymentFormRawValue['booking']>;
};

export type PaymentFormGroup = FormGroup<PaymentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaymentFormService {
  createPaymentFormGroup(payment: PaymentFormGroupInput = { id: null }): PaymentFormGroup {
    const paymentRawValue = this.convertPaymentToPaymentRawValue({
      ...this.getFormDefaults(),
      ...payment,
    });
    return new FormGroup<PaymentFormGroupContent>({
      id: new FormControl(
        { value: paymentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      paymentId: new FormControl(paymentRawValue.paymentId, {
        validators: [Validators.required],
      }),
      transactionId: new FormControl(paymentRawValue.transactionId, {
        validators: [Validators.required],
      }),
      receiptNumber: new FormControl(paymentRawValue.receiptNumber, {
        validators: [Validators.required],
      }),
      transactionDate: new FormControl(paymentRawValue.transactionDate, {
        validators: [Validators.required],
      }),
      transactionType: new FormControl(paymentRawValue.transactionType, {
        validators: [Validators.required],
      }),
      transactionAmount: new FormControl(paymentRawValue.transactionAmount, {
        validators: [Validators.required],
      }),
      transactionStatus: new FormControl(paymentRawValue.transactionStatus, {
        validators: [Validators.required],
      }),
      cardNumber: new FormControl(paymentRawValue.cardNumber, {
        validators: [Validators.pattern('^[0-9]{16}$')],
      }),
      expiryDate: new FormControl(paymentRawValue.expiryDate),
      cvv: new FormControl(paymentRawValue.cvv, {
        validators: [Validators.pattern('^[0-9]{3}$')],
      }),
      cardholderName: new FormControl(paymentRawValue.cardholderName),
      booking: new FormControl(paymentRawValue.booking),
    });
  }

  getPayment(form: PaymentFormGroup): IPayment | NewPayment {
    return this.convertPaymentRawValueToPayment(form.getRawValue() as PaymentFormRawValue | NewPaymentFormRawValue);
  }

  resetForm(form: PaymentFormGroup, payment: PaymentFormGroupInput): void {
    const paymentRawValue = this.convertPaymentToPaymentRawValue({ ...this.getFormDefaults(), ...payment });
    form.reset(
      {
        ...paymentRawValue,
        id: { value: paymentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PaymentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      transactionDate: currentTime,
    };
  }

  private convertPaymentRawValueToPayment(rawPayment: PaymentFormRawValue | NewPaymentFormRawValue): IPayment | NewPayment {
    return {
      ...rawPayment,
      transactionDate: dayjs(rawPayment.transactionDate, DATE_TIME_FORMAT),
    };
  }

  private convertPaymentToPaymentRawValue(
    payment: IPayment | (Partial<NewPayment> & PaymentFormDefaults),
  ): PaymentFormRawValue | PartialWithRequiredKeyOf<NewPaymentFormRawValue> {
    return {
      ...payment,
      transactionDate: payment.transactionDate ? payment.transactionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
