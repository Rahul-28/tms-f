import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CoachTypeService } from '../service/coach-type.service';
import { ICoachType } from '../coach-type.model';
import { CoachTypeFormService } from './coach-type-form.service';

import { CoachTypeUpdateComponent } from './coach-type-update.component';

describe('CoachType Management Update Component', () => {
  let comp: CoachTypeUpdateComponent;
  let fixture: ComponentFixture<CoachTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let coachTypeFormService: CoachTypeFormService;
  let coachTypeService: CoachTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoachTypeUpdateComponent],
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
      .overrideTemplate(CoachTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CoachTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    coachTypeFormService = TestBed.inject(CoachTypeFormService);
    coachTypeService = TestBed.inject(CoachTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const coachType: ICoachType = { id: 18089 };

      activatedRoute.data = of({ coachType });
      comp.ngOnInit();

      expect(comp.coachType).toEqual(coachType);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICoachType>>();
      const coachType = { id: 7940 };
      jest.spyOn(coachTypeFormService, 'getCoachType').mockReturnValue(coachType);
      jest.spyOn(coachTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coachType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: coachType }));
      saveSubject.complete();

      // THEN
      expect(coachTypeFormService.getCoachType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(coachTypeService.update).toHaveBeenCalledWith(expect.objectContaining(coachType));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICoachType>>();
      const coachType = { id: 7940 };
      jest.spyOn(coachTypeFormService, 'getCoachType').mockReturnValue({ id: null });
      jest.spyOn(coachTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coachType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: coachType }));
      saveSubject.complete();

      // THEN
      expect(coachTypeFormService.getCoachType).toHaveBeenCalled();
      expect(coachTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICoachType>>();
      const coachType = { id: 7940 };
      jest.spyOn(coachTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ coachType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(coachTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
