import { Injectable } from '@angular/core';
import { OntResponse } from '../interfaces/ResponseOnt';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { RequestOnt } from '../interfaces/RequestOnt';
import listOnts from './dataDummy'
import { Observable, map } from 'rxjs';
import { RequestGuardarOnt } from '../interfaces/RequestGuardarOnt';
import { GuardarOntResponse } from '../interfaces/OntGuardarResponse';

@Injectable({
  providedIn: 'root'
})
export class OltsService {
  private ontsConciliar: OntResponse[] = new Array<OntResponse>();
  private subject!: BehaviorSubject<OntResponse[]>;
  private readonly URL: string = "http://localhost:9081";

  constructor(private http: HttpClient) {
    this.populateData();
  }

  /**
   * Obtiene las onts que son candidatas a agregar al inventario fnial del gestor
   * Param1: idOlt
   * Param2: ipOlt
   * Param3: fechaIni
   * Param4: fechaFin
   * @returns 
   */
  obtenerOnts(request: RequestOnt) {
    const { idOlt, ipOlt, fechaIni, fechaFin } = request;
    const REQUEST_RESOURCE = `getRechazadasByOltInventario/${idOlt}/${ipOlt}/${fechaIni}/${fechaFin}`
    const REQUEST_API = `${this.URL}/${REQUEST_RESOURCE}`
    return this.http
      .get<OntResponse[]>(`${REQUEST_API}`);
      // .pipe(
      //   map(this.cleanOntsResponse)
      // );
    // return this.subject;
  }

  /**
   * Guarda en el inventario final la ont
   */
  homologarOnt(request: RequestGuardarOnt) {
    const { numeroSerie, tipo } = request;
    const REQUEST_RESOURCE = `insertInventario/${numeroSerie}/${tipo}`
    const REQUEST_API = `${this.URL}/${REQUEST_RESOURCE}`
    alert(REQUEST_API);
    //return this.http.get<GuardarOntResponse>(`${this.URL}/${REQUEST_API}`);
  }

  private cleanOntsResponse(data: OntResponse[]): OntResponse[] {
    let ontList = data.map(item => item)
    const ontCandidatas = ontList.filter(ont => ont.inventario === true);
    return ontCandidatas;
  }

  obtenerObservable() {
    this.subject.asObservable();
  }


  populateData() {
    if (!this.ontsConciliar
      || this.ontsConciliar.length === 0) {
      this.ontsConciliar = listOnts;
    }
    this.subject = new BehaviorSubject<OntResponse[]>(this.ontsConciliar);
  }
}


