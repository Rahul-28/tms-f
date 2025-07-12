import { IAdmin, NewAdmin } from './admin.model';

export const sampleWithRequiredData: IAdmin = {
  id: 12297,
  adminId: 'gah',
  username: 'though',
  password: 'since',
  email: 'Lizzie.Ruecker@hotmail.com',
  isActive: false,
};

export const sampleWithPartialData: IAdmin = {
  id: 2870,
  adminId: 'pointless yesterday',
  username: 'haunting with',
  password: 'than er',
  email: 'Otto51@yahoo.com',
  isActive: false,
};

export const sampleWithFullData: IAdmin = {
  id: 23061,
  adminId: 'adventurously',
  username: 'except',
  password: 'wise',
  email: 'Krystel_Aufderhar@hotmail.com',
  isActive: false,
};

export const sampleWithNewData: NewAdmin = {
  adminId: 'doubtfully wonderfully',
  username: 'stuff up',
  password: 'until',
  email: 'Deondre35@yahoo.com',
  isActive: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
