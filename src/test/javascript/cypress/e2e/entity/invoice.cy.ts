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

describe('Invoice e2e test', () => {
  const invoicePageUrl = '/invoice';
  const invoicePageUrlPattern = new RegExp('/invoice(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const invoiceSample = {
    invoiceNumber: 'of effector',
    transactionId: 'however ick up',
    receiptNumber: 'speedily yahoo worriedly',
    invoiceDate: '2025-07-12T00:34:49.149Z',
    transactionType: 'DEBIT',
    transactionAmount: 30326.25,
    transactionStatus: 'PENDING',
    customerDetails: 'ha schedule rear',
  };

  let invoice;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/invoices+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/invoices').as('postEntityRequest');
    cy.intercept('DELETE', '/api/invoices/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (invoice) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/invoices/${invoice.id}`,
      }).then(() => {
        invoice = undefined;
      });
    }
  });

  it('Invoices menu should load Invoices page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('invoice');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Invoice').should('exist');
    cy.url().should('match', invoicePageUrlPattern);
  });

  describe('Invoice page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(invoicePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Invoice page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/invoice/new$'));
        cy.getEntityCreateUpdateHeading('Invoice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', invoicePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/invoices',
          body: invoiceSample,
        }).then(({ body }) => {
          invoice = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/invoices+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [invoice],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(invoicePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Invoice page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('invoice');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', invoicePageUrlPattern);
      });

      it('edit button click should load edit Invoice page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Invoice');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', invoicePageUrlPattern);
      });

      it('edit button click should load edit Invoice page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Invoice');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', invoicePageUrlPattern);
      });

      it('last delete button click should delete instance of Invoice', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('invoice').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', invoicePageUrlPattern);

        invoice = undefined;
      });
    });
  });

  describe('new Invoice page', () => {
    beforeEach(() => {
      cy.visit(`${invoicePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Invoice');
    });

    it('should create an instance of Invoice', () => {
      cy.get(`[data-cy="invoiceNumber"]`).type('partial');
      cy.get(`[data-cy="invoiceNumber"]`).should('have.value', 'partial');

      cy.get(`[data-cy="transactionId"]`).type('gah utilization monster');
      cy.get(`[data-cy="transactionId"]`).should('have.value', 'gah utilization monster');

      cy.get(`[data-cy="receiptNumber"]`).type('instead appropriate');
      cy.get(`[data-cy="receiptNumber"]`).should('have.value', 'instead appropriate');

      cy.get(`[data-cy="invoiceDate"]`).type('2025-07-11T07:50');
      cy.get(`[data-cy="invoiceDate"]`).blur();
      cy.get(`[data-cy="invoiceDate"]`).should('have.value', '2025-07-11T07:50');

      cy.get(`[data-cy="transactionType"]`).select('CREDIT');

      cy.get(`[data-cy="transactionAmount"]`).type('17901.08');
      cy.get(`[data-cy="transactionAmount"]`).should('have.value', '17901.08');

      cy.get(`[data-cy="transactionStatus"]`).select('FAILED');

      cy.get(`[data-cy="customerDetails"]`).type('display');
      cy.get(`[data-cy="customerDetails"]`).should('have.value', 'display');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        invoice = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', invoicePageUrlPattern);
    });
  });
});
