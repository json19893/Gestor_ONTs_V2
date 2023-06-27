import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppUrlSettings } from 'src/app/services/AppUrlSettings';

@Injectable({
  providedIn: 'root'
})
export class DetalleActualizacionOltServiceService {

  constructor(private httpClient: HttpClient) { }

  public getDetallesActualizacion() {

    
      var headers = new HttpHeaders({
        'mode': 'no-cors',
        'Access-Control-Allow-Origin': '*'
      });
      return this.httpClient.get<any>(AppUrlSettings.BASE_API+AppUrlSettings.GET_DETALLE_ACTUALIZACION_OLT, { headers });
    }
    
/*
    return Observable.create(observer => {
      this.httpClient.get(AppUrlSettings.GET_DETALLE_ACTUALIZACION_OLT).subscribe(
        data => {
          if (data.status == 200) {
            //console.log(`---------------------------Servicio ${accion} TRUE: `, data)
            let dataResponse = { status: 200, datos: data.body }
            observer.next(dataResponse);
            observer.complete();
          } else {
            //console.log(`---------------------------Servicio ${accion} FALSE: `, data)
            let dataResponse = { status: 500, datos: data.body }
            observer.next(dataResponse);
            observer.complete();
          }
        },
        error => {
          //console.log(`---------------------------Servicio ${accion} ERROR: `, error)
          let dataResponse = error.error;
          observer.next(dataResponse);
          observer.complete();
        });
    });
  }*/

}
