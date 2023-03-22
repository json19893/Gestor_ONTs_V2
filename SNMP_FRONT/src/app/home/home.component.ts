import { DOCUMENT } from '@angular/common';
import { Component, ElementRef, Inject, Input, NgZone, OnInit, QueryList, ViewChild, ViewChildren,  } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import { MatIconRegistry } from "@angular/material/icon";
import { of } from 'rxjs';
import {pointService} from '../services/poinst.service';
import { FormControl, Validators } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import {getOlts}  from '../model/getOlt'
import { MatSort } from '@angular/material/sort';
import { MatSnackBar } from '@angular/material/snack-bar';

 //ELEMENT_DATA:olt[]= []
 const olts:olt[] = [];
@Component({
selector:'app-home',
templateUrl: './home.component.html',
styleUrls: ['./home.component.css']

})

export class HomeComponent implements OnInit {
  public dataRegion:any;
  public totalHuawei:any;
  public totalZte:any; 
  public totalFh:any; 
  public totalArribaZte:any;
  public totalArribaHuawei:any; 
  public totalArribaFh:any;
  public totalAbajoHuawei:any;
  public totalAbajoZte:any;
  public totalAbajoFh:any;
  public totalHuaweiEmp:any;
  public totalZteEmp:any;
  public totalFhEmp:any;
  public totalArribaZteEmp:any; 
  public totalArribaHuaweiEmp:any; 
  public totalArribaFhEmp:any; 
  public totalAbajoHuaweiEmp:any; 
  public totalAbajoZteEmp:any;
  public totalAbajoFhEmp:any; 
  public region: any;
  muestraOnt:any;
  muestraDetalle:any;
  public regiones:any;
  public oltRegion:any;
  panelOpenState = false;
  public nombreRegion:any;
  public mostrar: any;
  public busqueda:any;
  public getOltsData:getOlts | undefined;
  public nomR:any;
  public ipR:any;
  public page:any;
  seleIdRegion:any;
   totalHistorico:any;
   displayedColumns: string[] = ['olt', 'nombre', 'totalOnts','tecnologia','acciones'];
  selectFormRegion = new FormControl('', Validators.required);
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort: MatSort | undefined;
  //@ViewChild(MatPaginator) paginator: MatPaginator;
  dataUp = new MatTableDataSource<olt>(olts);
  width="78vw";
   // MatPaginator Inputs
   length:any;
   pageSize = 4;
   public div:any;
   
   // MatPaginator Output
   pageEvent: PageEvent | undefined;

    constructor(
      private router:Router,
      private spinner:NgxSpinnerService,
      private matIconRegistry: MatIconRegistry,
      @Inject(DOCUMENT) private document: any,
      private service:pointService,
      private _snackbar2: MatSnackBar
    ){
      this.matIconRegistry.addSvgIcon("ont", "./assets/img/device.SVG"); 
      this.region=localStorage.getItem('IdRegion');
      //setInterval(()=>this.getOltsByRegion(this.region,false),2000)
    }
        
    ngOnInit() {
      this.spinner.show();  
      this.busqueda=localStorage.getItem('busqueda');
      this.page=localStorage.getItem('page');
      
      if(this.page==undefined){
        this.page=0;
      }
      
      this.nomR= localStorage.getItem('nom');
      this.ipR= localStorage.getItem('ip');
      this.mostrar = localStorage.getItem('mostrar');
      this.muestraOnt=localStorage.getItem('muestraOnt');
      this.muestraDetalle= localStorage.getItem('muestraDetalle');
      this.region=localStorage.getItem('IdRegion');
      this.nombreRegion=localStorage.getItem('nombreRegion');
      this.dataRegion=JSON.parse(localStorage.getItem("dataRegion")!);
      
      if(this.mostrar==null){
        this.mostrar='E';
      }
      if(this.dataRegion==null){
        if (this.busqueda!='1'||this.busqueda==undefined){
          this.getOltsByRegion(this.region,this.mostrar);
        }else{      
          if(this.dataRegion==null){
            this.getOlt();
          }else{
            this.llenaTabla(this.dataRegion)
          }
        }
      }else{
        this.llenaTabla(this.dataRegion)
      }
      localStorage.setItem('muestraOnt','true');
      localStorage.setItem('muestraDetalle','false');
    }

    ngAfterViewInit() {
      //this.paginator.pageIndex = this.page;
      this.dataUp.paginator = this.paginator;
      this.dataUp.sort = this.sort!;
    }

    getOltsByRegion(idRegion:any,tipo:any){
      olts.length = olts.length - olts.length    
      this.service.getOltsByRegion(idRegion,tipo).subscribe(
        res => {
         this.totalHuawei=res.totalHuawei;
          this.totalZte=res.totalZte;
          this.totalFh=res.totalFh;
          this.totalArribaZte=res.totalArribaZte;
          this.totalArribaHuawei=res.totalArribaHuawei;
          this.totalArribaFh=res.totalArribaFh;
          this.totalAbajoHuawei=res.totalAbajoHuawei;
          this.totalAbajoZte=res.totalAbajoZte;
          this.totalAbajoFh=res.totalAbajoFh;
          this.totalHuaweiEmp=res.totalHuaweiEmp;
          this.totalZteEmp=res.totalZteEmp;
          this.totalFhEmp=res.totalFhEmp;
          this.totalArribaZteEmp=res.totalArribaZteEmp;
          this.totalArribaHuaweiEmp=res.totalArribaHuaweiEmp; 
          this.totalArribaFhEmp=res.totalArribaFhEmp;
          this.totalAbajoHuaweiEmp=res.totalAbajoHuaweiEmp;
          this.totalAbajoZteEmp=res.totalAbajoZteEmp;
          this.totalAbajoFhEmp=res.totalAbajoFhEmp; 
          let dat;
        
          localStorage.setItem('totalHuaweiRe',  res.totalHuawei);
          localStorage.setItem('totalZteRe',  res.totalZte);
          localStorage.setItem('totalFhRe',  res.totalFh);
          localStorage.setItem('totalArribaZteRe',  res.totalArribaZte);
          localStorage.setItem('totalArribaHuaweiRe',  res.totalArribaHuawei);
          localStorage.setItem('totalArribaFhRe',  res.totalArribaFh);
          localStorage.setItem('totalAbajoHuaweiRe',  res.totalAbajoHuawei);
          localStorage.setItem('totalAbajoZteRe',  res.totalAbajoZte);
          localStorage.setItem('totalAbajoFhRe',  res.totalAbajoFh);
          localStorage.setItem('totalHuaweiEmpRe',  res.totalHuaweiEmp);
          localStorage.setItem('totalZteEmpRe',  res.totalZteEmp);
          localStorage.setItem('totalFhEmpRe',  res.totalFhEmp);
          localStorage.setItem('totalArribaZteEmpRe',  res.totalArribaZteEmp);
          localStorage.setItem('totalArribaHuaweiEmpRe',  res.totalArribaHuaweiEmp); 
          localStorage.setItem('totalArribaFhEmpRe',  res.totalArribaFhEmp);
          localStorage.setItem('totalAbajoHuaweiEmpRe',  res.totalAbajoHuaweiEmp);
          localStorage.setItem('totalAbajoZteEmpRe',   res.totalAbajoZteEmp);
          localStorage.setItem('totalAbajoFhEmpRe',  res.totalAbajoFhEmp); 
          if( this.mostrar =='T'){
          for(let d in res.totalesRegion){
           if (res.totalesRegion[d].total_onts>0 || idRegion==11){
            dat={
              nombre: res.totalesRegion[d].nombre,
              id_olt:res.totalesRegion[d].id_olt ,
              ip:res.totalesRegion[d].ip,
              totalOnts:res.totalesRegion[d].total_onts,
              tecnologia:res.totalesRegion[d].tecnologia,
              selected:false
            }
            olts.push(dat);
            localStorage.setItem("dataRegion", JSON.stringify(olts));
          }
          }
        }          
        else if( this.mostrar =='V'){
        for(let d in res.totalesRegionVips){
          if (res.totalesRegionVips[d].total_onts>0 || idRegion==11){
            dat={
              nombre: res.totalesRegionVips[d].nombre,
              id_olt:res.totalesRegionVips[d].id_olt ,
              ip:res.totalesRegionVips[d].ip,
              totalOnts:res.totalesRegionVips[d].total_onts,
              tecnologia:res.totalesRegionVips[d].tecnologia,
              selected:false
            }
            olts.push(dat);
            localStorage.setItem("dataRegion", JSON.stringify(olts));
          }
          }
        }
        else if (this.mostrar == 'E' ) {
          for(let d in res.totalesRegionEmp){
            if (res.totalesRegionEmp[d].total_onts>0|| idRegion==11){
             dat={
               nombre: res.totalesRegionEmp[d].nombre,
               id_olt:res.totalesRegionEmp[d].id_olt ,
               ip:res.totalesRegionEmp[d].ip,
               totalOnts:res.totalesRegionEmp[d].total_onts,
               tecnologia:res.totalesRegionEmp[d].tecnologia,
               selected:false
             }
             olts.push(dat);
             localStorage.setItem("dataRegion", JSON.stringify(olts));

          }
           }
        }
       
       this.dataUp.paginator = this.paginator;
       this.spinner.hide();
    
        },
        err => console.error(err)
      
      )
    }

   
    llenaTabla(data:any){
          this.totalHuawei=localStorage.getItem('totalHuaweiRe');
          this.totalZte= localStorage.getItem('totalZteRe');
          this.totalFh=localStorage.getItem('totalFhRe');
          this.totalArribaZte=localStorage.getItem('totalArribaZteRe');
          this.totalArribaHuawei=localStorage.getItem('totalArribaHuaweiRe');
          this.totalArribaFh= localStorage.getItem('totalArribaFhRe');
          this.totalAbajoHuawei= localStorage.getItem('totalAbajoHuaweiRe');
          this.totalAbajoZte= localStorage.getItem('totalAbajoZteRe');
          this.totalAbajoFh=localStorage.getItem('totalAbajoFhRe');
          this.totalHuaweiEmp= localStorage.getItem('totalHuaweiEmpRe');
          this.totalZteEmp=localStorage.getItem('totalZteEmpRe');
          this.totalFhEmp=localStorage.getItem('totalFhEmpRe');
          this.totalArribaZteEmp=localStorage.getItem('totalArribaZteEmpRe');
          this.totalArribaHuaweiEmp=localStorage.getItem('totalArribaHuaweiEmpRe'); 
          this.totalArribaFhEmp= localStorage.getItem('totalArribaFhEmpRe');
          this.totalAbajoHuaweiEmp= localStorage.getItem('totalAbajoHuaweiEmpRe');
          this.totalAbajoZteEmp=localStorage.getItem('totalAbajoZteEmpRe');
          this.totalAbajoFhEmp=localStorage.getItem('totalAbajoFhEmpRe');
       
      let dat;    
      for(let d in data){
       
         dat={
           nombre: data[d].nombre,
           id_olt:data[d].id_olt ,
           ip:data[d].ip,
           totalOnts:data[d].totalOnts,
           tecnologia:data[d].tecnologia,
           selected:data[d].selected
         }
         olts.push(dat);
       
        

       
      
       this.spinner.hide();
       }
       this.dataUp.paginator = this.paginator;
    }
  getOlt(){
    this.getOltsData = new getOlts(this.ipR,this.nomR,this.mostrar);
    this.service.findOlt(this.getOltsData).subscribe(
      res => {
        if(res.success){
        let dat;
      
        localStorage.setItem('totalHuaweiRe',  res.data.totalHuawei);
        localStorage.setItem('totalZteRe',  res.data.totalZte);
        localStorage.setItem('totalFhRe',  res.data.totalFh);
        localStorage.setItem('totalArribaZteRe',  res.data.totalArribaZte);
        localStorage.setItem('totalArribaHuaweiRe',  res.data.totalArribaHuawei);
        localStorage.setItem('totalArribaFhRe',  res.data.totalArribaFh);
        localStorage.setItem('totalAbajoHuaweiRe',  res.data.totalAbajoHuawei);
        localStorage.setItem('totalAbajoZteRe',  res.data.totalAbajoZte);
        localStorage.setItem('totalAbajoFhRe',  res.data.totalAbajoFh);
        localStorage.setItem('totalHuaweiEmpRe',  res.data.totalHuaweiEmp);
        localStorage.setItem('totalZteEmpRe',  res.data.totalZteEmp);
        localStorage.setItem('totalFhEmpRe',  res.data.totalFhEmp);
        localStorage.setItem('totalArribaZteEmpRe',  res.data.totalArribaZteEmp);
        localStorage.setItem('totalArribaHuaweiEmpRe',  res.data.totalArribaHuaweiEmp); 
        localStorage.setItem('totalArribaFhEmpRe',  res.data.totalArribaFhEmp);
        localStorage.setItem('totalAbajoHuaweiEmpRe',  res.data.totalAbajoHuaweiEmp);
        localStorage.setItem('totalAbajoZteEmpRe',   res.data.totalAbajoZteEmp);
        localStorage.setItem('totalAbajoFhEmpRe',  res.data.totalAbajoFhEmp); 
        localStorage.setItem('IdRegion', res.idRegion);
        const nombreRegion="Region "+res.idRegion;
        localStorage.setItem('nombreRegion', nombreRegion);        
        localStorage.setItem('page', res.page);   
        if( this.mostrar =='T'){
        for(let d in res.data.totalesRegion){
         if (res.data.totalesRegion[d].total_onts>0 ){
          dat={
            nombre: res.data.totalesRegion[d].nombre,
            id_olt:res.data.totalesRegion[d].id_olt ,
            ip:res.data.totalesRegion[d].ip,
            totalOnts:res.data.totalesRegion[d].total_onts,
            tecnologia:res.data.totalesRegion[d].tecnologia,
            selected:res.data.totalesRegion[d].selected
          }
          olts.push(dat);
          localStorage.setItem("dataRegion", JSON.stringify(olts));
        }
        }
      }else{
        for(let d in res.data.totalesRegionEmp){
          if (res.data.totalesRegionEmp[d].total_onts>0){
           dat={
             nombre: res.data.totalesRegionEmp[d].nombre,
             id_olt:res.data.totalesRegionEmp[d].id_olt ,
             ip:res.data.totalesRegionEmp[d].ip,
             totalOnts:res.data.totalesRegionEmp[d].total_onts,
             tecnologia:res.data.totalesRegionEmp[d].tecnologia,
             selected:res.data.totalesRegionEmp[d].selected
           }
           olts.push(dat);
           localStorage.setItem("dataRegion", JSON.stringify(olts));
          
        }
         }
      }
 
      this.dataUp.paginator = this.paginator;
      this.spinner.hide();
      window.location.reload();
    }else{
      this.spinner.hide();
    }

      },
      err => console.error(err)
    
    )
  }

    applyFilter(event: Event) {
      const filterValue = (event.target as HTMLInputElement).value;
      this.dataUp.filter = filterValue.trim().toLowerCase();
  
      if (this.dataUp.paginator) {
        this.dataUp.paginator.firstPage();
      }
      this.dataUp.paginator = this.paginator;
    }
    verDetalle(idOlt:any,nombre:any,up:any,down:any,his:any,totalOnt:any,ip:any){
      if(totalOnt==0){
        this._snackbar2.open("No hay onts que mostrar", 'Cerrar', {
            horizontalPosition: 'center',
            verticalPosition: 'bottom'
        })      
      }else{
        localStorage.setItem('muestraOnt','false');
        localStorage.setItem('muestraDetalle','true');
        localStorage.setItem('idOlt',idOlt);
        //localStorage.setItem('down',down);
        //localStorage.setItem('totalOnt',totalOnt);
        //localStorage.setItem('up',up);
        localStorage.setItem('nombre',nombre);
        localStorage.setItem('ip',ip);
        localStorage.setItem('his',his);
    
        localStorage.removeItem('alias');
        localStorage.removeItem('ns');
        localStorage.removeItem('page');
        localStorage.removeItem('dataDetalle');
        localStorage.removeItem('busqueda');
        window.location.reload();
      }  
      
    }

    regionNombre(id:any){
      switch (id)
      {
         case 1:
          this.nombreRegion="Región 01"
             break;
    
          case 2:
            this.nombreRegion="Región 02"
             break;
          case 3:
            this.nombreRegion="Región 03"
             break;
          case 4:
            this.nombreRegion="Región 04"
              break;
          case 5:
            this.nombreRegion="Región 05"
              break;
          case 6:
            this.nombreRegion="Región 06"
              break;
          case 7:
            this.nombreRegion="Región 07"
              break;
          case 8:
            this.nombreRegion="Región 08"
              break;
          case 9:
            this.nombreRegion="Región 09"
              break;
          case 10:
            this.nombreRegion="Región 10"
              break;
              case 11:
            this.nombreRegion="Región 11"
              break;
      }
    }

    regresar(){
      localStorage.removeItem('tipo');
      localStorage.setItem('muestraHome','false');
      localStorage.removeItem('IdRegion');
      localStorage.removeItem('nombreRegion');
      localStorage.removeItem('dataRegion');
      localStorage.removeItem('totalHuaweiRe');
      localStorage.removeItem('totalZteRe');
      localStorage.removeItem('totalFhRe');
      localStorage.removeItem('totalArribaZteRe');
      localStorage.removeItem('totalArribaHuaweiRe');
      localStorage.removeItem('totalArribaFhRe');
      localStorage.removeItem('totalAbajoHuaweiRe');
      localStorage.removeItem('totalAbajoZteRe');
      localStorage.removeItem('totalAbajoFhRe');
      localStorage.removeItem('totalHuaweiEmpRe');
      localStorage.removeItem('totalZteEmpRe');
      localStorage.removeItem('totalFhEmpRe');
      localStorage.removeItem('totalArribaZteEmpRe');
      localStorage.removeItem('totalArribaHuaweiEmpRe'); 
      localStorage.removeItem('totalArribaFhEmpRe');
      localStorage.removeItem('totalAbajoHuaweiEmpRe');
      localStorage.removeItem('totalAbajoZteEmpRe');
      localStorage.removeItem('totalAbajoFhEmpRe');
      localStorage.removeItem('busqueda');
      localStorage.removeItem('nom');
      localStorage.removeItem('ip');
      localStorage.removeItem('page');
      window.location.reload();
     
     
    }

    }

    interface olt {
      nombre: string;
      id_olt: number;
      ip:string
      totalOnts:number;
      tecnologia:string
      selected:boolean
        }   


