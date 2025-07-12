import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('TrainCoach e2e test', () => {
  const trainCoachPageUrl = '/train-coach';
  const trainCoachPageUrlPattern = new RegExp('/train-coach(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const trainCoachSample = { trainNumber: 'rudely', seatCapacity: 27394, availableSeats: 22232, farePrice: 30793.7 };

  let trainCoach;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/train-coaches+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/train-coaches').as('postEntityRequest');
    cy.intercept('DELETE', '/api/train-coaches/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (trainCoach) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/train-coaches/${trainCoach.id}`,
      }).then(() => {
        trainCoach = undefined;
      });
    }
  });

  it('TrainCoaches menu should load TrainCoaches page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('train-coach');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TrainCoach').should('exist');
    cy.url().should('match', trainCoachPageUrlPattern);
  });

  describe('TrainCoach page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(trainCoachPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TrainCoach page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/train-coach/new$'));
        cy.getEntityCreateUpdateHeading('TrainCoach');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trainCoachPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/train-coaches',
          body: trainCoachSample,
        }).then(({ body }) => {
          trainCoach = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/train-coaches+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [trainCoach],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(trainCoachPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TrainCoach page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('trainCoach');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trainCoachPageUrlPattern);
      });

      it('edit button click should load edit TrainCoach page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TrainCoach');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trainCoachPageUrlPattern);
      });

      it('edit button click should load edit TrainCoach page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TrainCoach');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trainCoachPageUrlPattern);
      });

      it('last delete button click should delete instance of TrainCoach', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('trainCoach').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trainCoachPageUrlPattern);

        trainCoach = undefined;
      });
    });
  });

  describe('new TrainCoach page', () => {
    beforeEach(() => {
      cy.visit(`${trainCoachPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TrainCoach');
    });

    it('should create an instance of TrainCoach', () => {
      cy.get(`[data-cy="trainNumber"]`).type('barge skyline');
      cy.get(`[data-cy="trainNumber"]`).should('have.value', 'barge skyline');

      cy.get(`[data-cy="seatCapacity"]`).type('22150');
      cy.get(`[data-cy="seatCapacity"]`).should('have.value', '22150');

      cy.get(`[data-cy="availableSeats"]`).type('25487');
      cy.get(`[data-cy="availableSeats"]`).should('have.value', '25487');

      cy.get(`[data-cy="farePrice"]`).type('22980.83');
      cy.get(`[data-cy="farePrice"]`).should('have.value', '22980.83');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        trainCoach = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', trainCoachPageUrlPattern);
    });
  });
});
