import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import { MatPaginator, PageEvent } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { MatTableDataSource } from "@angular/material/table";
import { Router } from "@angular/router";
import { NgxSpinnerService } from 'ngx-spinner';
import { pointService } from '../services/poinst.service';
import { ActualizacionDetalle } from '../model/actualizacion.detalle';
import { MatSnackBar } from "@angular/material/snack-bar";
import { MatDialog } from "@angular/material/dialog";
export interface clasificacion {
  ip: string;
  numeroSerie: string;
  frame: number;
  slot: number;
  port:number;
  fechaActualizacion:Date;
  descripcionAlarma:string;
  causa:string;
}





@Component({
    selector: 'app-actualizacion',
    templateUrl: './actualizacion.component.html',
    styleUrls: ['./actualizacion.component.css']
  
  })

export class ActualizacionComponent  implements OnInit{ 
  //displayedColumns: string[] = ['tipo', 'numeroSerie', 'region', 'estatus','fechaDescubrimiento','desActualizacion'];
  displayedColumns: string[] = ['ip', 'numeroSerie', 'frame', 'slot','port','uid','fechaActualizacion','descripcionAlarma','causa','accion'];
  ELEMENT_DATA: clasificacion[] = []
  dataSource = new MatTableDataSource<clasificacion>;
  //public dataSource :any;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort: MatSort | undefined;
  public mostrar:any;
   /*public skip:any;
  public limit:any;
  length = 0;
 pageSize = 10;
  pageIndex = 0;
  pageSizeOptions = [10];
  public serieOnts:any
  hidePageSize = false;
  showPageSizeOptions = false;
  showFirstLastButtons = false;
  */
  disabled = false;
  form1: FormGroup | any;
  //pageEvent!: PageEvent;
  optionsSerieOnts: ActualizacionDetalle[] = [];
  constructor(
    private router: Router,
    private spinner: NgxSpinnerService,
    private service: pointService,
    private fb: FormBuilder,
    private _snackbar2: MatSnackBar,
    public dialog: MatDialog,) {
      this.form1 = this.fb.group(
        {
          numeroSerie: [null],
        }
      )
  }

  ngOnInit() {
    this.spinner.show();

    this.mostrar = localStorage.getItem('mostrar');
    if(this.mostrar==null||this.mostrar==undefined){
      this.mostrar='E';
     }
   // this.limit=10;
   // this.skip=0;

    //this.getDetalleActualizacion(this.mostrar,this.skip,this.limit);
    this.getDetalleActualizacionData( this.mostrar);
  }


   
 /* getDetalleActualizacion(mostrar:any,skip:any,limit:any) {
    this.spinner.show();
    this.mostrar = localStorage.getItem('mostrar');
    if(this.mostrar==undefined){
      this.mostrar="E"
    }

    ELEMENT_DATA.length = ELEMENT_DATA.length -ELEMENT_DATA.length
    this.service.getDetalleActualizacion(mostrar,skip,limit).subscribe(
      data => {
        this.dataSource=[];
        let dat;
        for (let d in data.detalle) {
          dat = {
            tip:data.detalle[d].tipo ,
            numeroSerie: data.detalle[d].numero_serie,
            region:"Región "+data.detalle[d].id_region ,
            estatus: data.detalle[d].estatus=='1'?'UP':'DOWN',
            desActualizacion:data.detalle[d].actualizacion==null?'Se registro por pdm':data.detalle[d].actualizacion=='1'?'Cambio a estatus arriba':data.detalle[d].actualizacion=='2'?'Cambio a estatus abajo':'Cambio a tipo E',
            fechaDescubrimiento:data.detalle[d].fecha_descubrimiento  
          }
      
          this.dataSource.push(dat);
        
        }
        if(this.length==0){
        this.length = data.total;
        }
  
            this.spinner.hide();
      },
      err => console.error(err)
    );
  }*/
  getDetalleActualizacionData(mostrar:any) {
    this.spinner.show();
    this.mostrar = localStorage.getItem('mostrar');
    if(this.mostrar==undefined){
      this.mostrar="E"
    }

    this.service.getDetalleActuacionData(mostrar).subscribe(
      data => {
        let dat
        this.ELEMENT_DATA = data;
        this.dataSource = new MatTableDataSource<clasificacion>(this.ELEMENT_DATA);
        this.dataSource!.paginator = this.paginator!;
        this.spinner.hide();
      },
      err => console.error(err)
    );
    console.log(this.dataSource)
  }

  /*applyFilter() {
    this.spinner.show();
    this.mostrar = localStorage.getItem('mostrar');
    if(this.mostrar==undefined){
      this.mostrar="E"
    }

    if (this.form1.value.numeroSerie == '' || this.form1.value.numeroSerie == null) {
      this.spinner.hide();
      return this.error('Favor de ingresar por lo menos un valor');
    }
    ELEMENT_DATA.length = ELEMENT_DATA.length -ELEMENT_DATA.length
    this.service.getOntDetalleAc(this.mostrar,this.form1.value.numeroSerie).subscribe(
      data => {
        console.log("data::: "+data)
        this.dataSource=[];
        let dat;
        for (let d in data) {
          dat = {
            tip:data[d].tipo ,
            numeroSerie: data[d].numero_serie,
            region:"Región "+data[d].id_region ,
            estatus: data[d].estatus=='1'?'UP': data[d].estatus=='1'?'DOWN':'DISCONNECT',
            desActualizacion:data[d].actualizacion==null?'Se registro por pdm':data[d].actualizacion=='1'?'Cambio a estatus arriba':data[d].actualizacion=='2'?'Cambio a estatus abajo':data[d].actualizacion=='3'?'Cambio a tipo E':"Cambio de estatus por proceso",
            fechaDescubrimiento:data[d].fecha_descubrimiento  
          }
      
          this.dataSource.push(dat);
        
        }
        if(this.length==0){
        this.length = data.total;
        }
  
            this.spinner.hide();
      },
      err => console.error(err)
    );
  }
  error(error: any) {
    this._snackbar2.open(error, 'Cerrar', {
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    })
  }*/
  /*findSerieOnts() {
    this.mostrar = localStorage.getItem('mostrar');
    if(this.mostrar==undefined){
      this.mostrar="E"
    }
    this.serieOnts = this.form1.value.numeroSerie;
    if (this.serieOnts.length > 10) {
      this.spinner.show();
      this.service.getSerieOntsAcDetalleRegex(this.mostrar,this.serieOnts).subscribe(
        (resp) => {
        
          this.optionsSerieOnts = resp;
          this.spinner.hide();
        }
      )
    }else{
      
      this.optionsSerieOnts = [];  
     
    }
  }

  handlePageEvent(e: PageEvent) {
    this.pageEvent = e;
    this.length = e.length;
    this.pageSize = e.pageSize;
    this.pageIndex = e.pageIndex;
    console.log("e.length::: "+e.length)
    console.log("e.pageIndex::: "+e.pageIndex)

    this.limit=(e.pageIndex+1)*e.pageSize
    this.skip=e.pageIndex*e.pageSize
    //this.paginator.pageIndex=e.pageIndex;
console.log("e.limit ::: "+this.limit);
console.log("e.skip ::: "+this.skip);
        this.getDetalleActualizacion(this.mostrar,this.skip,this.limit);  
     
  }*/

  getDetalle(ns:any){
    localStorage.setItem("serial",ns)
    this.dialog.open(detalleActualizacionDialog);
  }

}
@Component({
  selector: 'detalleActualizacion',
  templateUrl: './detalleActualizacion.html',
  styleUrls: ['./actualizacion.component.css']
})

export class detalleActualizacionDialog implements OnInit {
  displayedColumns: string[] = ['ip', 'numeroSerie', 'frame', 'slot','port','uid','fechaActualizacion','descripcionAlarma','causa','accion'];
  ELEMENT_DATA: clasificacion[] = []
  dataSource = new MatTableDataSource<clasificacion>;
  ns:any
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  public mostrar:any;
    constructor(
  
      private service: pointService,
      private spinner: NgxSpinnerService,
      private _snackbar: MatSnackBar
    ) {
     
  
    }
    ngOnInit() {
      this.mostrar = localStorage.getItem('mostrar');
      this.ns = localStorage.getItem('serial');
      if(this.mostrar==null||this.mostrar==undefined){
        this.mostrar='E';
       }
     this.getDetalleActuacionSerie( this.mostrar,this.ns);
    }
    getDetalleActuacionSerie(mostrar:any,ns:any) {
      this.spinner.show();
      this.ELEMENT_DATA.length = this.ELEMENT_DATA.length -this.ELEMENT_DATA.length
      this.mostrar = localStorage.getItem('mostrar');
      if(this.mostrar==undefined){
        this.mostrar="E"
      }
  
      this.service.getDetalleActuacionSerie(mostrar,ns).subscribe(
        data => {
          this.ELEMENT_DATA = data;
          this.dataSource = new MatTableDataSource<clasificacion>(this.ELEMENT_DATA);
          this.dataSource!.paginator = this.paginator!;
          this.spinner.hide();
        },
        err => console.error(err)
      );
      console.log(this.dataSource)
    }
  }