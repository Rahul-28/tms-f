import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITrainCoach } from '../train-coach.model';

@Component({
  selector: 'jhi-train-coach-detail',
  templateUrl: './train-coach-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TrainCoachDetailComponent {
  trainCoach = input<ITrainCoach | null>(null);

  previousState(): void {
    window.history.back();
  }
}
