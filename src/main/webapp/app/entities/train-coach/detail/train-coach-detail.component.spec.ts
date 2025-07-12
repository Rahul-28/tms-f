import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrainCoachDetailComponent } from './train-coach-detail.component';

describe('TrainCoach Management Detail Component', () => {
  let comp: TrainCoachDetailComponent;
  let fixture: ComponentFixture<TrainCoachDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrainCoachDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./train-coach-detail.component').then(m => m.TrainCoachDetailComponent),
              resolve: { trainCoach: () => of({ id: 15849 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TrainCoachDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrainCoachDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load trainCoach on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TrainCoachDetailComponent);

      // THEN
      expect(instance.trainCoach()).toEqual(expect.objectContaining({ id: 15849 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
