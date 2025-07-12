import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TrainService } from '../service/train.service';
import { ITrain } from '../train.model';
import { TrainFormService } from './train-form.service';

import { TrainUpdateComponent } from './train-update.component';

describe('Train Management Update Component', () => {
  let comp: TrainUpdateComponent;
  let fixture: ComponentFixture<TrainUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trainFormService: TrainFormService;
  let trainService: TrainService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TrainUpdateComponent],
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
      .overrideTemplate(TrainUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrainUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trainFormService = TestBed.inject(TrainFormService);
    trainService = TestBed.inject(TrainService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const train: ITrain = { id: 8615 };

      activatedRoute.data = of({ train });
      comp.ngOnInit();

      expect(comp.train).toEqual(train);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrain>>();
      const train = { id: 7549 };
      jest.spyOn(trainFormService, 'getTrain').mockReturnValue(train);
      jest.spyOn(trainService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ train });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: train }));
      saveSubject.complete();

      // THEN
      expect(trainFormService.getTrain).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trainService.update).toHaveBeenCalledWith(expect.objectContaining(train));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrain>>();
      const train = { id: 7549 };
      jest.spyOn(trainFormService, 'getTrain').mockReturnValue({ id: null });
      jest.spyOn(trainService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ train: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: train }));
      saveSubject.complete();

      // THEN
      expect(trainFormService.getTrain).toHaveBeenCalled();
      expect(trainService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrain>>();
      const train = { id: 7549 };
      jest.spyOn(trainService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ train });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trainService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
