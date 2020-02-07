import { TestBed } from '@angular/core/testing';

import { SpaceshipService } from './spaceship.service';

describe('SpaceshipService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SpaceshipService = TestBed.get(SpaceshipService);
    expect(service).toBeTruthy();
  });
});
