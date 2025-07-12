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

describe('Admin e2e test', () => {
  const adminPageUrl = '/admin';
  const adminPageUrlPattern = new RegExp('/admin(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const adminSample = {
    adminId: 'neatly',
    username: 'um since pointless',
    password: 'inasmuch anxiously shady',
    email: 'Shanelle.Jerde6@hotmail.com',
    isActive: true,
  };

  let admin;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/admins+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/admins').as('postEntityRequest');
    cy.intercept('DELETE', '/api/admins/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (admin) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/admins/${admin.id}`,
      }).then(() => {
        admin = undefined;
      });
    }
  });

  it('Admins menu should load Admins page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('admin');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Admin').should('exist');
    cy.url().should('match', adminPageUrlPattern);
  });

  describe('Admin page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(adminPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Admin page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/admin/new$'));
        cy.getEntityCreateUpdateHeading('Admin');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', adminPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/admins',
          body: adminSample,
        }).then(({ body }) => {
          admin = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/admins+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [admin],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(adminPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Admin page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('admin');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', adminPageUrlPattern);
      });

      it('edit button click should load edit Admin page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Admin');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', adminPageUrlPattern);
      });

      it('edit button click should load edit Admin page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Admin');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', adminPageUrlPattern);
      });

      it('last delete button click should delete instance of Admin', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('admin').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', adminPageUrlPattern);

        admin = undefined;
      });
    });
  });

  describe('new Admin page', () => {
    beforeEach(() => {
      cy.visit(`${adminPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Admin');
    });

    it('should create an instance of Admin', () => {
      cy.get(`[data-cy="adminId"]`).type('if whoever');
      cy.get(`[data-cy="adminId"]`).should('have.value', 'if whoever');

      cy.get(`[data-cy="username"]`).type('preheat oxidize');
      cy.get(`[data-cy="username"]`).should('have.value', 'preheat oxidize');

      cy.get(`[data-cy="password"]`).type('testing notwithstanding');
      cy.get(`[data-cy="password"]`).should('have.value', 'testing notwithstanding');

      cy.get(`[data-cy="email"]`).type('Skye_Hane1@yahoo.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Skye_Hane1@yahoo.com');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        admin = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', adminPageUrlPattern);
    });
  });
});
