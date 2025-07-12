export interface ICoachType {
  id: number;
  coachId?: string | null;
  coachName?: string | null;
  seatCapacity?: number | null;
  fareMultiplier?: number | null;
}

export type NewCoachType = Omit<ICoachType, 'id'> & { id: null };
