import { BehaviorSubject } from 'rxjs';
import { Http, Response, Headers } from '@angular/http';
import { HttpClientModule, HttpClient, HttpHeaders, HttpParams } from '@angular/common/http'
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { Component, OnInit, OnDestroy, Injectable } from '@angular/core';
import { AppUrlSettings } from './AppUrlSettings';
import { DataSource } from '@angular/cdk/collections';
import { AnyCatcher } from 'rxjs/internal/AnyCatcher';
import { RelativeStrengthIndex } from '@amcharts/amcharts5/.internal/charts/stock/indicators/RelativeStrengthIndex';
import { Olts } from '../model/names.olts';
import { Onts } from '../model/alias.onts';
import { ActualizacionDetalle } from '../model/actualizacion.detalle';
import { Actualizacion, ValidaUser } from '../detalleOnt/interfaces/atcualizacion-response';
@Injectable({
  providedIn: 'root'
})
export class pointService {
  public to: any;
  
  constructor(private http: HttpClient) {

    
   }

  getRegiones(): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_REGIONES,{headers});

  }
  getOltsByRegion(idRegion: any, tipo: any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_ONTS_BY_REGION + idRegion + "/" + tipo,{headers});

  }

  findOlt(data: any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.post(AppUrlSettings.BASE_API + AppUrlSettings.FIND_OLT, data,{headers});

  }

  findOnt(data: any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.post(AppUrlSettings.BASE_API + AppUrlSettings.FIND_ONT, data,{headers});

  }

  getOntsByOltsUp(idOlt: any, estatus: any, tipo: any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_ONTS_BY_OLTS_UP_DOWN + idOlt + "/" + estatus + "/" + tipo,{headers});

  }

  finOntsByIdAll(idOlt: any, tipo: any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_ONTS_ALL + idOlt + "/" + tipo,{headers});

  }
  getHistoricoCambios(idOlt: any, tipo: any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_HISTORICO_CAMBIOS + idOlt + "/" + tipo,{headers});

  }

  getTotalesByTecnologia(tipo: any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_TOTALES_TECNOLOGIA + tipo,{headers});

  }
  getTotalesActivo(tipo: any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_TOTALES_ACTIVO + tipo,{headers});

  }
  getTotalesByOlt(idOlt: any, tipo: any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_TOTALES_BY_OLT + idOlt + "/" + tipo,{headers});

  }

  getNameOlts(regex: String): Observable<Olts[]> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<Olts[]>(AppUrlSettings.BASE_API + AppUrlSettings.GET_NAME_OLT + regex,{headers});

  }

  getAliasOnts(regex: String): Observable<Onts[]> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<Onts[]>(AppUrlSettings.BASE_API + AppUrlSettings.GET_ALIAS_ONT + regex,{headers});
  }

  getIpOlts(regex: String): Observable<Olts[]> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<Olts[]>(AppUrlSettings.BASE_API + AppUrlSettings.GET_IP_OLT + regex,{headers});

  }

  getSerieOnts(regex: String): Observable<Onts[]> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<Onts[]>(AppUrlSettings.BASE_API + AppUrlSettings.GET_SERIE_ONT + regex,{headers});
  }

  getSerieOntsAcDetalleRegex(tipo: any, regex: any): Observable<ActualizacionDetalle[]> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<ActualizacionDetalle[]>(AppUrlSettings.BASE_API + AppUrlSettings.GET_REGEX_ACTUALIZACION + tipo + "/" + regex,{headers});
  }
  getOntDetalleAc(tipo: any, regex: any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_ONT_DETALLE_ACT + tipo + "/" + regex,{headers});
  }

  getDetalleActualizacion(tipo: any, skip: any, limit: any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_DETALLE_ACTUALIZACION + tipo + "/" + skip + "/" + limit,{headers});

  }
  getDetalleActuacionData(tipo: any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_DETALLE_ACTUALIZACION_DATA + tipo,{headers});

  }

  getDetalleActuacionSerie(tipo: any,ns:any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_DETALLE_ACTUALIZACION_SERIE + tipo+"/"+ns,{headers});

  }

  getMetricas(oid: String, id_olt: number): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_METRICAS + id_olt + "/" + oid,{headers});

  }

  getOlts(): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_OLTS,{headers});

  }

  updateStatus(data: any): Observable<Actualizacion> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});

    return this.http.post<Actualizacion>(AppUrlSettings.BASE_API + AppUrlSettings.UPDATE_STATUS, data,{headers});

  }

 

 

  updateEstatus(idOlt: any, estatus: any,usuario:any): Observable<ValidaUser> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<ValidaUser>(AppUrlSettings.BASE_API + AppUrlSettings.UPDATE_ESTATUS_OLT + idOlt + "/" + estatus+"/"+usuario,{headers});

  }

  
  validaMaximo(): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.VALIDA_MAXIMO,{headers});
  }

  getDetalleDescubrimiento(): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.DETALLE_DES_MANUAL,{headers} );
  }

  detalleMetricas(): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.DETALLES_METRICAS,{headers});
  }

  changeMetricas(idMetrica:any,idBloque:any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.CAMBIA_BLOQUE_METRICA+idMetrica+"/block/"+idBloque,{headers});
  }

  removeMetricas(idMetrica:any,idBloque:any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.DESASIGNA_BLOQUE_METRICA+idMetrica+"/block/"+idBloque,{headers});
  }

  getArchivo(archivo:any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_ARCHIVO+"/"+archivo,{headers});
  }

  
  

  cambios(idOlt:any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.CAMBIOS+idOlt,{headers});
  }
  actualizaOltByOnt(idOlt:any,serie:any): Observable<any> {
    var headers = new HttpHeaders({
      'mode':'no-cors'});
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.ACTUALIZA_OLT_BY_ONT+idOlt+"/"+serie,{headers});
  }
  

/*Servicios de login  */

login(data: any): Observable<ValidaUser> {
  var headers = new HttpHeaders({
    'mode':'no-cors'});
  return this.http.post<ValidaUser>(AppUrlSettings.BASE_API + AppUrlSettings.LOGIN, data,{headers});

}


/*Servicios de descubrimiento */

descubrimiento(data: any): Observable<any> {
  var headers = new HttpHeaders({
    'mode':'no-cors'});
  return this.http.post<any>(AppUrlSettings.BASE_API + AppUrlSettings.DESCUBRIMIENTO_MANUAL, data,{headers});
}
poleoMetrica(data: any): Observable<any> {
  var headers = new HttpHeaders({
    'mode':'no-cors'});
  return this.http.post<any>(AppUrlSettings.BASE_API + AppUrlSettings.POLEO_METRICAS_MANUAL,data,{headers});
}

poleoMetricaOid(data:any): Observable<any> {
  var headers = new HttpHeaders({
    'mode':'no-cors'});
  return this.http.post<any>(AppUrlSettings.BASE_API + AppUrlSettings.POLEO_METRICA_OID,data,{headers});
}
  
}

