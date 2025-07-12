import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPayment } from 'app/entities/payment/payment.model';
import { PaymentService } from 'app/entities/payment/service/payment.service';
import { InvoiceService } from '../service/invoice.service';
import { IInvoice } from '../invoice.model';
import { InvoiceFormService } from './invoice-form.service';

import { InvoiceUpdateComponent } from './invoice-update.component';

describe('Invoice Management Update Component', () => {
  let comp: InvoiceUpdateComponent;
  let fixture: ComponentFixture<InvoiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let invoiceFormService: InvoiceFormService;
  let invoiceService: InvoiceService;
  let paymentService: PaymentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [InvoiceUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InvoiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceFormService = TestBed.inject(InvoiceFormService);
    invoiceService = TestBed.inject(InvoiceService);
    paymentService = TestBed.inject(PaymentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Payment query and add missing value', () => {
      const invoice: IInvoice = { id: 1801 };
      const payment: IPayment = { id: 20208 };
      invoice.payment = payment;

      const paymentCollection: IPayment[] = [{ id: 20208 }];
      jest.spyOn(paymentService, 'query').mockReturnValue(of(new HttpResponse({ body: paymentCollection })));
      const additionalPayments = [payment];
      const expectedCollection: IPayment[] = [...additionalPayments, ...paymentCollection];
      jest.spyOn(paymentService, 'addPaymentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(paymentService.query).toHaveBeenCalled();
      expect(paymentService.addPaymentToCollectionIfMissing).toHaveBeenCalledWith(
        paymentCollection,
        ...additionalPayments.map(expect.objectContaining),
      );
      expect(comp.paymentsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const invoice: IInvoice = { id: 1801 };
      const payment: IPayment = { id: 20208 };
      invoice.payment = payment;

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(comp.paymentsSharedCollection).toContainEqual(payment);
      expect(comp.invoice).toEqual(invoice);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoice>>();
      const invoice = { id: 1362 };
      jest.spyOn(invoiceFormService, 'getInvoice').mockReturnValue(invoice);
      jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoice }));
      saveSubject.complete();

      // THEN
      expect(invoiceFormService.getInvoice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceService.update).toHaveBeenCalledWith(expect.objectContaining(invoice));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoice>>();
      const invoice = { id: 1362 };
      jest.spyOn(invoiceFormService, 'getInvoice').mockReturnValue({ id: null });
      jest.spyOn(invoiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoice }));
      saveSubject.complete();

      // THEN
      expect(invoiceFormService.getInvoice).toHaveBeenCalled();
      expect(invoiceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoice>>();
      const invoice = { id: 1362 };
      jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePayment', () => {
      it('should forward to paymentService', () => {
        const entity = { id: 20208 };
        const entity2 = { id: 31232 };
        jest.spyOn(paymentService, 'comparePayment');
        comp.comparePayment(entity, entity2);
        expect(paymentService.comparePayment).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
