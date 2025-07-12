import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { TransactionStatus } from 'app/entities/enumerations/transaction-status.model';
import { InvoiceService } from '../service/invoice.service';
import { IInvoice } from '../invoice.model';
import { InvoiceFormGroup, InvoiceFormService } from './invoice-form.service';

@Component({
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;
  invoice: IInvoice | null = null;
  transactionTypeValues = Object.keys(TransactionType);
  transactionStatusValues = Object.keys(TransactionStatus);

  paymentsSharedCollection: IPayment[] = [];

  protected invoiceService = inject(InvoiceService);
  protected invoiceFormService = inject(InvoiceFormService);
  protected paymentService = inject(PaymentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InvoiceFormGroup = this.invoiceFormService.createInvoiceFormGroup();

  comparePayment = (o1: IPayment | null, o2: IPayment | null): boolean => this.paymentService.comparePayment(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.invoice = invoice;
      if (invoice) {
        this.updateForm(invoice);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.invoiceFormService.getInvoice(this.editForm);
    if (invoice.id !== null) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
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

  protected updateForm(invoice: IInvoice): void {
    this.invoice = invoice;
    this.invoiceFormService.resetForm(this.editForm, invoice);

    this.paymentsSharedCollection = this.paymentService.addPaymentToCollectionIfMissing<IPayment>(
      this.paymentsSharedCollection,
      invoice.payment,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paymentService
      .query()
      .pipe(map((res: HttpResponse<IPayment[]>) => res.body ?? []))
      .pipe(map((payments: IPayment[]) => this.paymentService.addPaymentToCollectionIfMissing<IPayment>(payments, this.invoice?.payment)))
      .subscribe((payments: IPayment[]) => (this.paymentsSharedCollection = payments));
  }
}
