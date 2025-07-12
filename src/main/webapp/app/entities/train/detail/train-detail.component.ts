import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ITrain } from '../train.model';

@Component({
  selector: 'jhi-train-detail',
  templateUrl: './train-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TrainDetailComponent {
  train = input<ITrain | null>(null);

  previousState(): void {
    window.history.back();
  }
}
