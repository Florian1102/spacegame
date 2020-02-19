import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RenamePlanetComponent } from './rename-planet.component';

describe('RenamePlanetComponent', () => {
  let component: RenamePlanetComponent;
  let fixture: ComponentFixture<RenamePlanetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RenamePlanetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RenamePlanetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
