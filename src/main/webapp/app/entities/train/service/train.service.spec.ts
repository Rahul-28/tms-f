import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITrain } from '../train.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../train.test-samples';

import { RestTrain, TrainService } from './train.service';

const requireRestSample: RestTrain = {
  ...sampleWithRequiredData,
  serviceStartDate: sampleWithRequiredData.serviceStartDate?.format(DATE_FORMAT),
  serviceEndDate: sampleWithRequiredData.serviceEndDate?.format(DATE_FORMAT),
  departureTime: sampleWithRequiredData.departureTime?.toJSON(),
  arrivalTime: sampleWithRequiredData.arrivalTime?.toJSON(),
};

describe('Train Service', () => {
  let service: TrainService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrain | ITrain[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TrainService);
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

    it('should create a Train', () => {
      const train = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(train).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Train', () => {
      const train = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(train).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Train', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Train', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Train', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrainToCollectionIfMissing', () => {
      it('should add a Train to an empty array', () => {
        const train: ITrain = sampleWithRequiredData;
        expectedResult = service.addTrainToCollectionIfMissing([], train);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(train);
      });

      it('should not add a Train to an array that contains it', () => {
        const train: ITrain = sampleWithRequiredData;
        const trainCollection: ITrain[] = [
          {
            ...train,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrainToCollectionIfMissing(trainCollection, train);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Train to an array that doesn't contain it", () => {
        const train: ITrain = sampleWithRequiredData;
        const trainCollection: ITrain[] = [sampleWithPartialData];
        expectedResult = service.addTrainToCollectionIfMissing(trainCollection, train);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(train);
      });

      it('should add only unique Train to an array', () => {
        const trainArray: ITrain[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trainCollection: ITrain[] = [sampleWithRequiredData];
        expectedResult = service.addTrainToCollectionIfMissing(trainCollection, ...trainArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const train: ITrain = sampleWithRequiredData;
        const train2: ITrain = sampleWithPartialData;
        expectedResult = service.addTrainToCollectionIfMissing([], train, train2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(train);
        expect(expectedResult).toContain(train2);
      });

      it('should accept null and undefined values', () => {
        const train: ITrain = sampleWithRequiredData;
        expectedResult = service.addTrainToCollectionIfMissing([], null, train, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(train);
      });

      it('should return initial array if no Train is added', () => {
        const trainCollection: ITrain[] = [sampleWithRequiredData];
        expectedResult = service.addTrainToCollectionIfMissing(trainCollection, undefined, null);
        expect(expectedResult).toEqual(trainCollection);
      });
    });

    describe('compareTrain', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrain(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7549 };
        const entity2 = null;

        const compareResult1 = service.compareTrain(entity1, entity2);
        const compareResult2 = service.compareTrain(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7549 };
        const entity2 = { id: 8615 };

        const compareResult1 = service.compareTrain(entity1, entity2);
        const compareResult2 = service.compareTrain(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7549 };
        const entity2 = { id: 7549 };

        const compareResult1 = service.compareTrain(entity1, entity2);
        const compareResult2 = service.compareTrain(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
