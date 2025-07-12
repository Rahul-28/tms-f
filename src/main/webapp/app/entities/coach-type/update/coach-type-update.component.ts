import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICoachType } from '../coach-type.model';
import { CoachTypeService } from '../service/coach-type.service';
import { CoachTypeFormGroup, CoachTypeFormService } from './coach-type-form.service';

@Component({
  selector: 'jhi-coach-type-update',
  templateUrl: './coach-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CoachTypeUpdateComponent implements OnInit {
  isSaving = false;
  coachType: ICoachType | null = null;

  protected coachTypeService = inject(CoachTypeService);
  protected coachTypeFormService = inject(CoachTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CoachTypeFormGroup = this.coachTypeFormService.createCoachTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coachType }) => {
      this.coachType = coachType;
      if (coachType) {
        this.updateForm(coachType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const coachType = this.coachTypeFormService.getCoachType(this.editForm);
    if (coachType.id !== null) {
      this.subscribeToSaveResponse(this.coachTypeService.update(coachType));
    } else {
      this.subscribeToSaveResponse(this.coachTypeService.create(coachType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoachType>>): void {
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

  protected updateForm(coachType: ICoachType): void {
    this.coachType = coachType;
    this.coachTypeFormService.resetForm(this.editForm, coachType);
  }
}
