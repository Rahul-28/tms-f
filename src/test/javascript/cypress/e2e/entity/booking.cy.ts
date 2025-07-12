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

describe('Booking e2e test', () => {
  const bookingPageUrl = '/booking';
  const bookingPageUrlPattern = new RegExp('/booking(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const bookingSample = {
    pnrNumber: 'yuck till scope',
    bookingDate: '2025-07-11T10:41:38.815Z',
    travellingDate: '2025-07-11',
    boardingStation: 'fairly boohoo unhappy',
    destinationStation: 'artistic tune-up',
    boardingTime: '2025-07-11T14:37:58.597Z',
    arrivalTime: '2025-07-11T12:04:31.750Z',
    totalFare: 22887.28,
    bookingStatus: 'CONFIRMED',
    modeOfPayment: 'CREDIT_CARD',
    additionalServices: true,
    coachNumber: 'wash amongst',
    seatNumber: 'bookend blah',
  };

  let booking;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/bookings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bookings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bookings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (booking) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bookings/${booking.id}`,
      }).then(() => {
        booking = undefined;
      });
    }
  });

  it('Bookings menu should load Bookings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('booking');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Booking').should('exist');
    cy.url().should('match', bookingPageUrlPattern);
  });

  describe('Booking page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bookingPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Booking page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/booking/new$'));
        cy.getEntityCreateUpdateHeading('Booking');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bookingPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/bookings',
          body: bookingSample,
        }).then(({ body }) => {
          booking = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/bookings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/bookings?page=0&size=20>; rel="last",<http://localhost/api/bookings?page=0&size=20>; rel="first"',
              },
              body: [booking],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(bookingPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Booking page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('booking');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bookingPageUrlPattern);
      });

      it('edit button click should load edit Booking page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Booking');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bookingPageUrlPattern);
      });

      it('edit button click should load edit Booking page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Booking');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bookingPageUrlPattern);
      });

      it('last delete button click should delete instance of Booking', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('booking').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', bookingPageUrlPattern);

        booking = undefined;
      });
    });
  });

  describe('new Booking page', () => {
    beforeEach(() => {
      cy.visit(`${bookingPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Booking');
    });

    it('should create an instance of Booking', () => {
      cy.get(`[data-cy="pnrNumber"]`).type('ameliorate sandbar during');
      cy.get(`[data-cy="pnrNumber"]`).should('have.value', 'ameliorate sandbar during');

      cy.get(`[data-cy="bookingDate"]`).type('2025-07-11T13:47');
      cy.get(`[data-cy="bookingDate"]`).blur();
      cy.get(`[data-cy="bookingDate"]`).should('have.value', '2025-07-11T13:47');

      cy.get(`[data-cy="travellingDate"]`).type('2025-07-11');
      cy.get(`[data-cy="travellingDate"]`).blur();
      cy.get(`[data-cy="travellingDate"]`).should('have.value', '2025-07-11');

      cy.get(`[data-cy="boardingStation"]`).type('around whoa apt');
      cy.get(`[data-cy="boardingStation"]`).should('have.value', 'around whoa apt');

      cy.get(`[data-cy="destinationStation"]`).type('consistency');
      cy.get(`[data-cy="destinationStation"]`).should('have.value', 'consistency');

      cy.get(`[data-cy="boardingTime"]`).type('2025-07-11T09:41');
      cy.get(`[data-cy="boardingTime"]`).blur();
      cy.get(`[data-cy="boardingTime"]`).should('have.value', '2025-07-11T09:41');

      cy.get(`[data-cy="arrivalTime"]`).type('2025-07-12T02:24');
      cy.get(`[data-cy="arrivalTime"]`).blur();
      cy.get(`[data-cy="arrivalTime"]`).should('have.value', '2025-07-12T02:24');

      cy.get(`[data-cy="totalFare"]`).type('8942.6');
      cy.get(`[data-cy="totalFare"]`).should('have.value', '8942.6');

      cy.get(`[data-cy="bookingStatus"]`).select('COMPLETED');

      cy.get(`[data-cy="modeOfPayment"]`).select('DEBIT_CARD');

      cy.get(`[data-cy="additionalServices"]`).should('not.be.checked');
      cy.get(`[data-cy="additionalServices"]`).click();
      cy.get(`[data-cy="additionalServices"]`).should('be.checked');

      cy.get(`[data-cy="coachNumber"]`).type('whirlwind youthfully anaesthetise');
      cy.get(`[data-cy="coachNumber"]`).should('have.value', 'whirlwind youthfully anaesthetise');

      cy.get(`[data-cy="seatNumber"]`).type('swanling eyeglasses acquaintance');
      cy.get(`[data-cy="seatNumber"]`).should('have.value', 'swanling eyeglasses acquaintance');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        booking = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', bookingPageUrlPattern);
    });
  });
});
