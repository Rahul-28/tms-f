import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICustomer, NewCustomer } from '../customer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICustomer for edit and NewCustomerFormGroupInput for create.
 */
type CustomerFormGroupInput = ICustomer | PartialWithRequiredKeyOf<NewCustomer>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICustomer | NewCustomer> = Omit<T, 'registrationDate'> & {
  registrationDate?: string | null;
};

type CustomerFormRawValue = FormValueOf<ICustomer>;

type NewCustomerFormRawValue = FormValueOf<NewCustomer>;

type CustomerFormDefaults = Pick<NewCustomer, 'id' | 'registrationDate' | 'isActive'>;

type CustomerFormGroupContent = {
  id: FormControl<CustomerFormRawValue['id'] | NewCustomer['id']>;
  customerId: FormControl<CustomerFormRawValue['customerId']>;
  username: FormControl<CustomerFormRawValue['username']>;
  password: FormControl<CustomerFormRawValue['password']>;
  email: FormControl<CustomerFormRawValue['email']>;
  mobileNumber: FormControl<CustomerFormRawValue['mobileNumber']>;
  aadhaarNumber: FormControl<CustomerFormRawValue['aadhaarNumber']>;
  address: FormControl<CustomerFormRawValue['address']>;
  contactInformation: FormControl<CustomerFormRawValue['contactInformation']>;
  registrationDate: FormControl<CustomerFormRawValue['registrationDate']>;
  isActive: FormControl<CustomerFormRawValue['isActive']>;
};

export type CustomerFormGroup = FormGroup<CustomerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CustomerFormService {
  createCustomerFormGroup(customer: CustomerFormGroupInput = { id: null }): CustomerFormGroup {
    const customerRawValue = this.convertCustomerToCustomerRawValue({
      ...this.getFormDefaults(),
      ...customer,
    });
    return new FormGroup<CustomerFormGroupContent>({
      id: new FormControl(
        { value: customerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      customerId: new FormControl(customerRawValue.customerId, {
        validators: [Validators.required],
      }),
      username: new FormControl(customerRawValue.username, {
        validators: [Validators.required, Validators.minLength(3), Validators.pattern('^[a-zA-Z]+$')],
      }),
      password: new FormControl(customerRawValue.password, {
        validators: [Validators.required, Validators.minLength(8)],
      }),
      email: new FormControl(customerRawValue.email, {
        validators: [Validators.required, Validators.pattern('^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$')],
      }),
      mobileNumber: new FormControl(customerRawValue.mobileNumber, {
        validators: [Validators.required, Validators.pattern('^[0-9]{10}$')],
      }),
      aadhaarNumber: new FormControl(customerRawValue.aadhaarNumber, {
        validators: [Validators.required, Validators.pattern('^[0-9]{12}$')],
      }),
      address: new FormControl(customerRawValue.address),
      contactInformation: new FormControl(customerRawValue.contactInformation),
      registrationDate: new FormControl(customerRawValue.registrationDate),
      isActive: new FormControl(customerRawValue.isActive, {
        validators: [Validators.required],
      }),
    });
  }

  getCustomer(form: CustomerFormGroup): ICustomer | NewCustomer {
    return this.convertCustomerRawValueToCustomer(form.getRawValue() as CustomerFormRawValue | NewCustomerFormRawValue);
  }

  resetForm(form: CustomerFormGroup, customer: CustomerFormGroupInput): void {
    const customerRawValue = this.convertCustomerToCustomerRawValue({ ...this.getFormDefaults(), ...customer });
    form.reset(
      {
        ...customerRawValue,
        id: { value: customerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CustomerFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      registrationDate: currentTime,
      isActive: false,
    };
  }

  private convertCustomerRawValueToCustomer(rawCustomer: CustomerFormRawValue | NewCustomerFormRawValue): ICustomer | NewCustomer {
    return {
      ...rawCustomer,
      registrationDate: dayjs(rawCustomer.registrationDate, DATE_TIME_FORMAT),
    };
  }

  private convertCustomerToCustomerRawValue(
    customer: ICustomer | (Partial<NewCustomer> & CustomerFormDefaults),
  ): CustomerFormRawValue | PartialWithRequiredKeyOf<NewCustomerFormRawValue> {
    return {
      ...customer,
      registrationDate: customer.registrationDate ? customer.registrationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
