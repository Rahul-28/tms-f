import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICoachType } from '../coach-type.model';
import { CoachTypeService } from '../service/coach-type.service';

@Component({
  templateUrl: './coach-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CoachTypeDeleteDialogComponent {
  coachType?: ICoachType;

  protected coachTypeService = inject(CoachTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.coachTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
