import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CoordinateSystemViewComponent } from './coordinate-system-view.component';

describe('CoordinateSystemViewComponent', () => {
  let component: CoordinateSystemViewComponent;
  let fixture: ComponentFixture<CoordinateSystemViewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CoordinateSystemViewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CoordinateSystemViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
