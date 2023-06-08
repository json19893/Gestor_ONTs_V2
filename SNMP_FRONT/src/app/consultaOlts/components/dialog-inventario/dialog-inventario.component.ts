import { AfterContentInit, AfterViewInit, Component, EventEmitter, Inject, OnInit, Output } from '@angular/core';
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
import { NumberInput } from '@angular/cdk/coercion';


export interface OntInventarioResponse {
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
export class DialogInventarioComponent implements OnInit, AfterViewInit {
  intervalId!: any;
  resultsLength: NumberInput = 0;
  @Output() newItemEvent = new EventEmitter<boolean>(false);


  onKeydown($event: KeyboardEvent) {
    if ($event.key === "Enter") {
      alert('enter event')
    }
  }

  public oltSeleccionada!: Olts;

  dt!: OntInventarioResponse[];
  public estaSincronizandose: boolean = false;
  //Fechas de sincronizacion
  public ultimaActualizacion!: Date;

  displayedColumns: string[] = [
    'numero_serie', 'oid',
    'frame', 'slot', 'port',
    'fecha_descubrimiento', 'acciones'
  ];

  public dataSource!: MatTableDataSource<OntInventarioResponse>;
  private poleoObserver$!: Observable<GenericResponse>;
  private topic$!: Subscription;

  isLoadingResults: boolean = false;
  isRateLimitReached: boolean = false;

  public now!: string;
  public monthAgo = new Date().setMonth(new Date().getMonth() - 1);

  time = new Date();

  myGroup = new FormGroup({
    fechaIni: new FormControl<string>('2020-05-06'),
    fechaFin: new FormControl<string>('2023-06-06')
  });

  // element: HTMLElement = document.getElementById('auto_trigger') as HTMLElement;

  constructor(
    private service: pointService,
    public dialog: MatDialog,
    private spinner: NgxSpinnerService,
    public dialogRef: MatDialogRef<DialogInventarioComponent>,
    @Inject(MAT_DIALOG_DATA) public state: { sync: boolean, olt: Olts, topic: Observable<GenericResponse> }) { }

  ngAfterViewInit(): void {
    this.sincronizar();
    this.newItemEvent.emit(true);
  }



  ngOnInit(): void {
    this.spinner.hide();
    let { olt, sync, topic } = this.state;
    this.oltSeleccionada = olt;

    // Sirve para inicializar el relog:
    this.intervalId = setInterval(() => {
      this.time = new Date();
    }, 1000);
    //Que se lance por las que ya estan en el area temporal
  }

  filtrarPorRangoFechas() {
    const fechaIni = this.myGroup.value.fechaIni;
    const fechaFin = this.myGroup.value.fechaFin;

    const date1 = moment(fechaIni).toDate();
    const date2 = moment(fechaFin).toDate();


    console.log(date1);
    console.log(date2);

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }

    //this.dataSource.paginator = this.paginator;

    let ontResponse: OntResponse[] = [];
    let copyArray = this.dataSource.data.map(t => t);

    let list: OntInventarioResponse[] = [];

    for (const iterator of copyArray.values()) {
      let fecha = new Date(iterator.fecha_descubrimiento).getTime();
      if (fecha >= date1.getTime()
        && fecha <= date2.getTime()) {
        console.log(iterator)
        list.push(iterator);
      }
    }
    console.log({ list });

    this.dataSource = new MatTableDataSource<OntInventarioResponse>(list);
  }

  moverAInventario(numero_serie: string, tipo: string) {
  }

  request() {
    // return this.service.poleoOlt(this.oltSeleccionada.id_olt);
  }

  handleError(handleError: HttpErrorResponse) {
    return throwError(() => new Error(handleError.error));
  }

  sincronizar() {
    this.isLoadingResults = true;
    this.isRateLimitReached = true;

    //Si esta activo el procesamiento: preguntar al usuario si desea interrumpirlo
    this.estaSincronizandose = true;
    this.newItemEvent.emit(this.estaSincronizandose);

    console.log('Sincronizando con el servidor...');

    const { id_olt, ip } = this.oltSeleccionada;

    this.topic$ = this.service.sincronizarNCE(id_olt).subscribe((data) => {
      this.estaSincronizandose = false;
      this.newItemEvent.emit(this.estaSincronizandose);

      this.isLoadingResults = false;
      this.isRateLimitReached = false;

      if (data.cod == 0) {
        this.ultimaActualizacion = this.time;
        //Correr el otro proceso:
        this.preguntarInventarioFinal().subscribe((data) => {
          this.isLoadingResults = false;
          this.isRateLimitReached = false;
          if (data.length == 0) {
            //No hay data
            console.log('No hay data');
          } else {
            console.log(data);
            this.resultsLength = data.length;
            this.dataSource = new MatTableDataSource<OntInventarioResponse>(data)
          }
        });

      }
    });
  }

  preguntarInventarioFinal() {
    const { id_olt, ip } = this.oltSeleccionada;

    const fechaIni = this.myGroup.value.fechaIni;
    const fechaFin = this.myGroup.value.fechaFin;

    const date1 = moment(fechaIni).format();
    const date2 = moment(fechaFin).format();
    const settings: Settings = {
      idOlt: id_olt,
      ip: ip,
      fechaIni: date1,
      fechaFin: date2
    }
    return this.service.getAceptadosInventario(settings);
  }
}

