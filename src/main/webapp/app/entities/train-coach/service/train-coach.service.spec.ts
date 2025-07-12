import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITrainCoach } from '../train-coach.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../train-coach.test-samples';

import { TrainCoachService } from './train-coach.service';

const requireRestSample: ITrainCoach = {
  ...sampleWithRequiredData,
};

describe('TrainCoach Service', () => {
  let service: TrainCoachService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrainCoach | ITrainCoach[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TrainCoachService);
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

    it('should create a TrainCoach', () => {
      const trainCoach = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trainCoach).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TrainCoach', () => {
      const trainCoach = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trainCoach).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TrainCoach', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TrainCoach', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TrainCoach', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrainCoachToCollectionIfMissing', () => {
      it('should add a TrainCoach to an empty array', () => {
        const trainCoach: ITrainCoach = sampleWithRequiredData;
        expectedResult = service.addTrainCoachToCollectionIfMissing([], trainCoach);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trainCoach);
      });

      it('should not add a TrainCoach to an array that contains it', () => {
        const trainCoach: ITrainCoach = sampleWithRequiredData;
        const trainCoachCollection: ITrainCoach[] = [
          {
            ...trainCoach,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrainCoachToCollectionIfMissing(trainCoachCollection, trainCoach);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TrainCoach to an array that doesn't contain it", () => {
        const trainCoach: ITrainCoach = sampleWithRequiredData;
        const trainCoachCollection: ITrainCoach[] = [sampleWithPartialData];
        expectedResult = service.addTrainCoachToCollectionIfMissing(trainCoachCollection, trainCoach);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trainCoach);
      });

      it('should add only unique TrainCoach to an array', () => {
        const trainCoachArray: ITrainCoach[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trainCoachCollection: ITrainCoach[] = [sampleWithRequiredData];
        expectedResult = service.addTrainCoachToCollectionIfMissing(trainCoachCollection, ...trainCoachArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trainCoach: ITrainCoach = sampleWithRequiredData;
        const trainCoach2: ITrainCoach = sampleWithPartialData;
        expectedResult = service.addTrainCoachToCollectionIfMissing([], trainCoach, trainCoach2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trainCoach);
        expect(expectedResult).toContain(trainCoach2);
      });

      it('should accept null and undefined values', () => {
        const trainCoach: ITrainCoach = sampleWithRequiredData;
        expectedResult = service.addTrainCoachToCollectionIfMissing([], null, trainCoach, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trainCoach);
      });

      it('should return initial array if no TrainCoach is added', () => {
        const trainCoachCollection: ITrainCoach[] = [sampleWithRequiredData];
        expectedResult = service.addTrainCoachToCollectionIfMissing(trainCoachCollection, undefined, null);
        expect(expectedResult).toEqual(trainCoachCollection);
      });
    });

    describe('compareTrainCoach', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrainCoach(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 15849 };
        const entity2 = null;

        const compareResult1 = service.compareTrainCoach(entity1, entity2);
        const compareResult2 = service.compareTrainCoach(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 15849 };
        const entity2 = { id: 1856 };

        const compareResult1 = service.compareTrainCoach(entity1, entity2);
        const compareResult2 = service.compareTrainCoach(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 15849 };
        const entity2 = { id: 15849 };

        const compareResult1 = service.compareTrainCoach(entity1, entity2);
        const compareResult2 = service.compareTrainCoach(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
