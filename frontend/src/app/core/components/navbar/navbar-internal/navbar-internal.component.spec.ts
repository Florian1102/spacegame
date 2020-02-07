import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarInternalComponent } from './navbar-internal.component';

describe('NavbarInternalComponent', () => {
  let component: NavbarInternalComponent;
  let fixture: ComponentFixture<NavbarInternalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NavbarInternalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavbarInternalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
