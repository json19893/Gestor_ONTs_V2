import { Component, Inject, OnInit, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { ThemePalette } from '@angular/material/core';
import { MatSidenav } from '@angular/material/sidenav';
import { Router } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

import { MatBottomSheet, MatBottomSheetRef } from '@angular/material/bottom-sheet';

import { MatDialog } from '@angular/material/dialog';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { pointService } from '../services/poinst.service';
import { getOnts } from '../model/getOnts'
import { getOlts } from '../model/getOlt'
import { MatTableDataSource } from '@angular/material/table';
import { Observable } from 'rxjs';

import { system } from '@amcharts/amcharts4/core';
import { Olts } from '../model/names.olts';
import { Onts } from '../model/alias.onts';
import { CookieService } from 'ngx-cookie-service';



interface olt {
  nombre: string;
  id_olt: number;
  ip: string
  totalOnts: number;
  tecnologia: string
  selected: boolean
}
export interface up {
  _id: string;
  numero_serie: string;
  oid: string;
  fecha_descubrimiento: string;
  id_olt: number;
  estatus: string;
  id_ejecucion: string;
  tipoCambio: string;
  alias: number;
  frame: number;
  slot: number;
  puerto: number;
  uid: string;
  tipo: string;
  desEstatus: string;
  selected: boolean;



}
const olts: olt[] = [];
const ELEMENT_DATA: up[] = []

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']

})

export class MenuComponent implements OnInit {
public ses:any;
  public mostrar: any;
  color: ThemePalette = 'warn';
  public checked: any;
  public usuario:any;
  public rol:any;
  acceso=false;
  disabled = true;
  panelJobs = false;
  @ViewChild('sidenav') sidenav: MatSidenav | undefined;
  isExpanded = false;
  showSubmenu: boolean = false;
  showSubmenu2: boolean = false;
  isShowing = false;

  public vips: any;
  clasificacion: string = '';

  constructor(
    private service: pointService,
    private router: Router,
    private spinner: NgxSpinnerService,
    private _bottomSheet: MatBottomSheet,
    public dialog: MatDialog,
  ) { }

  openBottomSheet(): void {
    this._bottomSheet.open(BottomSheetOverviewExampleSheet);
  }

  openDialog() {
    const dialogRef = this.dialog.open(DialogContentExampleDialog, {disableClose:true});

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }


  ngOnInit() {
    this.ses=localStorage.getItem('cod_sesion');
    //if(this.ses==1){
    if(1==1){
      this.router.navigate(['/home'])
      
    }else{
      this.router.navigate(['/login'])
    }
    this.mostrar = localStorage.getItem('mostrar'); // se creo una variable en localStorage, con Get es para mostrar 
    this.usuario = localStorage.getItem('usuario');
   
    if (this.mostrar == 'E') {
      this.checked = true;
      this.clasificacion = 'Emp';
    } else if (this.mostrar == 'T') {
      this.checked = false;
      this.clasificacion = '';
    } else if (this.mostrar == 'V') {
      this.checked = false;
      this.clasificacion = 'Emp';
    } else {
      this.checked = true;
      this.clasificacion = 'Emp';
    }
    this.usuario =localStorage.getItem('usuario');
    this.rol =localStorage.getItem('rol');
   switch ( this.rol) {
     case 'Administrador':
       this.acceso=true
       break;
       case 'Operador':
         this.acceso=true
         break;
         case 'Usuario':
           this.acceso=false
           break;
   }
  }
  mouseenter() {
    if (!this.isExpanded) {
      //this.isShowing = true;
    }
  }


  renderizarAsignacionOnts(){
    localStorage.setItem( "detalleClasificacion",'false');
    localStorage.setItem('muestraHome', 'false');
    localStorage.setItem('muestraDetalle', 'false');
    localStorage.setItem('detalleOnts', 'false');
    localStorage.setItem('detalleAsignarOnts', 'true');

    window.location.reload();
  }

  getmuestradetalle() {

    localStorage.setItem( "detalleClasificacion",'true'); 
    localStorage.setItem('muestraHome', 'false');
    localStorage.setItem('muestraDetalle', 'false');
    localStorage.setItem('detalleOnts', 'false');
    localStorage.setItem('detalleAsignarOnts', 'false');

    window.location.reload();

  }

  getmuestraOnts() {

    localStorage.setItem( "detalleClasificacion",'false'); 
    localStorage.setItem('muestraHome', 'false');
    localStorage.setItem('muestraDetalle', 'false');
    localStorage.setItem('detalleOnts', 'true');
    localStorage.setItem('detalleAsignarOnts', 'false');

    window.location.reload();

  }





  

  mouseleave() {
    if (!this.isExpanded) {
      this.isShowing = false;
    }
  }
  home() {
    localStorage.setItem( "detalleClasificacion","false"); 
    localStorage.removeItem('tipo');
    localStorage.setItem('detalleOnts', 'false');
    localStorage.setItem('muestraHome', 'false');
    localStorage.setItem('muestraDetalle', 'false');
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
    localStorage.removeItem('alias');
    localStorage.removeItem('ns');
    localStorage.removeItem('dataDetalle');
    localStorage.removeItem("upDetalle");
    localStorage.removeItem("downDetalle");
    localStorage.removeItem("totalDetalle");
    localStorage.removeItem("cambiosDetalle");
    localStorage.setItem('detalleAsignarOnts', 'false');
    
    window.location.reload();
  }

  salir() {

    this.service.logout(this.usuario).subscribe(
      res => {
        localStorage.removeItem("cod_sesion");
        localStorage.removeItem("usuario");
        localStorage.removeItem("nombreCompleto");
        localStorage.removeItem("rol");
        localStorage.removeItem('detalleOnts');
        window.location.reload();
      },
      err => console.error(err)
      
    );
   // 
  }
}



@Component({
  selector: 'bottom',
  templateUrl: 'bottom.html',
  styleUrls: ['./menu.component.css']

})
export class BottomSheetOverviewExampleSheet {
  constructor(private _bottomSheetRef: MatBottomSheetRef<BottomSheetOverviewExampleSheet>) { }

  openLink(event: MouseEvent): void {
    this._bottomSheetRef.dismiss();
    event.preventDefault();
  }
}

//pop2 imagen 2 

@Component({
  selector: 'pop',
  templateUrl: 'pop.html',
  styleUrls: ['./menu.component.css']

})
export class DialogContentExampleDialog implements OnInit {
  nombreOlts:String = "";
  optionsOlts: Olts[] = [];

  aliasOnts:String = "";
  optionsOnts: Onts[] = [];

  ipOlts:String = "";
  optionsIpOlts: Olts[] = [];

  serieOnts:String = "";
  optionsSerieOnts: Onts[] = [];

  hide: boolean = true;
  form1: FormGroup | any;
  form2: FormGroup | any;
  private _snackbar: any;
  public mostrar: any;
  public nombreRegion: any;
  public muestraHome: any;
  public getOntsData: getOnts | undefined;
  public getOltsData: getOlts | undefined;
  dataUp = new MatTableDataSource<up>(ELEMENT_DATA);
  public usuario:any;
  public rol:any;
  acceso=false;
  constructor(private fb: FormBuilder, private fb2: FormBuilder, private _snackbar2: MatSnackBar, private service: pointService, private spinner: NgxSpinnerService, private cookieService: CookieService
  ) {
    this.form1 = this.fb.group(
      {
        numeroSerie: [null],
        alias: [null]
      }
    )

    this.form2 = this.fb2.group(
      {
        ip: [null],
        nombre: [null]
      }
    )
  }

  ngOnInit() {
    this.mostrar = localStorage.getItem('mostrar');
    this.usuario =localStorage.getItem('usuario');
    this.rol =localStorage.getItem('rol');
 /*  switch ( this.rol) {
     case 'Administrador':
       this.acceso=true
       break;
       case 'Operador':
         this.acceso=true
         break;
         case 'Usuario':
           this.acceso=false
           break;
   }*/
  }

  openBottomSheet1() {

    if (this.form1.value.numeroSerie == '' && this.form1.value.alias == '' || this.form1.value.numeroSerie == null && this.form1.value.alias == null) {
      return this.error('Favor de ingresar por lo menos un valor');
    }
    this.spinner.show();

    var numeroSerie = this.form1.value.numeroSerie == 'null' ? null : this.form1.value.numeroSerie;
    var alias = this.form1.value.alias == 'null' ? null : this.form1.value.alias;
    localStorage.setItem('ns', numeroSerie);
    localStorage.setItem('alias', alias);

    this.mostrar = localStorage.getItem('mostrar');
    localStorage.removeItem('dataDetalle');
    localStorage.removeItem('dataRegion');
    localStorage.removeItem('page');
    localStorage.removeItem("upDetalle");
    localStorage.removeItem("downDetalle");
    localStorage.removeItem("totalDetalle");
    localStorage.removeItem("cambiosDetalle");
    localStorage.setItem( "detalleClasificacion","false"); 
        localStorage.setItem('muestraDetalle', 'false');
        

    if (this.mostrar == null) {
      this.mostrar = 'E';
    }

   /* this.getOntsData = new getOnts(numeroSerie, alias, this.mostrar);
    this.service.findOnt(this.getOntsData).subscribe(
      res => {
        if (res.success) {
          let dat;
          for (let d in res.listOnts) {
            dat = {
              _id: res.listOnts[d]._id,
              numero_serie: res.listOnts[d].numero_serie,
              oid: res.listOnts[d].oid,
              fecha_descubrimiento: res.listOnts[d].fecha_descubrimiento,
              id_olt: res.listOnts[d].id_olt,
              estatus: res.listOnts[d].estatus,
              id_ejecucion: res.listOnts[d].id_ejecucion,
              tipoCambio: "--",
              alias: res.listOnts[d].alias,
              frame: res.listOnts[d].frame,
              slot: res.listOnts[d].slot,
              puerto: res.listOnts[d].port,
              uid: res.listOnts[d].uid,
              tipo: res.listOnts[d].tipo,
              desEstatus: res.listOnts[d].descripcionAlarma,
              selected: res.listOnts[d].selected,

            }
            ELEMENT_DATA.push(dat);
            //localStorage.setItem("dataDetalle", JSON.stringify(ELEMENT_DATA));

            console.log("busqueda menu "+ ELEMENT_DATA);
            //this.cookieService.set('dataDetalle', JSON.stringify(ELEMENT_DATA));
          }
          localStorage.setItem("upDetalle", res.totales.arriba);
          localStorage.setItem("downDetalle", res.totales.abajo);
          localStorage.setItem("totalDetalle", res.totales.totalOlt);
          localStorage.setItem("cambiosDetalle", res.totales.cambios);
          localStorage.setItem('page', res.page);
          localStorage.setItem("nombre", res.nombre);
          localStorage.setItem('ip', res.ip);
          localStorage.setItem('muestraOnt', 'false');
          localStorage.setItem('muestraHome', 'false');
          localStorage.setItem('muestraDetalle', 'true');
          localStorage.setItem('busqueda', '1');
          localStorage.setItem( "detalleOnts","false");
        
        } else {
          this.spinner.hide()
          this.error('No se encontró información con ese criterio de búsqueda');
        }

      },
      err => console.error(err)
    )*/
    localStorage.setItem('muestraOnt', 'false');
    localStorage.setItem('muestraHome', 'false');
    localStorage.setItem('muestraDetalle', 'true');
    localStorage.setItem('busqueda', '1');
    localStorage.setItem( "detalleOnts","false");
    window.location.reload();


  }

  openBottomSheet2() {
    if (this.form2.value.ip == '' && this.form2.value.nombre == '' || this.form2.value.ip == null && this.form2.value.nombre == null) {

      return this.error('Favor de ingresar por lo menos un valor');
    }
    localStorage.removeItem('dataRegion');
    localStorage.removeItem('dataDetalle');
    localStorage.removeItem('idOlt');
    localStorage.removeItem('down');
    localStorage.removeItem('his');
    localStorage.removeItem('up');
    localStorage.removeItem('nombre');
    localStorage.removeItem('totalOnt');
    localStorage.removeItem('alias');
    localStorage.removeItem('ns');
    localStorage.setItem( "detalleClasificacion","false"); 
    localStorage.setItem( "detalleOnts","false");
    localStorage.setItem('muestraDetalle', 'false');
    this.spinner.show();
    var nombre = this.form2.value.nombre == 'null' ? null : this.form2.value.nombre;
    var ip = this.form2.value.ip == 'null' ? null : this.form2.value.ip;
    localStorage.setItem('busqueda', '1');
    localStorage.setItem('nom', nombre);
    localStorage.setItem('ip', ip);

    this.getOltsData = new getOlts(ip, nombre, this.mostrar);
    this.service.findOlt(this.getOltsData).subscribe(
      res => {
        if (res.success) {
          let dat;

          localStorage.setItem('totalHuaweiRe', res.data.totalHuawei);
          localStorage.setItem('totalZteRe', res.data.totalZte);
          localStorage.setItem('totalFhRe', res.data.totalFh);
          localStorage.setItem('totalArribaZteRe', res.data.totalArribaZte);
          localStorage.setItem('totalArribaHuaweiRe', res.data.totalArribaHuawei);
          localStorage.setItem('totalArribaFhRe', res.data.totalArribaFh);
          localStorage.setItem('totalAbajoHuaweiRe', res.data.totalAbajoHuawei);
          localStorage.setItem('totalAbajoZteRe', res.data.totalAbajoZte);
          localStorage.setItem('totalAbajoFhRe', res.data.totalAbajoFh);
          localStorage.setItem('totalHuaweiEmpRe', res.data.totalHuaweiEmp);
          localStorage.setItem('totalZteEmpRe', res.data.totalZteEmp);
          localStorage.setItem('totalFhEmpRe', res.data.totalFhEmp);
          localStorage.setItem('totalArribaZteEmpRe', res.data.totalArribaZteEmp);
          localStorage.setItem('totalArribaHuaweiEmpRe', res.data.totalArribaHuaweiEmp);
          localStorage.setItem('totalArribaFhEmpRe', res.data.totalArribaFhEmp);
          localStorage.setItem('totalAbajoHuaweiEmpRe', res.data.totalAbajoHuaweiEmp);
          localStorage.setItem('totalAbajoZteEmpRe', res.data.totalAbajoZteEmp);
          localStorage.setItem('totalAbajoFhEmpRe', res.data.totalAbajoFhEmp);
          localStorage.setItem('IdRegion', res.idRegion);
          const nombreRegion = "Region " + res.idRegion;
          localStorage.setItem('nombreRegion', nombreRegion);
          localStorage.removeItem('page');
          localStorage.setItem('page', res.page);
          if (this.mostrar == 'T') {
            for (let d in res.data.totalesRegion) {
              //if (res.data.totalesRegion[d].total_onts > 0) {
                dat = {
                  nombre: res.data.totalesRegion[d].nombre,
                  id_olt: res.data.totalesRegion[d].id_olt,
                  ip: res.data.totalesRegion[d].ip,
                  totalOnts: res.data.totalesRegion[d].total_onts,
                  tecnologia: res.data.totalesRegion[d].tecnologia,
                  selected: res.data.totalesRegion[d].selected
                }
                olts.push(dat);
                localStorage.setItem("dataRegion", JSON.stringify(olts));
              //}
            }
          } else {
            for (let d in res.data.totalesRegionEmp) {
              if (res.data.totalesRegionEmp[d].total_onts > 0) {
                dat = {
                  nombre: res.data.totalesRegionEmp[d].nombre,
                  id_olt: res.data.totalesRegionEmp[d].id_olt,
                  ip: res.data.totalesRegionEmp[d].ip,
                  totalOnts: res.data.totalesRegionEmp[d].total_onts,
                  tecnologia: res.data.totalesRegionEmp[d].tecnologia,
                  selected: res.data.totalesRegionEmp[d].selected
                }
                olts.push(dat);

                localStorage.setItem("dataRegion", JSON.stringify(olts));

              }
            }
          }
          localStorage.setItem('busqueda', '1');
          localStorage.setItem('nom', nombre);
          localStorage.setItem('ip', ip);
          this.muestraHome = 'true';
          localStorage.setItem('muestraHome', this.muestraHome);
          localStorage.setItem('muestraOnt', 'true');
          localStorage.setItem('muestraDetalle', 'false');
          window.location.reload();
        } else {
          this.spinner.hide()
          this.error('No se encontró información con ese criterio de búsqueda');
        }

      },
      err => console.error(err)

    )

  }
  error(error: any) {
    this._snackbar2.open(error, 'Cerrar', {
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    })
  }
  verifyWidth(){
    this.optionsOnts = [] ;
  }
  
  findNamesOlts() {
    this.nombreOlts = this.form2.value.nombre;
    if (this.nombreOlts.length>4) {
      this.spinner.show();
      this.service.getNameOlts(this.nombreOlts).subscribe(
        (resp) => {
        
          this.optionsOlts = resp;
          this.spinner.hide();
        }
      )
    }else{
      this.optionsOlts = [];
    }
  }

  findAliasOnts() {
    this.aliasOnts = this.form1.value.alias;
    if (this.aliasOnts.length > 6) {
      this.spinner.show();
      this.service.getAliasOnts(this.aliasOnts).subscribe(
        (resp) => {
       
          this.optionsOnts = resp;
          this.spinner.hide();
        }
      )
    }else{
      this.optionsOnts = [];  
    }
  }

  findIpOlts() {
    this.ipOlts = this.form2.value.ip;
    if (this.ipOlts.length>3) {
      this.spinner.show();
      this.service.getIpOlts(this.ipOlts).subscribe(
        (resp) => {
         
          this.optionsIpOlts = resp;
          this.spinner.hide();
        }
      )
    }else{
      this.optionsIpOlts = [];
    }
  }

  findSerieOnts() {
    this.serieOnts = this.form1.value.numeroSerie;
    if (this.serieOnts.length > 10) {
      this.spinner.show();
      this.service.getSerieOnts(this.serieOnts).subscribe(
        (resp) => {
          console.log(resp);
          this.optionsSerieOnts = resp;
          this.spinner.hide();
        }
      )
    }else{
      
      this.optionsSerieOnts = [];  
     
    }
  }

}




