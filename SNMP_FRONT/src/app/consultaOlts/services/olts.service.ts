import { Injectable, OnInit } from '@angular/core';
import { OntResponse } from '../interfaces/ResponseOnt';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { RequestOnt } from '../interfaces/RequestOnt';
import { BehaviorSubject, Observable, Subject, tap } from 'rxjs';
import { RequestGuardarOnt } from '../interfaces/RequestGuardarOnt';
import { GuardarOntResponse } from '../interfaces/OntGuardarResponse';
import { Olts } from 'src/app/model/names.olts';
import listOnts, { OntInventarioResponse } from './dataDummy';
import { AppUrlSettings } from 'src/app/services/AppUrlSettings';
import { GenericResponse } from '../components/dialog-inventario/dialog-inventario.component';

@Injectable({
  providedIn: 'root'
})
export class OltSincronizacionService implements OnInit {
  private ontsConciliar: OntInventarioResponse[] = new Array<OntInventarioResponse>();

  private notify$: Subject<string> = new Subject<string>();
  private responseOnt$: Subject<OntResponse[]> = new Subject<OntResponse[]>();

  private readonly URL: string = "http://localhost:9081/snmp-monitor";
  public observer$!: BehaviorSubject<OntInventarioResponse[]>;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.observer$ = this.populateData();
  }

  /**
   * Obtiene las ontstanto para pdm como nce.
   * Param1: idOlt
   * Param2: ipOlt
   * Param3: fechaIni
   * Param4: fechaFin
   * @returns 
   */
  obtenerOnts(request: RequestOnt) {
    const { idOlt, ipOlt, fechaIni, fechaFin } = request;
    const REQUEST_RESOURCE = `${AppUrlSettings.GET_RECHAZADAS_OLT}/${idOlt}/${ipOlt}/${fechaIni}/${fechaFin}`
    const REQUEST_API = `${AppUrlSettings.BASE_API}/${REQUEST_RESOURCE}`

    return this.http.get<OntResponse[]>(`${REQUEST_API}`)
  }

  /**
   * Guarda en el inventario final la ont
   */
  homologarOnt(request: RequestGuardarOnt) {
    const { numeroSerie, tipo } = request;
    const REQUEST_RESOURCE = `insertInventario/${numeroSerie}/${tipo}`
    const REQUEST_API = `${this.URL}/${REQUEST_RESOURCE}`

    this.http.get<GuardarOntResponse>(`${this.URL}/${REQUEST_API}`);
  }

  private cleanOntsResponse(data: OntResponse[]): OntResponse[] {
    let ontList = data.map(item => item)
    const ontCandidatas = ontList.filter(ont => ont.inventario === true);
    return ontCandidatas;
  }



  poleoOlt(idOlt: number): Observable<GenericResponse> {
    return this.http.get<GenericResponse>(AppUrlSettings.BASE_API_LOGIN + AppUrlSettings.GET_RECHAZADAS_OLT_NCE + `/${idOlt}`);
  }

  getAceptadosInventario(settings?: { idOlt: number, ip: string, fechaIni: string, fechaFin: string }) {
    if (settings) {
      const params = `/${settings.idOlt}/${settings.ip}/${settings.fechaIni}/${settings.fechaFin}`;
      let resource = `${AppUrlSettings.BASE_API}${AppUrlSettings.GET_RECHAZADOS_INVENTARIO_FINAL}${params}`;
      return this.http.get<any>(resource);
    }
    return this.populateData();
  }

  moverOntInventario(numero_serie: string, tipo: string) {
    let resource = `${AppUrlSettings.BASE_API}${AppUrlSettings.MOVER_ONT_INVENTARIO_FINAL}/${numero_serie}/${tipo}`;
    return this.http.get<{ sms: string, cod: number }>(resource);
  }

  populateData() {
    if (!this.ontsConciliar
      || this.ontsConciliar.length === 0) {
      this.ontsConciliar = listOnts;
    }
    return new BehaviorSubject<OntInventarioResponse[]>(this.ontsConciliar);
  }
}

