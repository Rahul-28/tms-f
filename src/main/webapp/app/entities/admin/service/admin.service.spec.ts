import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAdmin } from '../admin.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../admin.test-samples';

import { AdminService } from './admin.service';

const requireRestSample: IAdmin = {
  ...sampleWithRequiredData,
};

describe('Admin Service', () => {
  let service: AdminService;
  let httpMock: HttpTestingController;
  let expectedResult: IAdmin | IAdmin[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AdminService);
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

    it('should create a Admin', () => {
      const admin = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(admin).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Admin', () => {
      const admin = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(admin).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Admin', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Admin', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Admin', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAdminToCollectionIfMissing', () => {
      it('should add a Admin to an empty array', () => {
        const admin: IAdmin = sampleWithRequiredData;
        expectedResult = service.addAdminToCollectionIfMissing([], admin);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(admin);
      });

      it('should not add a Admin to an array that contains it', () => {
        const admin: IAdmin = sampleWithRequiredData;
        const adminCollection: IAdmin[] = [
          {
            ...admin,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAdminToCollectionIfMissing(adminCollection, admin);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Admin to an array that doesn't contain it", () => {
        const admin: IAdmin = sampleWithRequiredData;
        const adminCollection: IAdmin[] = [sampleWithPartialData];
        expectedResult = service.addAdminToCollectionIfMissing(adminCollection, admin);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(admin);
      });

      it('should add only unique Admin to an array', () => {
        const adminArray: IAdmin[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const adminCollection: IAdmin[] = [sampleWithRequiredData];
        expectedResult = service.addAdminToCollectionIfMissing(adminCollection, ...adminArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const admin: IAdmin = sampleWithRequiredData;
        const admin2: IAdmin = sampleWithPartialData;
        expectedResult = service.addAdminToCollectionIfMissing([], admin, admin2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(admin);
        expect(expectedResult).toContain(admin2);
      });

      it('should accept null and undefined values', () => {
        const admin: IAdmin = sampleWithRequiredData;
        expectedResult = service.addAdminToCollectionIfMissing([], null, admin, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(admin);
      });

      it('should return initial array if no Admin is added', () => {
        const adminCollection: IAdmin[] = [sampleWithRequiredData];
        expectedResult = service.addAdminToCollectionIfMissing(adminCollection, undefined, null);
        expect(expectedResult).toEqual(adminCollection);
      });
    });

    describe('compareAdmin', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAdmin(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19424 };
        const entity2 = null;

        const compareResult1 = service.compareAdmin(entity1, entity2);
        const compareResult2 = service.compareAdmin(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19424 };
        const entity2 = { id: 11145 };

        const compareResult1 = service.compareAdmin(entity1, entity2);
        const compareResult2 = service.compareAdmin(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 19424 };
        const entity2 = { id: 19424 };

        const compareResult1 = service.compareAdmin(entity1, entity2);
        const compareResult2 = service.compareAdmin(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
