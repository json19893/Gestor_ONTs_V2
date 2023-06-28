import { TestBed } from '@angular/core/testing';

import { DetalleActualizacionOltServiceService } from './detalle-actualizacion-olt-service.service';

describe('DetalleActualizacionOltServiceService', () => {
  let service: DetalleActualizacionOltServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DetalleActualizacionOltServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
