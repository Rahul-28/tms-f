import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICoachType } from '../coach-type.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../coach-type.test-samples';

import { CoachTypeService } from './coach-type.service';

const requireRestSample: ICoachType = {
  ...sampleWithRequiredData,
};

describe('CoachType Service', () => {
  let service: CoachTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICoachType | ICoachType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CoachTypeService);
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

    it('should create a CoachType', () => {
      const coachType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(coachType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CoachType', () => {
      const coachType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(coachType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CoachType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CoachType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CoachType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCoachTypeToCollectionIfMissing', () => {
      it('should add a CoachType to an empty array', () => {
        const coachType: ICoachType = sampleWithRequiredData;
        expectedResult = service.addCoachTypeToCollectionIfMissing([], coachType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(coachType);
      });

      it('should not add a CoachType to an array that contains it', () => {
        const coachType: ICoachType = sampleWithRequiredData;
        const coachTypeCollection: ICoachType[] = [
          {
            ...coachType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCoachTypeToCollectionIfMissing(coachTypeCollection, coachType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CoachType to an array that doesn't contain it", () => {
        const coachType: ICoachType = sampleWithRequiredData;
        const coachTypeCollection: ICoachType[] = [sampleWithPartialData];
        expectedResult = service.addCoachTypeToCollectionIfMissing(coachTypeCollection, coachType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(coachType);
      });

      it('should add only unique CoachType to an array', () => {
        const coachTypeArray: ICoachType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const coachTypeCollection: ICoachType[] = [sampleWithRequiredData];
        expectedResult = service.addCoachTypeToCollectionIfMissing(coachTypeCollection, ...coachTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const coachType: ICoachType = sampleWithRequiredData;
        const coachType2: ICoachType = sampleWithPartialData;
        expectedResult = service.addCoachTypeToCollectionIfMissing([], coachType, coachType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(coachType);
        expect(expectedResult).toContain(coachType2);
      });

      it('should accept null and undefined values', () => {
        const coachType: ICoachType = sampleWithRequiredData;
        expectedResult = service.addCoachTypeToCollectionIfMissing([], null, coachType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(coachType);
      });

      it('should return initial array if no CoachType is added', () => {
        const coachTypeCollection: ICoachType[] = [sampleWithRequiredData];
        expectedResult = service.addCoachTypeToCollectionIfMissing(coachTypeCollection, undefined, null);
        expect(expectedResult).toEqual(coachTypeCollection);
      });
    });

    describe('compareCoachType', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCoachType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7940 };
        const entity2 = null;

        const compareResult1 = service.compareCoachType(entity1, entity2);
        const compareResult2 = service.compareCoachType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7940 };
        const entity2 = { id: 18089 };

        const compareResult1 = service.compareCoachType(entity1, entity2);
        const compareResult2 = service.compareCoachType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 7940 };
        const entity2 = { id: 7940 };

        const compareResult1 = service.compareCoachType(entity1, entity2);
        const compareResult2 = service.compareCoachType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
