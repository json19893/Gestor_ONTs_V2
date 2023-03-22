import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleOntComponent } from './detalleOnt.component';

describe('DetalleOntComponent', () => {
  let component: DetalleOntComponent;
  let fixture: ComponentFixture<DetalleOntComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetalleOntComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetalleOntComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
