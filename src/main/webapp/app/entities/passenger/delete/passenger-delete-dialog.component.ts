import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPassenger } from '../passenger.model';
import { PassengerService } from '../service/passenger.service';

@Component({
  templateUrl: './passenger-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PassengerDeleteDialogComponent {
  passenger?: IPassenger;

  protected passengerService = inject(PassengerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.passengerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
