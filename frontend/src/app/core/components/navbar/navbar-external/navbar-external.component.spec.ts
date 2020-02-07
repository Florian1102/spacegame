import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarExternalComponent } from './navbar-external.component';

describe('NavbarExternalComponent', () => {
  let component: NavbarExternalComponent;
  let fixture: ComponentFixture<NavbarExternalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NavbarExternalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavbarExternalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
