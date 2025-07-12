import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ICoachType } from '../coach-type.model';

@Component({
  selector: 'jhi-coach-type-detail',
  templateUrl: './coach-type-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class CoachTypeDetailComponent {
  coachType = input<ICoachType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
