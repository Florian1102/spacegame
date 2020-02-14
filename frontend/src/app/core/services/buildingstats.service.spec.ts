import { TestBed } from '@angular/core/testing';

import { BuildingstatsService } from './buildingstats.service';

describe('BuildingstatsService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BuildingstatsService = TestBed.get(BuildingstatsService);
    expect(service).toBeTruthy();
  });
});
