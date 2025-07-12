import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoice, NewInvoice } from '../invoice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoice for edit and NewInvoiceFormGroupInput for create.
 */
type InvoiceFormGroupInput = IInvoice | PartialWithRequiredKeyOf<NewInvoice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInvoice | NewInvoice> = Omit<T, 'invoiceDate'> & {
  invoiceDate?: string | null;
};

type InvoiceFormRawValue = FormValueOf<IInvoice>;

type NewInvoiceFormRawValue = FormValueOf<NewInvoice>;

type InvoiceFormDefaults = Pick<NewInvoice, 'id' | 'invoiceDate'>;

type InvoiceFormGroupContent = {
  id: FormControl<InvoiceFormRawValue['id'] | NewInvoice['id']>;
  invoiceNumber: FormControl<InvoiceFormRawValue['invoiceNumber']>;
  paymentId: FormControl<InvoiceFormRawValue['paymentId']>;
  transactionId: FormControl<InvoiceFormRawValue['transactionId']>;
  receiptNumber: FormControl<InvoiceFormRawValue['receiptNumber']>;
  invoiceDate: FormControl<InvoiceFormRawValue['invoiceDate']>;
  transactionType: FormControl<InvoiceFormRawValue['transactionType']>;
  transactionAmount: FormControl<InvoiceFormRawValue['transactionAmount']>;
  transactionStatus: FormControl<InvoiceFormRawValue['transactionStatus']>;
  customerDetails: FormControl<InvoiceFormRawValue['customerDetails']>;
  payment: FormControl<InvoiceFormRawValue['payment']>;
};

export type InvoiceFormGroup = FormGroup<InvoiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceFormService {
  createInvoiceFormGroup(invoice: InvoiceFormGroupInput = { id: null }): InvoiceFormGroup {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({
      ...this.getFormDefaults(),
      ...invoice,
    });
    return new FormGroup<InvoiceFormGroupContent>({
      id: new FormControl(
        { value: invoiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      invoiceNumber: new FormControl(invoiceRawValue.invoiceNumber, {
        validators: [Validators.required],
      }),
      paymentId: new FormControl(invoiceRawValue.paymentId, {
        validators: [Validators.required],
      }),
      transactionId: new FormControl(invoiceRawValue.transactionId, {
        validators: [Validators.required],
      }),
      receiptNumber: new FormControl(invoiceRawValue.receiptNumber, {
        validators: [Validators.required],
      }),
      invoiceDate: new FormControl(invoiceRawValue.invoiceDate, {
        validators: [Validators.required],
      }),
      transactionType: new FormControl(invoiceRawValue.transactionType, {
        validators: [Validators.required],
      }),
      transactionAmount: new FormControl(invoiceRawValue.transactionAmount, {
        validators: [Validators.required],
      }),
      transactionStatus: new FormControl(invoiceRawValue.transactionStatus, {
        validators: [Validators.required],
      }),
      customerDetails: new FormControl(invoiceRawValue.customerDetails, {
        validators: [Validators.required],
      }),
      payment: new FormControl(invoiceRawValue.payment),
    });
  }

  getInvoice(form: InvoiceFormGroup): IInvoice | NewInvoice {
    return this.convertInvoiceRawValueToInvoice(form.getRawValue() as InvoiceFormRawValue | NewInvoiceFormRawValue);
  }

  resetForm(form: InvoiceFormGroup, invoice: InvoiceFormGroupInput): void {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({ ...this.getFormDefaults(), ...invoice });
    form.reset(
      {
        ...invoiceRawValue,
        id: { value: invoiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InvoiceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      invoiceDate: currentTime,
    };
  }

  private convertInvoiceRawValueToInvoice(rawInvoice: InvoiceFormRawValue | NewInvoiceFormRawValue): IInvoice | NewInvoice {
    return {
      ...rawInvoice,
      invoiceDate: dayjs(rawInvoice.invoiceDate, DATE_TIME_FORMAT),
    };
  }

  private convertInvoiceToInvoiceRawValue(
    invoice: IInvoice | (Partial<NewInvoice> & InvoiceFormDefaults),
  ): InvoiceFormRawValue | PartialWithRequiredKeyOf<NewInvoiceFormRawValue> {
    return {
      ...invoice,
      invoiceDate: invoice.invoiceDate ? invoice.invoiceDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
