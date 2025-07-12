import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPassenger } from '../passenger.model';

@Component({
  selector: 'jhi-passenger-detail',
  templateUrl: './passenger-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PassengerDetailComponent {
  passenger = input<IPassenger | null>(null);

  previousState(): void {
    window.history.back();
  }
}
