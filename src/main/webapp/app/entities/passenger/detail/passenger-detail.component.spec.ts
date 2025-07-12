import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PassengerDetailComponent } from './passenger-detail.component';

describe('Passenger Management Detail Component', () => {
  let comp: PassengerDetailComponent;
  let fixture: ComponentFixture<PassengerDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PassengerDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./passenger-detail.component').then(m => m.PassengerDetailComponent),
              resolve: { passenger: () => of({ id: 22165 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PassengerDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PassengerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load passenger on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PassengerDetailComponent);

      // THEN
      expect(instance.passenger()).toEqual(expect.objectContaining({ id: 22165 }));
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
