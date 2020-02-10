import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanetResourcesComponent } from './planet-resources.component';

describe('PlanetResourcesComponent', () => {
  let component: PlanetResourcesComponent;
  let fixture: ComponentFixture<PlanetResourcesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PlanetResourcesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PlanetResourcesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
