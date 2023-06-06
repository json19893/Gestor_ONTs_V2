import { AfterContentInit, Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { Olts } from 'src/app/model/names.olts';
import { OntResponse } from '../../interfaces/ResponseOnt';
import { MAT_DIALOG_DATA, MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { pointService } from 'src/app/services/poinst.service';
import { Observable, Observer, Subscription, catchError, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { NgxSpinnerService } from 'ngx-spinner';
import * as moment from 'moment';
import { OltSincronizacionService } from '../../services/olts.service';


export interface OntElement {
  _id: string;
  oid: string;
  uid: string;
  valor: string;
  id_olt: number;
  id_metrica: number;
  fecha_poleo: Date;
  fecha_modificacion: Date;
  fecha_descubrimiento: Date;
  estatus: number;
  id_ejecucion: string;
  id_region: number;
  frame: number;
  slot: number;
  port: number;
  id_puerto: string;
  numero_serie: string;
  tecnologia: string;
  index: string;
  indexFSP: string;
  descripcion: string;
  error: boolean;
  alias: string;
  tipo: string;
  lastDownTime: string;
  descripcionAlarma: string;
  actualizacion: number;
  vip: number;
  sa: boolean;
  inventario: boolean;
}

export interface GenericResponse { sms: string, cod: number }
export interface Settings { idOlt: number, ip: string, fechaIni: string, fechaFin: string }

@Component({
  selector: 'app-dialog-inventario',
  templateUrl: './dialog-inventario.component.html',
  styleUrls: ['./dialog-inventario.component.css']
})
export class DialogInventarioComponent implements OnInit {
  public oltSeleccionada!: Olts;

  dt!: OntElement[];
  public estaSincronizandose: boolean = false;
  //Fechas de sincronizacion
  public ultimaActualizacion!: Date;

  displayedColumns: string[] = [
    'numero_serie', 'oid',
    'frame', 'slot', 'port',
    'fecha_descubrimiento', 'acciones'
  ];

  public dataSource!: MatTableDataSource<OntElement>;
  private poleoObserver$!: Observable<GenericResponse>;
  private topic$!: Observable<GenericResponse>;

  isLoadingResults: boolean = false;
  isRateLimitReached: boolean = false;

  public now!: string;
  public monthAgo = new Date().setMonth(new Date().getMonth() - 1);


  myGroup = new FormGroup({
    fechaIni: new FormControl<string>('2020-05-06'),
    fechaFin: new FormControl<string>('2020-06-06')
  });

  status!: string[];

  constructor(
    private pointService: pointService,
    public dialog: MatDialog,
    private spinner: NgxSpinnerService,
    public dialogRef: MatDialogRef<DialogInventarioComponent>,
    @Inject(MAT_DIALOG_DATA) public state: { sync: boolean, olt: Olts, topic: Observable<GenericResponse> },
    private service: OltSincronizacionService) { }



  ngOnInit(): void {
    this.spinner.hide();
    let { olt, sync, topic } = this.state;
    this.oltSeleccionada = olt;

  }

  filtrarPorRangoFechas() {


    const fechaIni = this.myGroup.value.fechaIni;
    const fechaFin = this.myGroup.value.fechaFin;

    const date1 = moment(fechaIni).toDate();
    const date2 = moment(fechaFin).toDate();


    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
    //this.dataSource.paginator = this.paginator;

    //Aqui filtra y despues 
    let ontResponse: OntResponse[] = [];
    let pureArray = this.dt.map(t => t);

    let list: OntResponse[] = [];

    for (const iterator of pureArray.values()) {

      if (fechaFin != null
        && fechaIni != null
        && iterator.fecha_descubrimiento >= date1
        && iterator.fecha_descubrimiento <= date2) {
        console.log(iterator)
        list.push(iterator);
      }
    }

    // this.dataSource = new MatTableDataSource<OntResponse>(list);*/
  }

  moverAInventario(numero_serie: string, tipo: string) {
  }

  request() {
    return this.service.poleoOlt(this.oltSeleccionada.id_olt);
  }

  handleError(handleError: HttpErrorResponse) {
    return throwError(() => new Error(handleError.error));
  }

  async sincronizar() {
    //Si esta activo el procesamiento: preguntar al usuario si desea interrumpirlo
    this.estaSincronizandose = true;

    console.log('Sincronizando con el servidor...');

    this.topic$ = new Observable<GenericResponse>((subscriber) => {
      setTimeout(() => {
        clearInterval(intervalId);
        this.estaSincronizandose = false;
        subscriber.next({ sms: "success", cod: 0 });
      }, 60000);
    });

    this.topic$.subscribe((response) => {
      console.log(response);
      this.service.getAceptadosInventario().subscribe((data) => {
        this.dataSource = new MatTableDataSource<OntElement>(data);
      });
    });

    let intervalId = setInterval(() => {
      console.log('running process');
      this.pointService.getArchivo(2)
        .subscribe((archivo) => {
          this.status = archivo;
        });
    }, 5000);
  }
}


