import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TrainDetailComponent } from './train-detail.component';

describe('Train Management Detail Component', () => {
  let comp: TrainDetailComponent;
  let fixture: ComponentFixture<TrainDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrainDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./train-detail.component').then(m => m.TrainDetailComponent),
              resolve: { train: () => of({ id: 7549 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TrainDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrainDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load train on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TrainDetailComponent);

      // THEN
      expect(instance.train()).toEqual(expect.objectContaining({ id: 7549 }));
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
