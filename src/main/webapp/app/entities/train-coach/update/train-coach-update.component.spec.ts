import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICoachType } from 'app/entities/coach-type/coach-type.model';
import { CoachTypeService } from 'app/entities/coach-type/service/coach-type.service';
import { ITrain } from 'app/entities/train/train.model';
import { TrainService } from 'app/entities/train/service/train.service';
import { ITrainCoach } from '../train-coach.model';
import { TrainCoachService } from '../service/train-coach.service';
import { TrainCoachFormService } from './train-coach-form.service';

import { TrainCoachUpdateComponent } from './train-coach-update.component';

describe('TrainCoach Management Update Component', () => {
  let comp: TrainCoachUpdateComponent;
  let fixture: ComponentFixture<TrainCoachUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trainCoachFormService: TrainCoachFormService;
  let trainCoachService: TrainCoachService;
  let coachTypeService: CoachTypeService;
  let trainService: TrainService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TrainCoachUpdateComponent],
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
      .overrideTemplate(TrainCoachUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrainCoachUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trainCoachFormService = TestBed.inject(TrainCoachFormService);
    trainCoachService = TestBed.inject(TrainCoachService);
    coachTypeService = TestBed.inject(CoachTypeService);
    trainService = TestBed.inject(TrainService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CoachType query and add missing value', () => {
      const trainCoach: ITrainCoach = { id: 1856 };
      const coachType: ICoachType = { id: 7940 };
      trainCoach.coachType = coachType;

      const coachTypeCollection: ICoachType[] = [{ id: 7940 }];
      jest.spyOn(coachTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: coachTypeCollection })));
      const additionalCoachTypes = [coachType];
      const expectedCollection: ICoachType[] = [...additionalCoachTypes, ...coachTypeCollection];
      jest.spyOn(coachTypeService, 'addCoachTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trainCoach });
      comp.ngOnInit();

      expect(coachTypeService.query).toHaveBeenCalled();
      expect(coachTypeService.addCoachTypeToCollectionIfMissing).toHaveBeenCalledWith(
        coachTypeCollection,
        ...additionalCoachTypes.map(expect.objectContaining),
      );
      expect(comp.coachTypesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Train query and add missing value', () => {
      const trainCoach: ITrainCoach = { id: 1856 };
      const train: ITrain = { id: 7549 };
      trainCoach.train = train;

      const trainCollection: ITrain[] = [{ id: 7549 }];
      jest.spyOn(trainService, 'query').mockReturnValue(of(new HttpResponse({ body: trainCollection })));
      const additionalTrains = [train];
      const expectedCollection: ITrain[] = [...additionalTrains, ...trainCollection];
      jest.spyOn(trainService, 'addTrainToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trainCoach });
      comp.ngOnInit();

      expect(trainService.query).toHaveBeenCalled();
      expect(trainService.addTrainToCollectionIfMissing).toHaveBeenCalledWith(
        trainCollection,
        ...additionalTrains.map(expect.objectContaining),
      );
      expect(comp.trainsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const trainCoach: ITrainCoach = { id: 1856 };
      const coachType: ICoachType = { id: 7940 };
      trainCoach.coachType = coachType;
      const train: ITrain = { id: 7549 };
      trainCoach.train = train;

      activatedRoute.data = of({ trainCoach });
      comp.ngOnInit();

      expect(comp.coachTypesSharedCollection).toContainEqual(coachType);
      expect(comp.trainsSharedCollection).toContainEqual(train);
      expect(comp.trainCoach).toEqual(trainCoach);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrainCoach>>();
      const trainCoach = { id: 15849 };
      jest.spyOn(trainCoachFormService, 'getTrainCoach').mockReturnValue(trainCoach);
      jest.spyOn(trainCoachService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trainCoach });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trainCoach }));
      saveSubject.complete();

      // THEN
      expect(trainCoachFormService.getTrainCoach).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trainCoachService.update).toHaveBeenCalledWith(expect.objectContaining(trainCoach));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrainCoach>>();
      const trainCoach = { id: 15849 };
      jest.spyOn(trainCoachFormService, 'getTrainCoach').mockReturnValue({ id: null });
      jest.spyOn(trainCoachService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trainCoach: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trainCoach }));
      saveSubject.complete();

      // THEN
      expect(trainCoachFormService.getTrainCoach).toHaveBeenCalled();
      expect(trainCoachService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrainCoach>>();
      const trainCoach = { id: 15849 };
      jest.spyOn(trainCoachService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trainCoach });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trainCoachService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCoachType', () => {
      it('should forward to coachTypeService', () => {
        const entity = { id: 7940 };
        const entity2 = { id: 18089 };
        jest.spyOn(coachTypeService, 'compareCoachType');
        comp.compareCoachType(entity, entity2);
        expect(coachTypeService.compareCoachType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTrain', () => {
      it('should forward to trainService', () => {
        const entity = { id: 7549 };
        const entity2 = { id: 8615 };
        jest.spyOn(trainService, 'compareTrain');
        comp.compareTrain(entity, entity2);
        expect(trainService.compareTrain).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
