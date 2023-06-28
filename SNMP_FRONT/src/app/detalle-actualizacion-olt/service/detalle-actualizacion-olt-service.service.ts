import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppUrlSettings } from 'src/app/services/AppUrlSettings';

@Injectable({
  providedIn: 'root'
})
export class DetalleActualizacionOltServiceService {

  constructor(private httpClient: HttpClient) { }

  public getDetallesActualizacion(ipOlt: any) {

    
      var headers = new HttpHeaders({
        'mode': 'no-cors',
        'Access-Control-Allow-Origin': '*'
      });
      return this.httpClient.get<any>(AppUrlSettings.BASE_API+AppUrlSettings.GET_DETALLE_ACTUALIZACION_OLT+"/"+ipOlt, { headers });
    }
    

}
