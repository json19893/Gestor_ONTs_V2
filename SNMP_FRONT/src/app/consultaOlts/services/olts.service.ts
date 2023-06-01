import { Injectable } from '@angular/core';
import { OntResponse } from '../interfaces/ResponseOnt';
import { HttpClient } from '@angular/common/http';
import { RequestOnt } from '../interfaces/RequestOnt';
import { BehaviorSubject, Subject, tap } from 'rxjs';
import { RequestGuardarOnt } from '../interfaces/RequestGuardarOnt';
import { GuardarOntResponse } from '../interfaces/OntGuardarResponse';
import { Olts } from 'src/app/model/names.olts';
import listOnts from './dataDummy';

@Injectable({
  providedIn: 'root'
})
export class OltsService {
  private ontsConciliar: OntResponse[] = new Array<OntResponse>();
  private notify$: Subject<string> = new Subject<string>();
  private responseOnt$: Subject<OntResponse[]> = new Subject<OntResponse[]>();

  private readonly URL: string = "http://localhost:9081/snmp-monitor";
  public subject!: BehaviorSubject<OntResponse[]>;

  constructor(private http: HttpClient) { }

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
    const REQUEST_RESOURCE = `getRechazadasByOlt/${idOlt}/${ipOlt}/${fechaIni}/${fechaFin}`
    const REQUEST_API = `${this.URL}/${REQUEST_RESOURCE}`

    return this.http.get<OntResponse[]>(`${REQUEST_API}`)
    //return this.populateData();
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

  obtenerObservable() {
    return this.notify$.asObservable();
  }


  polearOLT(olt: Olts) {
    //Cuando acabes de polear me avisas para consumir otra api y renderizar.
    //http://localhost:9084/descubrimientoNCE/1
    //const REQUEST_RESOURCE = this.URL + `descubrimientoNCE/${olt.id_olt}`
    const REQUEST_RESOURCE = 'http://localhost:9084/descubrimientoNCE/1';


    return this.http.get(REQUEST_RESOURCE)
      .pipe(
        tap((response) => {
          this.notify$.next('POLEO_OLT');
          console.log(response);
        })
      );
  }

  populateData() {
    if (!this.ontsConciliar
      || this.ontsConciliar.length === 0) {
      this.ontsConciliar = listOnts;
    }
    return new BehaviorSubject<OntResponse[]>(this.ontsConciliar);
  }
}

