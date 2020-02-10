import { TestBed } from '@angular/core/testing';

import { CoordinateSystemService } from './coordinate-system.service';

describe('CoordinateSystemService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CoordinateSystemService = TestBed.get(CoordinateSystemService);
    expect(service).toBeTruthy();
  });
});
