import { TestBed } from '@angular/core/testing';

import { TradeOfferService } from './trade-offer.service';

describe('TradeOfferService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TradeOfferService = TestBed.get(TradeOfferService);
    expect(service).toBeTruthy();
  });
});
