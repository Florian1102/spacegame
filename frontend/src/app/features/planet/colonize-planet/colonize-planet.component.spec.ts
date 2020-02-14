import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ColonizePlanetComponent } from './colonize-planet.component';

describe('ColonizePlanetComponent', () => {
  let component: ColonizePlanetComponent;
  let fixture: ComponentFixture<ColonizePlanetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ColonizePlanetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ColonizePlanetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
