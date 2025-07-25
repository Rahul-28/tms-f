import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../payment.test-samples';

import { PaymentFormService } from './payment-form.service';

describe('Payment Form Service', () => {
  let service: PaymentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaymentFormService);
  });

  describe('Service methods', () => {
    describe('createPaymentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaymentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            paymentId: expect.any(Object),
            transactionId: expect.any(Object),
            receiptNumber: expect.any(Object),
            transactionDate: expect.any(Object),
            transactionType: expect.any(Object),
            transactionAmount: expect.any(Object),
            transactionStatus: expect.any(Object),
            cardNumber: expect.any(Object),
            expiryDate: expect.any(Object),
            cvv: expect.any(Object),
            cardholderName: expect.any(Object),
            booking: expect.any(Object),
          }),
        );
      });

      it('passing IPayment should create a new form with FormGroup', () => {
        const formGroup = service.createPaymentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            paymentId: expect.any(Object),
            transactionId: expect.any(Object),
            receiptNumber: expect.any(Object),
            transactionDate: expect.any(Object),
            transactionType: expect.any(Object),
            transactionAmount: expect.any(Object),
            transactionStatus: expect.any(Object),
            cardNumber: expect.any(Object),
            expiryDate: expect.any(Object),
            cvv: expect.any(Object),
            cardholderName: expect.any(Object),
            booking: expect.any(Object),
          }),
        );
      });
    });

    describe('getPayment', () => {
      it('should return NewPayment for default Payment initial value', () => {
        const formGroup = service.createPaymentFormGroup(sampleWithNewData);

        const payment = service.getPayment(formGroup) as any;

        expect(payment).toMatchObject(sampleWithNewData);
      });

      it('should return NewPayment for empty Payment initial value', () => {
        const formGroup = service.createPaymentFormGroup();

        const payment = service.getPayment(formGroup) as any;

        expect(payment).toMatchObject({});
      });

      it('should return IPayment', () => {
        const formGroup = service.createPaymentFormGroup(sampleWithRequiredData);

        const payment = service.getPayment(formGroup) as any;

        expect(payment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPayment should not enable id FormControl', () => {
        const formGroup = service.createPaymentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPayment should disable id FormControl', () => {
        const formGroup = service.createPaymentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
