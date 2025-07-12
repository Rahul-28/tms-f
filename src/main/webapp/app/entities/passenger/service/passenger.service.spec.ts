import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPassenger } from '../passenger.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../passenger.test-samples';

import { PassengerService } from './passenger.service';

const requireRestSample: IPassenger = {
  ...sampleWithRequiredData,
};

describe('Passenger Service', () => {
  let service: PassengerService;
  let httpMock: HttpTestingController;
  let expectedResult: IPassenger | IPassenger[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PassengerService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Passenger', () => {
      const passenger = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(passenger).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Passenger', () => {
      const passenger = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(passenger).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Passenger', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Passenger', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Passenger', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPassengerToCollectionIfMissing', () => {
      it('should add a Passenger to an empty array', () => {
        const passenger: IPassenger = sampleWithRequiredData;
        expectedResult = service.addPassengerToCollectionIfMissing([], passenger);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(passenger);
      });

      it('should not add a Passenger to an array that contains it', () => {
        const passenger: IPassenger = sampleWithRequiredData;
        const passengerCollection: IPassenger[] = [
          {
            ...passenger,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPassengerToCollectionIfMissing(passengerCollection, passenger);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Passenger to an array that doesn't contain it", () => {
        const passenger: IPassenger = sampleWithRequiredData;
        const passengerCollection: IPassenger[] = [sampleWithPartialData];
        expectedResult = service.addPassengerToCollectionIfMissing(passengerCollection, passenger);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(passenger);
      });

      it('should add only unique Passenger to an array', () => {
        const passengerArray: IPassenger[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const passengerCollection: IPassenger[] = [sampleWithRequiredData];
        expectedResult = service.addPassengerToCollectionIfMissing(passengerCollection, ...passengerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const passenger: IPassenger = sampleWithRequiredData;
        const passenger2: IPassenger = sampleWithPartialData;
        expectedResult = service.addPassengerToCollectionIfMissing([], passenger, passenger2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(passenger);
        expect(expectedResult).toContain(passenger2);
      });

      it('should accept null and undefined values', () => {
        const passenger: IPassenger = sampleWithRequiredData;
        expectedResult = service.addPassengerToCollectionIfMissing([], null, passenger, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(passenger);
      });

      it('should return initial array if no Passenger is added', () => {
        const passengerCollection: IPassenger[] = [sampleWithRequiredData];
        expectedResult = service.addPassengerToCollectionIfMissing(passengerCollection, undefined, null);
        expect(expectedResult).toEqual(passengerCollection);
      });
    });

    describe('comparePassenger', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePassenger(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 22165 };
        const entity2 = null;

        const compareResult1 = service.comparePassenger(entity1, entity2);
        const compareResult2 = service.comparePassenger(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 22165 };
        const entity2 = { id: 30166 };

        const compareResult1 = service.comparePassenger(entity1, entity2);
        const compareResult2 = service.comparePassenger(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 22165 };
        const entity2 = { id: 22165 };

        const compareResult1 = service.comparePassenger(entity1, entity2);
        const compareResult2 = service.comparePassenger(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
