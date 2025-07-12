import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AdminService } from '../service/admin.service';
import { IAdmin } from '../admin.model';
import { AdminFormService } from './admin-form.service';

import { AdminUpdateComponent } from './admin-update.component';

describe('Admin Management Update Component', () => {
  let comp: AdminUpdateComponent;
  let fixture: ComponentFixture<AdminUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let adminFormService: AdminFormService;
  let adminService: AdminService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AdminUpdateComponent],
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
      .overrideTemplate(AdminUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AdminUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    adminFormService = TestBed.inject(AdminFormService);
    adminService = TestBed.inject(AdminService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const admin: IAdmin = { id: 11145 };

      activatedRoute.data = of({ admin });
      comp.ngOnInit();

      expect(comp.admin).toEqual(admin);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdmin>>();
      const admin = { id: 19424 };
      jest.spyOn(adminFormService, 'getAdmin').mockReturnValue(admin);
      jest.spyOn(adminService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ admin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: admin }));
      saveSubject.complete();

      // THEN
      expect(adminFormService.getAdmin).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(adminService.update).toHaveBeenCalledWith(expect.objectContaining(admin));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdmin>>();
      const admin = { id: 19424 };
      jest.spyOn(adminFormService, 'getAdmin').mockReturnValue({ id: null });
      jest.spyOn(adminService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ admin: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: admin }));
      saveSubject.complete();

      // THEN
      expect(adminFormService.getAdmin).toHaveBeenCalled();
      expect(adminService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdmin>>();
      const admin = { id: 19424 };
      jest.spyOn(adminService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ admin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(adminService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
