import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultaOltsComponent } from './consultaOlts.component';

describe('ConsultaOltsComponent', () => {
  let component: ConsultaOltsComponent;
  let fixture: ComponentFixture<ConsultaOltsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConsultaOltsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsultaOltsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
