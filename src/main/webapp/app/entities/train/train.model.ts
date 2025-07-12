import dayjs from 'dayjs/esm';
import { ServiceType } from 'app/entities/enumerations/service-type.model';

export interface ITrain {
  id: number;
  trainNumber?: string | null;
  trainName?: string | null;
  origin?: string | null;
  destination?: string | null;
  intermediateStop?: string | null;
  serviceStartDate?: dayjs.Dayjs | null;
  serviceEndDate?: dayjs.Dayjs | null;
  serviceType?: keyof typeof ServiceType | null;
  departureTime?: dayjs.Dayjs | null;
  arrivalTime?: dayjs.Dayjs | null;
  basicPrice?: number | null;
  isActive?: boolean | null;
}

export type NewTrain = Omit<ITrain, 'id'> & { id: null };
