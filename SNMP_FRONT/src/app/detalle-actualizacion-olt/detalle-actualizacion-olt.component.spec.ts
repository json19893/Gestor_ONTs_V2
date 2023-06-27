import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleActualizacionOltComponent } from './detalle-actualizacion-olt.component';

describe('DetalleActualizacionOltComponent', () => {
  let component: DetalleActualizacionOltComponent;
  let fixture: ComponentFixture<DetalleActualizacionOltComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetalleActualizacionOltComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetalleActualizacionOltComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
