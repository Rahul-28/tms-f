import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITrainCoach } from '../train-coach.model';
import { TrainCoachService } from '../service/train-coach.service';

@Component({
  templateUrl: './train-coach-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TrainCoachDeleteDialogComponent {
  trainCoach?: ITrainCoach;

  protected trainCoachService = inject(TrainCoachService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trainCoachService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
