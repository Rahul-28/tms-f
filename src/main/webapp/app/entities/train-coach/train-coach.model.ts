import { ICoachType } from 'app/entities/coach-type/coach-type.model';
import { ITrain } from 'app/entities/train/train.model';

export interface ITrainCoach {
  id: number;
  trainNumber?: string | null;
  seatCapacity?: number | null;
  availableSeats?: number | null;
  farePrice?: number | null;
  coachType?: Pick<ICoachType, 'id'> | null;
  train?: Pick<ITrain, 'id'> | null;
}

export type NewTrainCoach = Omit<ITrainCoach, 'id'> & { id: null };
