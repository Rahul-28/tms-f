import dayjs from 'dayjs/esm';

export interface ICustomer {
  id: number;
  customerId?: string | null;
  username?: string | null;
  password?: string | null;
  email?: string | null;
  mobileNumber?: string | null;
  aadhaarNumber?: string | null;
  address?: string | null;
  contactInformation?: string | null;
  registrationDate?: dayjs.Dayjs | null;
  isActive?: boolean | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
