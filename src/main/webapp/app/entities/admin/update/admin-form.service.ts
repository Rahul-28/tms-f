import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAdmin, NewAdmin } from '../admin.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAdmin for edit and NewAdminFormGroupInput for create.
 */
type AdminFormGroupInput = IAdmin | PartialWithRequiredKeyOf<NewAdmin>;

type AdminFormDefaults = Pick<NewAdmin, 'id' | 'isActive'>;

type AdminFormGroupContent = {
  id: FormControl<IAdmin['id'] | NewAdmin['id']>;
  adminId: FormControl<IAdmin['adminId']>;
  username: FormControl<IAdmin['username']>;
  password: FormControl<IAdmin['password']>;
  email: FormControl<IAdmin['email']>;
  isActive: FormControl<IAdmin['isActive']>;
};

export type AdminFormGroup = FormGroup<AdminFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AdminFormService {
  createAdminFormGroup(admin: AdminFormGroupInput = { id: null }): AdminFormGroup {
    const adminRawValue = {
      ...this.getFormDefaults(),
      ...admin,
    };
    return new FormGroup<AdminFormGroupContent>({
      id: new FormControl(
        { value: adminRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      adminId: new FormControl(adminRawValue.adminId, {
        validators: [Validators.required],
      }),
      username: new FormControl(adminRawValue.username, {
        validators: [Validators.required],
      }),
      password: new FormControl(adminRawValue.password, {
        validators: [Validators.required],
      }),
      email: new FormControl(adminRawValue.email, {
        validators: [Validators.required],
      }),
      isActive: new FormControl(adminRawValue.isActive, {
        validators: [Validators.required],
      }),
    });
  }

  getAdmin(form: AdminFormGroup): IAdmin | NewAdmin {
    return form.getRawValue() as IAdmin | NewAdmin;
  }

  resetForm(form: AdminFormGroup, admin: AdminFormGroupInput): void {
    const adminRawValue = { ...this.getFormDefaults(), ...admin };
    form.reset(
      {
        ...adminRawValue,
        id: { value: adminRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AdminFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
