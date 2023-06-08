import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { Olts } from 'src/app/model/names.olts';
import { OntResponse } from '../../interfaces/ResponseOnt';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { pointService } from 'src/app/services/poinst.service';
import { poleoMetricaOidRequest } from 'src/app/model/poleoMetricaOidRequest';

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

@Component({
  selector: 'app-dialog-inventario',
  templateUrl: './dialog-inventario.component.html',
  styleUrls: ['./dialog-inventario.component.css']
})
export class DialogInventarioComponent implements OnInit {
  usuario:string = "";
  dt!: OntElement[];

  public requestPoleoOid:poleoMetricaOidRequest | undefined;

  marcarOnt() {
    throw new Error('Method not implemented.');
  }


  displayedColumns: string[] = [
    'numero_serie', 'oid',
    'frame', 'slot', 'port',
    'fecha_descubrimiento', 'acciones'
  ];

  dataSource!: MatTableDataSource<any>;
  resultsLength = 0;

  isLoadingResults: boolean = false;
  isRateLimitReached: boolean = false;

  myGroup = new FormGroup({
    fechaIni: new FormControl<Date>(new Date()),
    fechaFin: new FormControl<Date>(new Date()),
  });

  olt!: Olts;

  constructor(public dialogRef: MatDialogRef<String>,
    @Inject(MAT_DIALOG_DATA) public obj: { olt: Olts, list: OntElement[] },
    private service: pointService) {
      //setInterval(() => this.getaArchivo(), 1000);
    }
  
  // getaArchivo() {
  //     this.service.getArchivo(1).subscribe(
  //       res => {
  //         this.archivo = res;
  //   })
  
  // }

  ngOnInit(): void {
    this.dt = this.obj.list;
    this.olt = this.obj.olt;
    this.dataSource = new MatTableDataSource<any>(this.obj.list);
    this.usuario = localStorage.getItem('usuario')!;
  }

  filtrarPorRangoFechas(event: Event) {
    let dt = document.getElementById('dates');

    const fechaIni = this.myGroup.value.fechaIni;
    const fechaFin = this.myGroup.value.fechaFin;

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
        && iterator.fecha_descubrimiento >= fechaIni
        && iterator.fecha_descubrimiento <= fechaFin) {
        console.log(iterator)
        list.push(iterator);
      }
    }

    this.dataSource = new MatTableDataSource<OntResponse>(list);
  }

  moverAInventario(numero_serie: string, tipo: string, ejecucion:string ) {

    this.service.moverOntInventario(numero_serie, tipo, ejecucion)
      .subscribe((response) => {
        if (response.cod == 0) {
          this.service.getAceptadosInventario(
            this.olt.id_olt,
            this.olt.ip,
            new Date().toISOString(),
            new Date().toISOString(), this.usuario!).subscribe(resp => {
              alert('flujo completo');
              this.actualizarFrame(numero_serie);
              
            })
        }
      })
  }

  actualizarFrame(serie:string){
      
    this.requestPoleoOid=new poleoMetricaOidRequest(serie,13);      
    this.service.poleoMetricaOid(this.requestPoleoOid).subscribe(
        res =>{}
    )
  }


}
