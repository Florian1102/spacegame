import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FlyToPlanetComponent } from './fly-to-planet.component';

describe('FlyToPlanetComponent', () => {
  let component: FlyToPlanetComponent;
  let fixture: ComponentFixture<FlyToPlanetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FlyToPlanetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FlyToPlanetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
