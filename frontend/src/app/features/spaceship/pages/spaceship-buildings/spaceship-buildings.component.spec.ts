import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpaceshipBuildingsComponent } from './spaceship-buildings.component';

describe('SpaceshipBuildingsComponent', () => {
  let component: SpaceshipBuildingsComponent;
  let fixture: ComponentFixture<SpaceshipBuildingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpaceshipBuildingsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpaceshipBuildingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
