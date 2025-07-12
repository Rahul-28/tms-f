import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICoachType } from 'app/entities/coach-type/coach-type.model';
import { CoachTypeService } from 'app/entities/coach-type/service/coach-type.service';
import { ITrain } from 'app/entities/train/train.model';
import { TrainService } from 'app/entities/train/service/train.service';
import { TrainCoachService } from '../service/train-coach.service';
import { ITrainCoach } from '../train-coach.model';
import { TrainCoachFormGroup, TrainCoachFormService } from './train-coach-form.service';

@Component({
  selector: 'jhi-train-coach-update',
  templateUrl: './train-coach-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrainCoachUpdateComponent implements OnInit {
  isSaving = false;
  trainCoach: ITrainCoach | null = null;

  coachTypesSharedCollection: ICoachType[] = [];
  trainsSharedCollection: ITrain[] = [];

  protected trainCoachService = inject(TrainCoachService);
  protected trainCoachFormService = inject(TrainCoachFormService);
  protected coachTypeService = inject(CoachTypeService);
  protected trainService = inject(TrainService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrainCoachFormGroup = this.trainCoachFormService.createTrainCoachFormGroup();

  compareCoachType = (o1: ICoachType | null, o2: ICoachType | null): boolean => this.coachTypeService.compareCoachType(o1, o2);

  compareTrain = (o1: ITrain | null, o2: ITrain | null): boolean => this.trainService.compareTrain(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trainCoach }) => {
      this.trainCoach = trainCoach;
      if (trainCoach) {
        this.updateForm(trainCoach);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trainCoach = this.trainCoachFormService.getTrainCoach(this.editForm);
    if (trainCoach.id !== null) {
      this.subscribeToSaveResponse(this.trainCoachService.update(trainCoach));
    } else {
      this.subscribeToSaveResponse(this.trainCoachService.create(trainCoach));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrainCoach>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(trainCoach: ITrainCoach): void {
    this.trainCoach = trainCoach;
    this.trainCoachFormService.resetForm(this.editForm, trainCoach);

    this.coachTypesSharedCollection = this.coachTypeService.addCoachTypeToCollectionIfMissing<ICoachType>(
      this.coachTypesSharedCollection,
      trainCoach.coachType,
    );
    this.trainsSharedCollection = this.trainService.addTrainToCollectionIfMissing<ITrain>(this.trainsSharedCollection, trainCoach.train);
  }

  protected loadRelationshipsOptions(): void {
    this.coachTypeService
      .query()
      .pipe(map((res: HttpResponse<ICoachType[]>) => res.body ?? []))
      .pipe(
        map((coachTypes: ICoachType[]) =>
          this.coachTypeService.addCoachTypeToCollectionIfMissing<ICoachType>(coachTypes, this.trainCoach?.coachType),
        ),
      )
      .subscribe((coachTypes: ICoachType[]) => (this.coachTypesSharedCollection = coachTypes));

    this.trainService
      .query()
      .pipe(map((res: HttpResponse<ITrain[]>) => res.body ?? []))
      .pipe(map((trains: ITrain[]) => this.trainService.addTrainToCollectionIfMissing<ITrain>(trains, this.trainCoach?.train)))
      .subscribe((trains: ITrain[]) => (this.trainsSharedCollection = trains));
  }
}
