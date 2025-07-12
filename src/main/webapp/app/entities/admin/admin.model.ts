export interface IAdmin {
  id: number;
  adminId?: string | null;
  username?: string | null;
  password?: string | null;
  email?: string | null;
  isActive?: boolean | null;
}

export type NewAdmin = Omit<IAdmin, 'id'> & { id: null };
