import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpaceshipResourcesComponent } from './spaceship-resources.component';

describe('SpaceshipResourcesComponent', () => {
  let component: SpaceshipResourcesComponent;
  let fixture: ComponentFixture<SpaceshipResourcesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpaceshipResourcesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpaceshipResourcesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
