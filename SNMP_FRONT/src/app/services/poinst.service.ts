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
  constructor(private http: HttpClient) { }

  getRegiones(): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_REGIONES);

  }
  getOltsByRegion(idRegion: any, tipo: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_ONTS_BY_REGION + idRegion + "/" + tipo);

  }

  findOlt(data: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.post(AppUrlSettings.BASE_API + AppUrlSettings.FIND_OLT, data);

  }

  findOnt(data: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.post(AppUrlSettings.BASE_API + AppUrlSettings.FIND_ONT, data);

  }

  getOntsByOltsUp(idOlt: any, estatus: any, tipo: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_ONTS_BY_OLTS_UP_DOWN + idOlt + "/" + estatus + "/" + tipo);

  }

  finOntsByIdAll(idOlt: any, tipo: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_ONTS_ALL + idOlt + "/" + tipo);

  }
  getHistoricoCambios(idOlt: any, tipo: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_HISTORICO_CAMBIOS + idOlt + "/" + tipo);

  }

  getTotalesByTecnologia(tipo: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_TOTALES_TECNOLOGIA + tipo);

  }
  getTotalesActivo(tipo: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_TOTALES_ACTIVO + tipo);

  }
  getTotalesByOlt(idOlt: any, tipo: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_TOTALES_BY_OLT + idOlt + "/" + tipo);

  }

  getNameOlts(regex: String): Observable<Olts[]> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<Olts[]>(AppUrlSettings.BASE_API + AppUrlSettings.GET_NAME_OLT + regex);

  }

  getAliasOnts(regex: String): Observable<Onts[]> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<Onts[]>(AppUrlSettings.BASE_API + AppUrlSettings.GET_ALIAS_ONT + regex);
  }

  getIpOlts(regex: String): Observable<Olts[]> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<Olts[]>(AppUrlSettings.BASE_API + AppUrlSettings.GET_IP_OLT + regex);

  }

  getSerieOnts(regex: String): Observable<Onts[]> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<Onts[]>(AppUrlSettings.BASE_API + AppUrlSettings.GET_SERIE_ONT + regex);
  }

  getSerieOntsAcDetalleRegex(tipo: any, regex: any): Observable<ActualizacionDetalle[]> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<ActualizacionDetalle[]>(AppUrlSettings.BASE_API + AppUrlSettings.GET_REGEX_ACTUALIZACION + tipo + "/" + regex);
  }
  getOntDetalleAc(tipo: any, regex: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_ONT_DETALLE_ACT + tipo + "/" + regex);
  }

  getDetalleActualizacion(tipo: any, skip: any, limit: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_DETALLE_ACTUALIZACION + tipo + "/" + skip + "/" + limit);

  }
  getDetalleActuacionData(tipo: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_DETALLE_ACTUALIZACION_DATA + tipo);

  }

  getDetalleActuacionSerie(tipo: any,ns:any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_DETALLE_ACTUALIZACION_SERIE + tipo+"/"+ns);

  }

  getMetricas(oid: String, id_olt: number): Observable<any> {
    /*var headers = new HttpHeaders({
         'Authorization': token,
         'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_METRICAS + id_olt + "/" + oid);

  }

  getOlts(): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_OLTS);

  }

  updateStatus(data: any): Observable<Actualizacion> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/

    return this.http.post<Actualizacion>(AppUrlSettings.BASE_API + AppUrlSettings.UPDATE_STATUS, data);

  }

  validaUsuario(data: any): Observable<ValidaUser> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/

    return this.http.post<ValidaUser>(AppUrlSettings.BASE_API + AppUrlSettings.VALIDA_USUARIO, data);

  }

  login(data: any): Observable<ValidaUser> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.post<ValidaUser>(AppUrlSettings.BASE_API_LOGIN + AppUrlSettings.LOGIN, data);

  }

  updateEstatus(idOlt: any, estatus: any,usuario:any): Observable<ValidaUser> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<ValidaUser>(AppUrlSettings.BASE_API + AppUrlSettings.UPDATE_ESTATUS_OLT + idOlt + "/" + estatus+"/"+usuario);

  }

  descubrimiento(data: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.post<any>(AppUrlSettings.BASE_API_DESCUBRIMIENTO + AppUrlSettings.DESCUBRIMIENTO_MANUAL, data);
  }
  poleoMetrica(data: any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.post<any>(AppUrlSettings.BASE_API_DESCUBRIMIENTO + AppUrlSettings.POLEO_METRICAS_MANUAL,data);
  }
  validaMaximo(): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.VALIDA_MAXIMO);
  }

  getDetalleDescubrimiento(): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.DETALLE_DES_MANUAL );
  }

  detalleMetricas(): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.DETALLES_METRICAS);
  }

  changeMetricas(idMetrica:any,idBloque:any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.CAMBIA_BLOQUE_METRICA+idMetrica+"/block/"+idBloque);
  }

  removeMetricas(idMetrica:any,idBloque:any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.DESASIGNA_BLOQUE_METRICA+idMetrica+"/block/"+idBloque);
  }

  getArchivo(archivo:any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.get<any>(AppUrlSettings.BASE_API + AppUrlSettings.GET_ARCHIVO+"/"+archivo);
  }

  
  poleoMetricaOid(data:any): Observable<any> {
    /*var headers = new HttpHeaders({
      'Authorization': token,
      'mode':'no-cors'});*/
    return this.http.post<any>(AppUrlSettings.BASE_API_DESCUBRIMIENTO + AppUrlSettings.POLEO_METRICA_OID,data);
  }
  




  
}
