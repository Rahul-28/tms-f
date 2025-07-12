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

describe('Passenger e2e test', () => {
  const passengerPageUrl = '/passenger';
  const passengerPageUrlPattern = new RegExp('/passenger(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const passengerSample = {
    passengerName: 'admonish pfft',
    age: 76,
    coachNumber: 'inspect concerning ouch',
    seatNumber: 'against except while',
  };

  let passenger;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/passengers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/passengers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/passengers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (passenger) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/passengers/${passenger.id}`,
      }).then(() => {
        passenger = undefined;
      });
    }
  });

  it('Passengers menu should load Passengers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('passenger');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Passenger').should('exist');
    cy.url().should('match', passengerPageUrlPattern);
  });

  describe('Passenger page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(passengerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Passenger page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/passenger/new$'));
        cy.getEntityCreateUpdateHeading('Passenger');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passengerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/passengers',
          body: passengerSample,
        }).then(({ body }) => {
          passenger = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/passengers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [passenger],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(passengerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Passenger page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('passenger');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passengerPageUrlPattern);
      });

      it('edit button click should load edit Passenger page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Passenger');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passengerPageUrlPattern);
      });

      it('edit button click should load edit Passenger page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Passenger');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passengerPageUrlPattern);
      });

      it('last delete button click should delete instance of Passenger', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('passenger').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', passengerPageUrlPattern);

        passenger = undefined;
      });
    });
  });

  describe('new Passenger page', () => {
    beforeEach(() => {
      cy.visit(`${passengerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Passenger');
    });

    it('should create an instance of Passenger', () => {
      cy.get(`[data-cy="passengerName"]`).type('inasmuch lone amid');
      cy.get(`[data-cy="passengerName"]`).should('have.value', 'inasmuch lone amid');

      cy.get(`[data-cy="age"]`).type('11');
      cy.get(`[data-cy="age"]`).should('have.value', '11');

      cy.get(`[data-cy="coachNumber"]`).type('continually hopelessly atrium');
      cy.get(`[data-cy="coachNumber"]`).should('have.value', 'continually hopelessly atrium');

      cy.get(`[data-cy="seatNumber"]`).type('hungrily miserable extremely');
      cy.get(`[data-cy="seatNumber"]`).should('have.value', 'hungrily miserable extremely');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        passenger = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', passengerPageUrlPattern);
    });
  });
});
