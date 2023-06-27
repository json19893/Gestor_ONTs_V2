import { AfterViewChecked, Component, ElementRef, Inject, OnInit, ViewChild } from '@angular/core';
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
export class DialogInventarioComponent implements OnInit, AfterViewChecked {
  usuario:string = "";
  archivo:string = "";
  dt!: OntElement[];
  olt!: Olts;
  dataSource!: MatTableDataSource<any>;
  resultsLength = 0;
  isLoadingResults: boolean = false;
  isRateLimitReached: boolean = false;
  showLogs:boolean = false;
  interval?: any;
  public requestPoleoOid:poleoMetricaOidRequest | undefined;
  public demo1TabIndex = 0;

  data:boolean = false;

  displayedColumns: string[] = ['numero_serie', 'oid', /*'frame', 'slot', 'port',*/ 'fecha_descubrimiento', 'acciones'];
  @ViewChild('scrollMe') 
  private myScrollContainer?: ElementRef;

  myGroup = new FormGroup({
    fechaIni: new FormControl<Date>(new Date()),
    fechaFin: new FormControl<Date>(new Date()),
  });

 


  constructor(public dialogRef: MatDialogRef<String>,
    @Inject(MAT_DIALOG_DATA) public obj: { olt: Olts, fecInic:string, fecFinal:string, usuario:string, detalle:boolean, logs:boolean},
    private service: pointService) {
      
      dialogRef.disableClose = true;
  }
  
  
  ngOnInit(): void {
    // this.dt = this.obj.list;
    this.olt = this.obj.olt;
    //this.dataSource = new MatTableDataSource<any>(this.obj.list);
    //this.usuario = localStorage.getItem('usuario')!;

    console.log(this.obj.detalle)

    this.showLogs =  this.obj.logs;

   
    if(this.obj.detalle){
      this.interval = setInterval(() => this.getaArchivo(), 1000);
      this.getDetalle();  
    }else{
      this.interval = setInterval(() => this.getaArchivo(), 1000);
      this.poleoOlt();
    }
  }

  ngAfterViewChecked() {        
    this.scrollToBottom();        
  } 

  scrollToBottom(): void {
    try {
        this.myScrollContainer!.nativeElement.scrollTop = this.myScrollContainer?.nativeElement.scrollHeight;
    } catch(err) { }                 
  }

  poleoOlt(){
    console.log('servicio 1');
    let olt  = this.obj.olt;

    this.service.poleoOlt(olt.id_olt, this.obj.usuario).subscribe((data) => {
      console.log(data);

      if (data.cod == 0) {
        this.service.getAceptadosInventario(olt.id_olt, olt.ip, this.obj.fecInic, this.obj.fecFinal, this.obj.usuario).subscribe((resp) => {
          console.log('servicio 2');
          this.dataSource = new MatTableDataSource<any>(resp); 
          this.data =  this.dataSource.data.length === 0;
          //this.showLogs = false;  
          this.demo1TabIndex=1; 
          this.dialogRef.disableClose = false;   
        },()=>{clearInterval( this.interval); this.dialogRef.disableClose = false;},()=>{clearInterval( this.interval); this.dialogRef.disableClose = false;} );
      }
    }, ()=>{clearInterval( this.interval); this.dialogRef.disableClose = false;},()=>{clearInterval( this.interval); this.dialogRef.disableClose = false;});

  }

  getDetalle(){
    let  olt = this.obj.olt;    
    this.service.getAceptadosInventario(olt.id_olt, olt.ip, this.obj.fecInic, this.obj.fecFinal, this.obj.usuario).subscribe((resp) => {
      console.log('servicio 2');
      this.dataSource = new MatTableDataSource<any>(resp);
      this.data =  this.dataSource.data.length === 0;
      this.dialogRef.disableClose = false;
    }, ()=>{clearInterval( this.interval)}, ()=>{clearInterval( this.interval); this.dialogRef.disableClose = false; });
    
  }
  
  getaArchivo() {
      this.service.getArchivo(10,this.obj.usuario ).subscribe(
      res => {
          this.archivo = res;
      }) 
  }

  


  marcarOnt() {
    throw new Error('Method not implemented.');
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
