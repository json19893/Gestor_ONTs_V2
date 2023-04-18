
import { AfterViewInit, Component, Inject, OnInit, QueryList, ViewChild, ViewChildren,  } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import {pointService} from '../services/poinst.service';
import {poleoMetricaOidRequest} from '../model/poleoMetricaOidRequest'
import {getOnts}  from '../model/getOnts'
import { CookieService } from 'ngx-cookie-service';
import {animate, state, style, transition, trigger} from '@angular/animations';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
export interface up {
    _id: string;
    numero_serie:string;
    oid:string;
    fecha_descubrimiento: string;
    id_olt:number;
    estatus: string;
    id_ejecucion:string; 
    tipoCambio:string;   
    alias:string;
    frame:number;
    slot:number;
    puerto:number;
    uid:string;
    tipo:string;
    desEstatus:string;
    selected:boolean;
    fecha_ultima_caida: string;
    olts:oltsAsignacion[]
  }
  export interface oltsAsignacion {
    id_olt:number;
    ip:string;
    oid:string;

  }
  const ELEMENT_DATA:up[]= []
  const ELEMENT_DATA2:up[]= []

@Component({
selector:'app-detalle-ont',
templateUrl: './detalleOnt.component.html',
styleUrls: ['./detalleOnt.component.css'],
animations: [
  trigger('detailExpand', [
    state('collapsed', style({height: '0px', minHeight: '0'})),
    state('expanded', style({height: '*'})),
    transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
  ]),
],

})
export class DetalleOntComponent implements OnInit {
  public mostrar: any;
    public idOlt:any;
    public nombre:any;
    public metricas:any;
    public up:any;
    public dowm:any
    public totalOnt:any;
    public his:any;
    public ip:any;
    public icono:any;
    tipoCambio=false;
    public dataDetalle:any;
    public busqueda:any;
    public page:any;
    public nS:any;
    public al:any;
    tabla=false;
    public getOntsData:getOnts | undefined;
    public requestPoleoOid:poleoMetricaOidRequest | undefined;
    displayedColumns: string[] = [];

    public lastUpTime: any
    public upBytes: any
    public timeOut:any
    public upPackets:any
    public downPackets: any
    public dropUpPackets:any
    public dropDownPackets:any
    public cpu: any
    public memoria: any
    public profName: any
    public downBytes: any 
    public fechaPoleo: any
    public metrics:any
    public cambTabla:any
     asigna=true;
  public oltsAsig:any

    columnsToDisplayWithExpand : string[] = [];
    dataUp = new MatTableDataSource<up>(ELEMENT_DATA);
    dataDown = new MatTableDataSource<up>(ELEMENT_DATA2);
    @ViewChild(MatSort) sort: MatSort | undefined;
    width="75vw";
   public titulo:any;
    @ViewChild('tbl1',{static:false}) paginator!: MatPaginator;
  @ViewChild('tbl2',{static:false}) paginator2!: MatPaginator;
  expandedElement:any|null=null;
    constructor(
        private router:Router,
        private spinner:NgxSpinnerService,
        private service:pointService,
        private cookieService: CookieService,
        private _snackBar: MatSnackBar,
        public dialog: MatDialog,
        
        ){
    
        }
    ngOnInit() {
  
      this.getMetricas()
       this.dataDetalle=JSON.parse(localStorage.getItem("dataDetalle")!);   
      if(this.mostrar==null){
        this.mostrar='E';
       }
      this.busqueda=localStorage.getItem('busqueda');
      this.al= localStorage.getItem('alias');
      this.nS= localStorage.getItem('ns');
      this.page=localStorage.getItem('page');
      if(this.page==undefined){
        this.page=0;
      }

 
  if (this.busqueda==undefined ||this.busqueda!='1'){      
  this.idOlt= localStorage.getItem('idOlt');
  this.nombre= localStorage.getItem('nombre');
 
  this.mostrar = localStorage.getItem('mostrar');
  this.ip =localStorage.getItem('ip');
  this.getOntsByolAll()
  this.getTotalesOlt();
           
}else{
if(this.dataDetalle==undefined){
  if(this.dataDetalle==undefined||this.dataDetalle==null){
   /* setTimeout(() => {
      this.spinner.hide();
    }, 1000);*/
   }
  //this.getOntsSearch()
}else{
 
  this.llenaTabla(this.dataDetalle);
}
}
this.displayedColumns=['tipo','oid','frame','slot','puerto','uid','numeroSerie', 'alias', 'estatus','fecha','fechaUltimaCaida','desEstatus','acciones'];
 this.columnsToDisplayWithExpand= [...this.displayedColumns, 'expand'];
   this.titulo="TOTAL";
            this.icono="./assets/img/network.png";
            this.tabla=true;
    
    }


    ngAfterViewInit() {
      //this.paginator.pageIndex = this.page;
      this.dataUp.paginator = this.paginator;
      this.dataUp.sort = this.sort!;
    }

    getMetricas (){
      this.service.detalleMetricas().subscribe(
       res =>{
      this.metrics=res.entity;
      this.metrics=this.metrics.filter((nombre: { nombre: any; }) => nombre.nombre != "SERIAL NUMBER")
      }
      )
    }

    openDetalle() {
    const dialogo=  this.dialog.open(detalleEjecucionMetricaDialog);
      dialogo.afterClosed().subscribe(result =>{
        this.cambTabla=localStorage.getItem("cambiaTabla");
       window.location.reload()
      })
   
    }
    poleoMetrica(ns:any,idMetrica:any){
      this.requestPoleoOid=new poleoMetricaOidRequest(ns,idMetrica);
      this.openDetalle()
      this.service.poleoMetricaOid(this.requestPoleoOid).subscribe(
        res =>{
          this._snackBar.open(res.sms, "cerrar", {
            duration: 4000
          });
         
       }
       
       )

      
    }
      applyFilter(event: Event) {
        const filterValue = (event.target as HTMLInputElement).value;
        this.dataUp.filter = filterValue.trim().toLowerCase();
    
        if (this.dataUp.paginator) {
          this.dataUp.paginator.firstPage();
        }
      }
      getOntsByOlts(idOlt:any,valor:any,tipo:any){
        this.spinner.show();
        ELEMENT_DATA.length = ELEMENT_DATA.length - ELEMENT_DATA.length
        this.service.getOntsByOltsUp(idOlt,valor,tipo).subscribe(
          res => {
           
            let dat;
            for(let d in res){
              dat={
                _id: res[d]._id,
                numero_serie:res[d].numero_serie,
                oid:res[d].oid,
                fecha_descubrimiento: res[d].fecha_modificacion,
                id_olt:res[d].id_olt,
                estatus: res[d].estatus==1? "UP":res[d].estatus==0?"DISCONNECT":"DOWN",
                id_ejecucion:res[d].id_ejecucion,    
                tipoCambio:"--",
                alias:res[d].alias, 
                frame:res[d].frame,
                slot:res[d].slot,
                puerto:res[d].port,
                uid:res[d].uid,
                tipo:res[d].tipo,
                desEstatus:res[d].descripcionAlarma=='0-0-0,0:0:0.0,.0:0'?'1':res[d].descripcionAlarma,
                fecha_ultima_caida: res[d].lastDownTime=='0-0-0,0:0:0.0,.0:0'?'1 ':res[d].lastDownTime,
                selected:false,
                olts:[]
              }
              ELEMENT_DATA.push(dat);
            }
            this.dataUp.paginator = this.paginator;
            this.spinner.hide();
          },
          err => console.error(err)
        )
      }

      getOntsSearch(){
        this.dataDetalle=JSON.parse(localStorage.getItem("dataDetalle")!);
        //this.dataDetalle=JSON.parse(this.cookieService.get('dataDetalle')!);
        if(this.dataDetalle==undefined||this.dataDetalle==null){
          this.spinner.show();
         setTimeout(() => {
           this.spinner.hide();
         }, 1000);
        }
          this.mostrar = localStorage.getItem('mostrar');
        if(this.mostrar==null){
          this.mostrar='E';
         }
        
        this.getOntsData = new getOnts(this.nS,this.al,this.mostrar);
        ELEMENT_DATA.length = ELEMENT_DATA.length - ELEMENT_DATA.length
        this.service.findOnt( this.getOntsData).subscribe(
          res => {
            if(res.success){
            let dat;
            for(let d in res.listOnts){
              dat={
                _id: res.listOnts[d]._id,
                numero_serie:res.listOnts[d].numero_serie,
                oid:res.listOnts[d].oid,
                fecha_descubrimiento: res.listOnts[d].fecha_modificacion,
                id_olt:res.listOnts[d].id_olt,
                estatus: res[d].estatus==1? "UP":res[d].estatus==0?"DISCONNECT":"DOWN",
                id_ejecucion:res.listOnts[d].id_ejecucion,    
                tipoCambio:"--",
                alias:res.listOnts[d].alias, 
                frame:res.listOnts[d].frame,
                slot:res.listOnts[d].slot,
                puerto:res.listOnts[d].port,
                uid:res.listOnts[d].uid,
                tipo:res.listOnts[d].tipo,
                desEstatus:res.listOnts[d].descripcionAlarma,
                selected:res.listOnts[d].selected,
                fecha_ultima_caida: res[d].lastDownTime,
                olts:[]
      
              }
              ELEMENT_DATA.push(dat);
              this.dataUp.paginator = this.paginator;
              //localStorage.setItem("dataDetalle", JSON.stringify(ELEMENT_DATA));
            }
           
            localStorage.setItem("upDetalle", res.totales.arriba);
            localStorage.setItem("downDetalle", res.totales.abajo);
            localStorage.setItem("totalDetalle", res.totales.totalOlt);
            localStorage.setItem("cambiosDetalle", res.totales.cambios);
            localStorage.setItem('page',res.page);
            localStorage.setItem("nombre", res.nombre);
            localStorage.setItem('ip',res.ip);

            
           
            window.location.reload();
            
            
           
          }
       
          },
          err => console.error(err)
        )
      }

      llenaTabla(res:any){
      /* if(this.busqueda==1){
        this.spinner.show()
        setTimeout(() => {
          this.spinner.hide();
        }, 45000);
       }*/
        this.up= localStorage.getItem("upDetalle");
        this.dowm= localStorage.getItem("downDetalle");
        this.totalOnt= localStorage.getItem("totalDetalle");
        this.his=localStorage.getItem("cambiosDetalle", );
        this.nombre=localStorage.getItem("nombre");
        this.ip=localStorage.getItem("ip");
      console.log(this.ip);
        let dat;
        for(let d in res){
          dat={
            _id: res[d]._id,
            numero_serie:res[d].numero_serie,
            oid:res[d].oid,
            fecha_descubrimiento: res[d].fecha_modificacion,
            id_olt:res[d].id_olt,
            estatus: res[d].estatus==1? "UP":res[d].estatus==0?"DISCONNECT":"DOWN",
            id_ejecucion:res[d].id_ejecucion,    
            tipoCambio:"--",
            fecha_ultima_caida: res[d].lastDownTime,
            alias:res[d].alias, 
            frame:res[d].frame,
            slot:res[d].slot,
            puerto:res[d].port,
            uid:res[d].uid,
            tipo:res[d].tipo,
            desEstatus:res[d].descripcionAlarma,
            selected:res[d].selected,
            olts:[]
          }
          ELEMENT_DATA.push(dat);
        }
        this.dataUp.paginator = this.paginator;
        this.spinner.hide();
      }

      getTotalesOlt(){
        if(this.mostrar==null){
          this.mostrar='E';
         }
        this.service.getTotalesByOlt(this.idOlt,this.mostrar).subscribe(
          res => {
            this.up=res.arriba;
             this.dowm=res.abajo;
            this.totalOnt=res.totalOlt;
            this.his=res.cambios;
          },
          err => console.error(err)
        )

      }
      getHistByOlts(idOlt:any,tipo:any){
        this.spinner.show();
        ELEMENT_DATA.length = ELEMENT_DATA.length - ELEMENT_DATA.length
        this.service.getHistoricoCambios(idOlt,tipo).subscribe(
          res => {
   
            let dat;
            for(let d in res){
              dat={
                _id: res[d]._id,
                numero_serie:res[d].numero_serie,
                oid:res[d].oid,
                fecha_descubrimiento:  res[d].fecha_modificacion=='0-0-0,0:0:0.0,.0:0'?'1 ':res[d].fecha_modificacion,
                id_olt:res[d].id_olt,
                estatus: res[d].estatus==1? "UP":res[d].estatus==0?"DISCONNECT":"DOWN",
                id_ejecucion:res[d].id_ejecucion,    
                tipoCambio:res[d].tipoCambio,
                 alias:res[d].alias,
                         frame:res[d].frame,
                 slot:res[d].slot,
                 puerto:res[d].port,
                 uid:res[d].uid,
                 tipo:res[d].tipo,
                 desEstatus:res[d].descripcionAlarma,
                 fecha_ultima_caida: res[d].lastDownTime=='0-0-0,0:0:0.0,.0:0'?'1 ':res[d].lastDownTime,
                 selected:false,
                 olts:[]
              }
              ELEMENT_DATA.push(dat);
            }
            this.dataUp.paginator = this.paginator;
            this.spinner.hide();
          },
          err => console.error(err)
        )
      }
      getCambios(idOlt:any,tipo:any){
        this.spinner.show();
        ELEMENT_DATA.length = ELEMENT_DATA.length - ELEMENT_DATA.length
        this.service.cambios(idOlt).subscribe(
          res => {
     
           let dat;
            for(let d in res.onts){
              dat={
                _id: res.onts[d]._id,
                numero_serie:res.onts[d].numero_serie,
                oid:res.onts[d].oid,
                fecha_descubrimiento:res.onts[d].fecha_poleo=='0-0-0,0:0:0.0,.0:0'?'1 ':res.onts[d].fecha_poleo,
                id_olt:res.onts[d].id_olt,
                estatus: res.onts[d].estatus==1? "UP":res.onts[d]==0?"DISCONNECT":"DOWN",
                id_ejecucion:res.onts[d].id_ejecucion,    
                tipoCambio:res.onts[d].tipoCambio,
                 alias:res.onts[d].alias,
                frame:res.onts[d].frame,
                 slot:res.onts[d].slot,
                 puerto:res.onts[d].port,
                 uid:res.onts[d].uid,
                 tipo:res.onts[d].tipo,
                 desEstatus:"--",
                 fecha_ultima_caida: "---",
                 selected:false,
                 olts:res.onts[d].oltList
              }
              ELEMENT_DATA.push(dat);
            }
            this.dataUp.paginator = this.paginator;
            this.spinner.hide();
            console.log(ELEMENT_DATA)
          },
          err => console.error(err)
        )
      }

      actualizaOntbyOLt(idOlt:any,serie:any){
        this.service.actualizaOltByOnt(idOlt,serie).subscribe(
          res => {
              this._snackBar.open(res.sms, "cerrar", {
                duration: 4000
              });
             console.log(res.sms)
              setTimeout(() => {
                window.location.reload();
              }, 1400);
           
          },
          err => console.error(err)
        )
      }
      getOntsByolAll(){
        this.spinner.show();
        ELEMENT_DATA.length = ELEMENT_DATA.length - ELEMENT_DATA.length
        if(this.mostrar==null){
          this.mostrar='E';
         }

        this.lastUpTime=""
        this.upBytes=""
        this.timeOut=""
        this.upPackets=""
        this.downPackets=""
        this.dropUpPackets=""
        this.dropDownPackets=""
        this.cpu=""
        this.memoria=""
        this.profName=""
        this.downBytes=""
        this.fechaPoleo=""
        this.service.finOntsByIdAll(this.idOlt,this.mostrar).subscribe(
          res => {
        
            let dat;
            for(let d in res){
              dat={
                _id: res[d]._id,
                numero_serie:res[d].numero_serie,
                oid:res[d].oid,
                fecha_descubrimiento: res[d].fecha_modificacion,
                id_olt:res[d].id_olt,
                estatus: res[d].estatus==1? "UP":res[d].estatus==2?"DOWN":"DISCONNECT",
                id_ejecucion:res[d].id_ejecucion,    
                tipoCambio:"--",
                alias:res[d].alias,
                frame:res[d].frame,
                slot:res[d].slot,
                puerto:res[d].port,
                uid:res[d].uid,
                tipo:res[d].tipo,
                desEstatus:res[d].descripcionAlarma,
                fecha_ultima_caida: res[d].lastDownTime,
                selected:false,
                olts:[]
              }
              ELEMENT_DATA.push(dat);
            }
            this.dataUp.paginator = this.paginator;
            this.spinner.hide();
          },
          err => console.error(err)
        )
      }
  
      regresar(){
        localStorage.removeItem('idOlt');
       localStorage.removeItem('down');
        localStorage.removeItem('his');
       localStorage.removeItem('up');
       localStorage.removeItem('nombre');
       localStorage.removeItem('totalOnt');
       localStorage.setItem('muestraOnt','true');
       localStorage.setItem('muestraDetalle','false');
       localStorage.removeItem('busqueda');
       localStorage.removeItem('alias');
      localStorage.removeItem('ns');
     localStorage.removeItem('page');
     localStorage.removeItem('dataDetalle');"E"
     localStorage.removeItem("downDetalle");
     localStorage.removeItem("totalDetalle");
     localStorage.removeItem("cambiosDetalle");
       window.location.reload();
       
       
      }

      cambiaTabla(id:any){
        localStorage.setItem("cambiaTabla",id);
        if(this.busqueda!='1'){
          this.detallTabla(id);
      }
    }
    detallTabla(id:any){
      switch (id)
      {
         case 1:
        
          this.getOntsByolAll();
          this.titulo="TOTAL";
          this.icono="./assets/img/network.png";
          this.tabla=true;
          this.displayedColumns=['tipo','oid','frame','slot','puerto','uid','numeroSerie', 'alias', 'estatus','fecha','fechaUltimaCaida','desEstatus','acciones'];
          this.columnsToDisplayWithExpand= [...this.displayedColumns, 'expand'];
          this.tipoCambio=false; 
          this.asigna=true
      
             break;
    
          case 2:
           this.getOntsByOlts(this.idOlt,1,this.mostrar);
           this.titulo="ARRIBA";
           this.icono="./assets/img/up.png";
           this.tabla=true;
           this.displayedColumns=['tipo','oid','frame','slot','puerto','uid','numeroSerie', 'alias', 'estatus','fecha','fechaUltimaCaida','desEstatus','acciones'];
           this.columnsToDisplayWithExpand= [...this.displayedColumns, 'expand'];
           this.tipoCambio=false;
           this.asigna=true
         
             break;
          case 3:
            this.getOntsByOlts(this.idOlt,2,this.mostrar);
            this.titulo="ABAJO";
            this.icono="./assets/img/down.png";
            this.tabla=true;
            this.displayedColumns=['tipo','oid','frame','slot','puerto','uid','numeroSerie', 'alias',  'estatus','fecha', 'fechaUltimaCaida','desEstatus','acciones'];
            this.columnsToDisplayWithExpand= [...this.displayedColumns, 'expand'];
            this.tipoCambio=false;
            this.asigna=true
         
             break;          
          case 4:
            this.getCambios(this.idOlt,this.mostrar);
            this.icono="./assets/img/change.png";
            this.titulo="CAMBIOS";
            this.tabla=true;
            this.displayedColumns=['oid','numeroSerie','estatus','fecha','acciones',];
            this.columnsToDisplayWithExpand= [...this.displayedColumns];
            this.tipoCambio=true;
            this.asigna=false
       
              break;
      }
    }
    getData(oid:string,id_olt:number){
      this.spinner.show();
      this.service.getMetricas(oid,id_olt).subscribe(
        resp=>{

          this.lastUpTime=resp.lastUpTime
          this.upBytes=   resp.upBytes/1048576
          this.timeOut=resp.timeOut==-1?"Sin tiempo de vencimiento":resp.timeOut;
          this.upPackets=resp.upPackets/1048576
          this.downPackets=resp.downPackets/1048576
          this.dropUpPackets=resp.dropUpPackets/1048576
          this.dropDownPackets=resp.dropDownPackets/1048576
          this.cpu=resp.cpu
          this.memoria=resp.memoria
          this.profName=resp.profName
          this.downBytes=resp.downBytes/1048576
          this.fechaPoleo=resp.fechaPoleo

          this.spinner.hide();
        }
      )
    }

    cleanData(){
      this.lastUpTime=""
      this.upBytes=""
      this.timeOut=""
      this.upPackets=""
      this.downPackets=""
      this.dropUpPackets=""
      this.dropDownPackets=""
      this.cpu=""
      this.memoria=""
      this.profName=""
      this.downBytes=""
      this.fechaPoleo=""

    }
  }

  @Component({
    selector: 'detalleEjecucionMetrica',
    templateUrl: './detalleEjecucionMetrica.html',
    styleUrls: ['./detalleOnt.component.css']
  })
  
  export class detalleEjecucionMetricaDialog implements OnInit {
    archivo:any;
    public idOlt:any;
      constructor(
        public dialogRef:MatDialogRef<detalleEjecucionMetricaDialog>,
        private service: pointService,
        private spinner: NgxSpinnerService,
        private _snackbar: MatSnackBar
      ) {
     
        setInterval(() => this.getaArchivo(), 1000);
    
      }
      ngOnInit() {
        this.getaArchivo();
      
      }
     
    
      getaArchivo() {
        this.service.getArchivo(2).subscribe(
          res => {
            this.archivo=res;
          })
    
      }

      OnNoclick():void{
        this.dialogRef.close();
      }
    }