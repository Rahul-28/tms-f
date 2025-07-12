import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ServiceType } from 'app/entities/enumerations/service-type.model';
import { ITrain } from '../train.model';
import { TrainService } from '../service/train.service';
import { TrainFormGroup, TrainFormService } from './train-form.service';

@Component({
  selector: 'jhi-train-update',
  templateUrl: './train-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TrainUpdateComponent implements OnInit {
  isSaving = false;
  train: ITrain | null = null;
  serviceTypeValues = Object.keys(ServiceType);

  protected trainService = inject(TrainService);
  protected trainFormService = inject(TrainFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TrainFormGroup = this.trainFormService.createTrainFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ train }) => {
      this.train = train;
      if (train) {
        this.updateForm(train);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const train = this.trainFormService.getTrain(this.editForm);
    if (train.id !== null) {
      this.subscribeToSaveResponse(this.trainService.update(train));
    } else {
      this.subscribeToSaveResponse(this.trainService.create(train));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrain>>): void {
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

  protected updateForm(train: ITrain): void {
    this.train = train;
    this.trainFormService.resetForm(this.editForm, train);
  }
}
