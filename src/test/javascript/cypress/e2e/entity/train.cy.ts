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

describe('Train e2e test', () => {
  const trainPageUrl = '/train';
  const trainPageUrlPattern = new RegExp('/train(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const trainSample = {
    trainNumber: 'yippee lest separately',
    trainName: 'final shameless',
    origin: 'drug meh',
    destination: 'condense pro along',
    intermediateStop: 'while',
    serviceStartDate: '2025-07-12',
    serviceEndDate: '2025-07-12',
    serviceType: 'DAILY',
    departureTime: '2025-07-11T08:46:11.331Z',
    arrivalTime: '2025-07-11T21:10:52.656Z',
    basicPrice: 11267.94,
    isActive: false,
  };

  let train;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/trains+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/trains').as('postEntityRequest');
    cy.intercept('DELETE', '/api/trains/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (train) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/trains/${train.id}`,
      }).then(() => {
        train = undefined;
      });
    }
  });

  it('Trains menu should load Trains page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('train');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Train').should('exist');
    cy.url().should('match', trainPageUrlPattern);
  });

  describe('Train page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(trainPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Train page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/train/new$'));
        cy.getEntityCreateUpdateHeading('Train');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trainPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/trains',
          body: trainSample,
        }).then(({ body }) => {
          train = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/trains+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/trains?page=0&size=20>; rel="last",<http://localhost/api/trains?page=0&size=20>; rel="first"',
              },
              body: [train],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(trainPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Train page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('train');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trainPageUrlPattern);
      });

      it('edit button click should load edit Train page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Train');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trainPageUrlPattern);
      });

      it('edit button click should load edit Train page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Train');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trainPageUrlPattern);
      });

      it('last delete button click should delete instance of Train', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('train').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', trainPageUrlPattern);

        train = undefined;
      });
    });
  });

  describe('new Train page', () => {
    beforeEach(() => {
      cy.visit(`${trainPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Train');
    });

    it('should create an instance of Train', () => {
      cy.get(`[data-cy="trainNumber"]`).type('deep');
      cy.get(`[data-cy="trainNumber"]`).should('have.value', 'deep');

      cy.get(`[data-cy="trainName"]`).type('accidentally');
      cy.get(`[data-cy="trainName"]`).should('have.value', 'accidentally');

      cy.get(`[data-cy="origin"]`).type('ah');
      cy.get(`[data-cy="origin"]`).should('have.value', 'ah');

      cy.get(`[data-cy="destination"]`).type('trim carefully');
      cy.get(`[data-cy="destination"]`).should('have.value', 'trim carefully');

      cy.get(`[data-cy="intermediateStop"]`).type('oof knowledgeable about');
      cy.get(`[data-cy="intermediateStop"]`).should('have.value', 'oof knowledgeable about');

      cy.get(`[data-cy="serviceStartDate"]`).type('2025-07-11');
      cy.get(`[data-cy="serviceStartDate"]`).blur();
      cy.get(`[data-cy="serviceStartDate"]`).should('have.value', '2025-07-11');

      cy.get(`[data-cy="serviceEndDate"]`).type('2025-07-11');
      cy.get(`[data-cy="serviceEndDate"]`).blur();
      cy.get(`[data-cy="serviceEndDate"]`).should('have.value', '2025-07-11');

      cy.get(`[data-cy="serviceType"]`).select('WEEKLY');

      cy.get(`[data-cy="departureTime"]`).type('2025-07-11T08:53');
      cy.get(`[data-cy="departureTime"]`).blur();
      cy.get(`[data-cy="departureTime"]`).should('have.value', '2025-07-11T08:53');

      cy.get(`[data-cy="arrivalTime"]`).type('2025-07-11T22:00');
      cy.get(`[data-cy="arrivalTime"]`).blur();
      cy.get(`[data-cy="arrivalTime"]`).should('have.value', '2025-07-11T22:00');

      cy.get(`[data-cy="basicPrice"]`).type('24218.19');
      cy.get(`[data-cy="basicPrice"]`).should('have.value', '24218.19');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        train = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', trainPageUrlPattern);
    });
  });
});
