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

describe('CoachType e2e test', () => {
  const coachTypePageUrl = '/coach-type';
  const coachTypePageUrlPattern = new RegExp('/coach-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const coachTypeSample = { coachId: 'yowza finer than', coachName: 'although unwieldy', seatCapacity: 25237, fareMultiplier: 28421.27 };

  let coachType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/coach-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/coach-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/coach-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (coachType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/coach-types/${coachType.id}`,
      }).then(() => {
        coachType = undefined;
      });
    }
  });

  it('CoachTypes menu should load CoachTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('coach-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CoachType').should('exist');
    cy.url().should('match', coachTypePageUrlPattern);
  });

  describe('CoachType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(coachTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CoachType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/coach-type/new$'));
        cy.getEntityCreateUpdateHeading('CoachType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', coachTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/coach-types',
          body: coachTypeSample,
        }).then(({ body }) => {
          coachType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/coach-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [coachType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(coachTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CoachType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('coachType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', coachTypePageUrlPattern);
      });

      it('edit button click should load edit CoachType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CoachType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', coachTypePageUrlPattern);
      });

      it('edit button click should load edit CoachType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CoachType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', coachTypePageUrlPattern);
      });

      it('last delete button click should delete instance of CoachType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('coachType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', coachTypePageUrlPattern);

        coachType = undefined;
      });
    });
  });

  describe('new CoachType page', () => {
    beforeEach(() => {
      cy.visit(`${coachTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CoachType');
    });

    it('should create an instance of CoachType', () => {
      cy.get(`[data-cy="coachId"]`).type('tenderly');
      cy.get(`[data-cy="coachId"]`).should('have.value', 'tenderly');

      cy.get(`[data-cy="coachName"]`).type('up circle evince');
      cy.get(`[data-cy="coachName"]`).should('have.value', 'up circle evince');

      cy.get(`[data-cy="seatCapacity"]`).type('23410');
      cy.get(`[data-cy="seatCapacity"]`).should('have.value', '23410');

      cy.get(`[data-cy="fareMultiplier"]`).type('19167.36');
      cy.get(`[data-cy="fareMultiplier"]`).should('have.value', '19167.36');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        coachType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', coachTypePageUrlPattern);
    });
  });
});
