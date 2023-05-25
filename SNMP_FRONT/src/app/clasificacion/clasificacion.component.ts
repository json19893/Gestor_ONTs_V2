import { DOCUMENT } from '@angular/common';
import { Component, ElementRef, Inject, NgZone, OnInit, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { MatIconRegistry } from "@angular/material/icon";
import { of } from 'rxjs';
import { pointService } from '../services/poinst.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import * as am4core from "@amcharts/amcharts4/core";
import * as am4charts from "@amcharts/amcharts4/charts";
import am4themes_animated from "@amcharts/amcharts4/themes/animated";
import { MatTableDataSource } from '@angular/material/table';


import { MatSnackBar } from '@angular/material/snack-bar';

import { CookieService } from 'ngx-cookie-service';
import { MatSort } from '@angular/material/sort';

export interface data {
  idRegion: number;
  region: string;
  totalOlt: number;
  totalOnt: number;
  arriba: number;
  abajo: number;
  cambios: number;
  estatus: number;
  totalRegion: number;
}
const ELEMENT_DATA: data[] = []

const ELEMENT_DATAE: data[] = []
const ELEMENT_DATAT: data[] = []
const ELEMENT_DATAV: data[] = []  //Se metio en un arreglo de TIPO data 
@Component({
  selector: 'app-clasificacion',
  templateUrl: './clasificacion.component.html',
  styleUrls: ['./clasificacion.component.css']

})

export class ClasificacionComponent implements OnInit {
  public muestraHome: any;
  public mostrar: any;
  public vips: any;
  public muestraHome2 = false;
  public tipo: any;
  public huawei: any;
  public zte: any;
  public div: any;
  public totalHuawei: any;
  public totalHuaweiEmp: any;
  public totalZteEmp: any;
  public totalFhEmp: any;
  public totalArribaZteEmp: any;
  public totalArribaHuaweiEmp: any
  public totalArribaFhEmp: any;
  public totalAbajoHuaweiEmp: any;
  public totalAbajoZteEmp: any;
  public totalAbajoFhEmp: any;
  public totalZte: any
  public totalArribaZte: any;
  public totalArribaHuawei: any;
  public totalAbajoHuawei: any;
  public totalAbajoZte: any;
  public totalOlts: any;
  public totales: any;
  public totalFb: any;
  public totalFbArriba: any;
  public totalFbAbajo: any;
  public fechaAct: any;
  public FechaDes: any;
  public conteoPdmOnts: any;
  public dataE: any;
  public dataT: any;
  public dataV: any;  //se creo una variable 
  public busqueda:any;
  public detalleClasificacion:any;
  public detalleOnts:any;
  muestraDetalle:any;
  inventarioNce:any;
  public ses:any;

  @ViewChild('tbl1', { static: false }) paginator!: MatPaginator;
  @ViewChild(MatSort) sort: MatSort | undefined;
  pageSize = 11;
  pageSizeOptions: number[] = [10, 50, 100];
  pageEvent: PageEvent | undefined;
  buttonDisablity = true;
  dataUp = new MatTableDataSource<data>(ELEMENT_DATA);
  width = "76vw";
  displayedColumns: string[] = ['region', 'totalOlt', 'totales', 'totalOnts', 'arriba', 'abajo', 'cambios', 'acciones'];
  public div2: any;
  
  constructor(private fb: FormBuilder, private _snackbar: MatSnackBar,
    private router: Router,
    private spinner: NgxSpinnerService,
    private service: pointService,
    private cookieService: CookieService) {
      this.ses=localStorage.getItem('cod_sesion');
      this.busqueda=localStorage.getItem('busqueda');
      this.muestraHome = localStorage.getItem('muestraHome');
      this.muestraDetalle= localStorage.getItem('muestraDetalle');
      this.detalleClasificacion= localStorage.getItem('detalleClasificacion');
      this.detalleOnts= localStorage.getItem('detalleOnts');
      if (this.ses==1){
      if(this.muestraHome=='false' && this.muestraDetalle=='false' && this.detalleClasificacion=='false'  && this.detalleOnts=='false'  || this.muestraHome==undefined && this.muestraDetalle==undefined && this.detalleClasificacion==undefined && this.detalleOnts==undefined){                setInterval(() => this.getTecnAsn('E'), 200000);
                setInterval(() => this.getTecnAsn('T'), 200000);
                setInterval(() => this.getTecnAsn('V'), 200000);
                setInterval(() => this.getTotales('T'), 200000);
                setInterval(() => this.getTotales('E'), 200000);
                setInterval(() => this.getTotales('V'), 200000);
  }
}
    //setInterval(()=>this.getTotales(),20000)
  }

  ngOnInit() {
    this.busqueda=localStorage.getItem('busqueda');
    this.muestraHome = localStorage.getItem('muestraHome');
    this.muestraDetalle= localStorage.getItem('muestraDetalle');
    this.detalleClasificacion= localStorage.getItem('detalleClasificacion');
    this.detalleOnts= localStorage.getItem('detalleOnts');
    this.inventarioNce=localStorage.getItem('inventarioNce')
    if ( this.muestraHome==null ||  this.muestraHome=='false'){
      if(this.detalleClasificacion=='false'||this.detalleClasificacion==undefined){
        if(this.detalleOnts=='false'||this.detalleOnts==undefined){
    this.mostrar = localStorage.getItem('mostrar');
    ELEMENT_DATAE.length = ELEMENT_DATAE.length - ELEMENT_DATAE.length
    ELEMENT_DATAT.length = ELEMENT_DATAT.length - ELEMENT_DATAT.length
    ELEMENT_DATAV.length = ELEMENT_DATAV.length - ELEMENT_DATAV.length //se vaceo el arreglo
    this.dataE = JSON.parse(localStorage.getItem("dataE")!);
    this.dataT = JSON.parse(localStorage.getItem("dataT")!);
    this.dataV = JSON.parse(localStorage.getItem("dataV")!);

    if (this.dataT == null) {
      this.getTecnAsn('T');
     this.getTotales('T')
    }if (this.dataV == null ) {
     this.getTecnAsn('V');
      this.getTotales('V')

    }
    if (this.mostrar == 'T') {
      this.llenaTabla(this.dataT, this.mostrar);
    } else if (this.mostrar == 'E') {
      this.llenaTabla(this.dataE, this.mostrar);
    }
    else if (this.mostrar == 'V') {
      this.llenaTabla(this.dataV, this.mostrar);
    }else {
     this.getTecnAsn('E');
      this.llenaTabla(this.dataE, this.mostrar);
   this.getTotales('E')
    }

    window.addEventListener("keydown", function (event) {
  
     /* if (event.shiftKey && event.key === 't'  || event.shiftKey && event.key === 'T') {
      
        localStorage.setItem('mostrar', 'T');
        window.location.reload();
      }*/
      if (event.shiftKey && event.key === 'e'  || event.shiftKey && event.key === 'E') {
        localStorage.setItem('mostrar', 'E');
        window.location.reload();
      }
      if (event.shiftKey && event.key === 'v'  || event.shiftKey && event.key === 'V') {
        localStorage.setItem('mostrar', 'V');
        window.location.reload();
      }
    }, false);
    this.spinner.show();
    this.tipo = localStorage.getItem('tipo');
  }
}
}
  }
  ngAfterViewInit() {
    this.div = document.getElementById("chartdiv");
    if (this.mostrar == null) {
      this.muestraTotales('E');
    }
    if(this.mostrar == 'T'){
      this.muestraTotales('T');
    }
    if(this.mostrar == 'V'){
      this.muestraTotales('V');
    }
    if(this.mostrar == 'E'){
      this.muestraTotales('E');
    }
  
    /*this.paginator.pageIndex = 0;
    this.dataUp.paginator = this.paginator;*/
    this.dataUp.sort = this.sort!;
  }


  getClasificacion(idRegion: any, region: any) {
    this.muestraHome = 'true';

    localStorage.setItem('muestraHome', this.muestraHome);
    localStorage.setItem('muestraOnt', 'true');
    localStorage.setItem('IdRegion', idRegion);
    localStorage.setItem('nombreRegion', region);
    localStorage.removeItem('busqueda');
      localStorage.removeItem('nom');
      localStorage.removeItem('ip');
      localStorage.removeItem('page');
      localStorage.removeItem("dataRegion");
    window.location.reload();
  }

  getTotalTecnologia(tipo: any) {
    this.spinner.show();
    this.service.getTotalesByTecnologia(tipo).subscribe(
      res => {
        this.llenaTabla(res, tipo);

      },
      err => console.error(err)
    );

  }
  getTecnAsn(tipo: any) {
    if (tipo == 'E') {
      ELEMENT_DATAE.length = ELEMENT_DATAE.length - ELEMENT_DATAE.length
    } else if (tipo == 'T') {
      ELEMENT_DATAT.length = ELEMENT_DATAT.length - ELEMENT_DATAT.length
    } else if (tipo == 'V' ) {
      ELEMENT_DATAV.length = ELEMENT_DATAV.length - ELEMENT_DATAV.length
    }
    this.service.getTotalesByTecnologia(tipo).subscribe(
      data => {
        let dat;
        for (let d in data) {
          dat = {
            idRegion: data[d].idRegion,
            region: data[d].region=='Región 11'?'Región Nueva':data[d].region,
            totalOlt: data[d].totalOlt,
            totalOnt: data[d].totalOnt,
            arriba: data[d].arriba,
            abajo: data[d].abajo,
            cambios: data[d].cambios,
            estatus: data[d].estatus,
            totalRegion: data[d].totalRegion
          }
          if (tipo == 'T') {
            ELEMENT_DATAT.push(dat);
            localStorage.setItem("dataT", JSON.stringify(ELEMENT_DATAT));
          } else if (tipo == 'V'){
            ELEMENT_DATAV.push(dat);
            localStorage.setItem("dataV", JSON.stringify(ELEMENT_DATAV));
          }else {
            ELEMENT_DATAE.push(dat);
            localStorage.setItem("dataE", JSON.stringify(ELEMENT_DATAE));
          }
          if (this.mostrar == 'T') {
            this.llenaTabla(ELEMENT_DATAT, '');
          } else if(this.mostrar== 'E') {
            this.llenaTabla(ELEMENT_DATAE, '');
          }else if(this.mostrar== 'V') {
            this.llenaTabla(ELEMENT_DATAV, '');
          }  else if (this.mostrar==null){
            this.llenaTabla(ELEMENT_DATAE, '');
          }
        }


      },
      err => console.error(err)
    );
  }

  getTotales(tipo: any) {
    this.service.getTotalesActivo(tipo).subscribe(
      res => {
        console.log(res);
        if (tipo=='E'){
        localStorage.setItem('totalHuaweiE',  res.totalHuawei);
        localStorage.setItem('totalZteE',  res.totalZte);
        localStorage.setItem('totalArribaZteE', res.totalArribaZte);
        localStorage.setItem('totalArribaHuaweiE',res.totalArribaHuawei);
        localStorage.setItem('totalAbajoHuaweiE', res.totalAbajoHuawei);
        localStorage.setItem('totalAbajoZteE', res.totalAbajoZte);
        localStorage.setItem('totalOltsE', res.totalOlts);
        localStorage.setItem('totalFbE',res.totalFh);
        localStorage.setItem('totalFbArribaE',res.totalArribaFh);
        localStorage.setItem('totalFbAbajoE', res.totalAbajoFh);
        localStorage.setItem('fechaActE', res.ultimaActualizacion);
        localStorage.setItem('FechaDesE', res.proximoDescubrimiento);
        localStorage.setItem('PdmOntsE', res.conteoPdmOnts);
        localStorage.setItem('totalHuaweiEmpE', res.totalHuaweiEmp);
        localStorage.setItem('totalZteEmpE', res.totalZteEmp);
        localStorage.setItem('totalFhEmpE', res.totalFhEmp);
        localStorage.setItem('totalArribaZteEmpE',res.totalArribaZteEmp);
        localStorage.setItem('totalArribaHuaweiEmpE', res.totalArribaHuaweiEmp);
        localStorage.setItem('totalArribaFhEmpE', res.totalArribaFhEmp);
        localStorage.setItem('totalAbajoHuaweiEmpE', res.totalAbajoHuaweiEmp);
        localStorage.setItem('totalAbajoZteEmpE', res.totalAbajoZteEmp);
        localStorage.setItem('totalAbajoFhEmpE', res.totalAbajoFhEmp);
        localStorage.setItem('graficaE', JSON.stringify(res.grafica));
        }
      if (tipo=='T'){
        localStorage.setItem('totalHuaweiT',  res.totalHuawei);
        localStorage.setItem('totalZteT',  res.totalZte);
        localStorage.setItem('totalArribaZteT', res.totalArribaZte);
        localStorage.setItem('totalArribaHuaweiT',res.totalArribaHuawei);
        localStorage.setItem('totalAbajoHuaweiT', res.totalAbajoHuawei);
        localStorage.setItem('totalAbajoZteT', res.totalAbajoZte);
        localStorage.setItem('totalOltsT', res.totalOlts);
        localStorage.setItem('totalFbT',res.totalFh);
        localStorage.setItem('totalFbArribaT',res.totalArribaFh);
        localStorage.setItem('totalFbAbajoT', res.totalAbajoFh);
        localStorage.setItem('fechaActT', res.ultimaActualizacion);
        localStorage.setItem('FechaDesT', res.proximoDescubrimiento);
        localStorage.setItem('PdmOntsT', res.conteoPdmOnts);
        localStorage.setItem('totalHuaweiEmpT', res.totalHuaweiEmp);
        localStorage.setItem('totalZteEmpT', res.totalZteEmp);
        localStorage.setItem('totalFhEmpT', res.totalFhEmp);
        localStorage.setItem('totalArribaZteEmpT',res.totalArribaZteEmp);
        localStorage.setItem('totalArribaHuaweiEmpT', res.totalArribaHuaweiEmp);
        localStorage.setItem('totalArribaFhEmpT', res.totalArribaFhEmp);
        localStorage.setItem('totalAbajoHuaweiEmpT', res.totalAbajoHuaweiEmp);
        localStorage.setItem('totalAbajoZteEmpT', res.totalAbajoZteEmp);
        localStorage.setItem('totalAbajoFhEmpT', res.totalAbajoFhEmp);
        localStorage.setItem('graficaT', JSON.stringify(res.grafica));
        }

        if (tipo=='V'){
          localStorage.setItem('totalHuaweiV',  res.totalHuawei);
          localStorage.setItem('totalZteV',  res.totalZte);
          localStorage.setItem('totalArribaZteV', res.totalArribaZte);
          localStorage.setItem('totalArribaHuaweiV',res.totalArribaHuawei);
          localStorage.setItem('totalAbajoHuaweiV', res.totalAbajoHuawei);
          localStorage.setItem('totalAbajoZteV', res.totalAbajoZte);
          localStorage.setItem('totalOltsV', res.totalOlts);
          localStorage.setItem('totalFbV',res.totalFh);
          localStorage.setItem('totalFbArribaV',res.totalArribaFh);
          localStorage.setItem('totalFbAbajoV', res.totalAbajoFh);
          localStorage.setItem('fechaActV', res.ultimaActualizacion);
          localStorage.setItem('FechaDesV', res.proximoDescubrimiento);
          localStorage.setItem('PdmOntsV', res.conteoPdmOnts);
          localStorage.setItem('totalHuaweiEmpV', res.totalHuaweiEmp);
          localStorage.setItem('totalZteEmpV', res.totalZteEmp);
          localStorage.setItem('totalFhEmpV', res.totalFhEmp);
          localStorage.setItem('totalArribaZteEmpV',res.totalArribaZteEmp);
          localStorage.setItem('totalArribaHuaweiEmpV', res.totalArribaHuaweiEmp);
          localStorage.setItem('totalArribaFhEmpV', res.totalArribaFhEmp);
          localStorage.setItem('totalAbajoHuaweiEmpV', res.totalAbajoHuaweiEmp);
          localStorage.setItem('totalAbajoZteEmpV', res.totalAbajoZteEmp);
          localStorage.setItem('totalAbajoFhEmpV', res.totalAbajoFhEmp);
          localStorage.setItem('graficaV', JSON.stringify(res.grafica));
          }
        if (this.mostrar == 'T') {
         this.muestraTotales('T')
        } else if(this.mostrar== 'E') {
          this.muestraTotales('E')
        }
        else if(this.mostrar== 'V') {
          this.muestraTotales('V')
        } else if (this.mostrar==null){
          this.muestraTotales('E')
          if(this.dataE == null){
          setTimeout(() => {
            this.spinner.hide();
          }, 35000);
        }else{
          setTimeout(() => {
            this.spinner.hide();
          }, 1000);
        }
        }
      },
      err => console.error(err)
    );
  }
  muestraTotales(tipo:any){
 if (tipo=='T'){
    this.totalHuawei = localStorage.getItem('totalHuaweiT');
    this.totalZte =localStorage.getItem('totalZteT');
    this.totalArribaZte = localStorage.getItem('totalArribaZteT');
    this.totalArribaHuawei =  localStorage.getItem('totalArribaHuaweiT');
    this.totalAbajoHuawei =localStorage.getItem('totalAbajoHuaweiT');
    this.totalAbajoZte =localStorage.getItem('totalAbajoZteT');
    this.totalOlts =localStorage.getItem('totalOltsT');
    this.totalFb = localStorage.getItem('totalFbT');
    this.totalFbArriba=localStorage.getItem('totalFbArribaT');
    this.totalFbAbajo =localStorage.getItem('totalFbAbajoT');
    this.conteoPdmOnts = localStorage.getItem('PdmOntsT');
    this.fechaAct = localStorage.getItem('fechaActE');
    this.FechaDes = localStorage.getItem('FechaDesE');

    this.totalHuaweiEmp = localStorage.getItem('totalHuaweiEmpT');
    this.totalZteEmp = localStorage.getItem('totalZteEmpT');
    this.totalFhEmp =localStorage.getItem('totalFhEmpT');
    this.totalArribaZteEmp = localStorage.getItem('totalArribaZteEmpT');
    this.totalArribaHuaweiEmp = localStorage.getItem('totalArribaHuaweiEmpT');
    this.totalArribaFhEmp =localStorage.getItem('totalArribaFhEmpT');
    this.totalAbajoHuaweiEmp = localStorage.getItem('totalAbajoHuaweiEmpT');
    this.totalAbajoZteEmp = localStorage.getItem('totalAbajoZteEmpT');
    this.totalAbajoFhEmp =  localStorage.getItem('totalAbajoFhEmpT');
    this.getGrafica(JSON.parse(localStorage.getItem('graficaT')!));
    if(this.dataT == null){
    setTimeout(() => {
      this.spinner.hide();
    }, 45000);
  }else{
    setTimeout(() => {
      this.spinner.hide();
    }, 1000);
  }
 }

 if (tipo=='V'){
  this.totalHuawei = localStorage.getItem('totalHuaweiV');
  this.totalZte =localStorage.getItem('totalZteV');
  this.totalArribaZte = localStorage.getItem('totalArribaZteV');
  this.totalArribaHuawei =  localStorage.getItem('totalArribaHuaweiV');
  this.totalAbajoHuawei =localStorage.getItem('totalAbajoHuaweiV');
  this.totalAbajoZte =localStorage.getItem('totalAbajoZteV');
  this.totalOlts =localStorage.getItem('totalOltsV');
  this.totalFb = localStorage.getItem('totalFbV');
  this.totalFbArriba=localStorage.getItem('totalFbArribaV');
  this.totalFbAbajo =localStorage.getItem('totalFbAbajoV');
  this.conteoPdmOnts = localStorage.getItem('PdmOntsV');
  this.fechaAct = localStorage.getItem('fechaActV');
  this.FechaDes = localStorage.getItem('FechaDesV');

  this.totalHuaweiEmp = localStorage.getItem('totalHuaweiEmpV');
  this.totalZteEmp = localStorage.getItem('totalZteEmpV');
  this.totalFhEmp =localStorage.getItem('totalFhEmpV');
  this.totalArribaZteEmp = localStorage.getItem('totalArribaZteEmpV');
  this.totalArribaHuaweiEmp = localStorage.getItem('totalArribaHuaweiEmpV');
  this.totalArribaFhEmp =localStorage.getItem('totalArribaFhEmpV');
  this.totalAbajoHuaweiEmp = localStorage.getItem('totalAbajoHuaweiEmpV');
  this.totalAbajoZteEmp = localStorage.getItem('totalAbajoZteEmpV');
  this.totalAbajoFhEmp =  localStorage.getItem('totalAbajoFhEmpV');
  this.getGrafica(JSON.parse(localStorage.getItem('graficaV')!));
 
  setTimeout(() => {
    this.spinner.hide();
  }, 1000);
}
 if (tipo=='E'){
  this.totalHuawei = localStorage.getItem('totalHuaweiE');
  this.totalZte =localStorage.getItem('totalZteE');
  this.totalArribaZte = localStorage.getItem('totalArribaZteE');
  this.totalArribaHuawei =  localStorage.getItem('totalArribaHuaweiE');
  this.totalAbajoHuawei =localStorage.getItem('totalAbajoHuaweiE');
  this.totalAbajoZte =localStorage.getItem('totalAbajoZteE');
  this.totalOlts =localStorage.getItem('totalOltsE');
  this.totalFb = localStorage.getItem('totalFbE');
  this.totalFbArriba=localStorage.getItem('totalFbArribaE');
  this.totalFbAbajo =localStorage.getItem('totalFbAbajoE');
  this.fechaAct = localStorage.getItem('fechaActE');
  this.FechaDes = localStorage.getItem('FechaDesE');
  this.conteoPdmOnts = localStorage.getItem('PdmOntsE');
  this.totalHuaweiEmp = localStorage.getItem('totalHuaweiEmpE');
  this.totalZteEmp = localStorage.getItem('totalZteEmpE');
  this.totalFhEmp =localStorage.getItem('totalFhEmpE');
  this.totalArribaZteEmp = localStorage.getItem('totalArribaZteEmpE');
  this.totalArribaHuaweiEmp = localStorage.getItem('totalArribaHuaweiEmpE');
  this.totalArribaFhEmp =localStorage.getItem('totalArribaFhEmpE');
  this.totalAbajoHuaweiEmp = localStorage.getItem('totalAbajoHuaweiEmpE');
  this.totalAbajoZteEmp = localStorage.getItem('totalAbajoZteEmpE');
  this.totalAbajoFhEmp =  localStorage.getItem('totalAbajoFhEmpE');
  this.getGrafica(JSON.parse(localStorage.getItem('graficaE')!));
  if(this.dataE == null){
    setTimeout(() => {
      this.spinner.hide();
    }, 35000);
  }else{
    setTimeout(() => {
      this.spinner.hide();
    }, 1000);
  }
}

  }
  llenaTabla(data: any, tipo: any) {
    ELEMENT_DATA.length = ELEMENT_DATA.length - ELEMENT_DATA.length
    let dat;
    for (let d in data) {
      dat = {
        idRegion: data[d].idRegion,
        region: data[d].region,
        totalOlt: data[d].totalOlt,
        totalOnt: data[d].totalOnt,
        arriba: data[d].arriba,
        abajo: data[d].abajo,
        cambios: data[d].cambios,
        estatus: data[d].estatus,
        totalRegion: data[d].totalRegion
      }

      ELEMENT_DATA.push(dat);
      this.dataUp.paginator = this.paginator;


    }
  }
  getGrafica(data: any) {

    /* Chart code */
    // Themes begin
    am4core.useTheme(am4themes_animated);
    // Themes end



    // Create chart instance
    let chart = am4core.create(this.div, am4charts.RadarChart);
    // Add data
    chart.data = data;

    if (chart.logo) {
      chart.logo.disabled = true;
    }

    // Make chart not full circle
    chart.startAngle = -90;
    chart.endAngle = 180;
    chart.innerRadius = am4core.percent(15);
    chart.arrangeTooltips = true;

    // Set number format
    chart.numberFormatter.numberFormat = "#.#";

    // Create axes
    let categoryAxis = chart.yAxes.push(new am4charts.CategoryAxis<am4charts.AxisRendererRadial>());
    categoryAxis.dataFields.category = "category";
    categoryAxis.renderer.grid.template.location = 100;
    categoryAxis.renderer.grid.template.strokeOpacity = 100;
    categoryAxis.renderer.labels.template.horizontalCenter = "right";
    categoryAxis.renderer.labels.template.fontSize = 14;

    categoryAxis.disabled = false;
  
    categoryAxis.renderer.minGridDistance = 5;

    let valueAxis = chart.xAxes.push(new am4charts.ValueAxis<am4charts.AxisRendererCircular>());
    valueAxis.renderer.grid.template.strokeOpacity = 0;
    valueAxis.min = 0;
    valueAxis.max = 100;
    valueAxis.strictMinMax = true;
    valueAxis.disabled = true
    // Create series
    let series1 = chart.series.push(new am4charts.RadarColumnSeries());
    series1.dataFields.valueX = "full";
    series1.dataFields.categoryY = "category";
    series1.clustered = false;
    series1.columns.template.fill = new am4core.InterfaceColorSet().getFor("alternativeBackground");
    series1.columns.template.fillOpacity = 0.08;
    series1.columns.template.strokeWidth = 0;
    series1.columns.template.radarColumn.cornerRadius = 40;


    let series2 = chart.series.push(new am4charts.RadarColumnSeries());
    series2.dataFields.valueX = "value";
    series2.dataFields.categoryY = "category";
    series2.clustered = false;
    series2.columns.template.strokeWidth = 0;
    series2.columns.template.tooltipText = "{category}: [bold]{value}[/]";
    series2.columns.template.radarColumn.cornerRadius = 40;

    series2.columns.template.adapter.add("fill", function (fill, target) {
      chart.colors.list = [
        am4core.color("#7D54AA"),
        am4core.color("#4FABD6"),
        am4core.color("#20AE0F"),

      ];
      return chart.colors.list[target.dataItem!.index];
    });

    // Add cursor
    chart.cursor = new am4charts.RadarCursor();
  }

  
}