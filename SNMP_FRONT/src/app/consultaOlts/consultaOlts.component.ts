import { Component, ElementRef, Inject, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA, MatDialogConfig } from "@angular/material/dialog";
import { MatPaginator } from "@angular/material/paginator";
import { MatTable, MatTableDataSource } from "@angular/material/table";
import { NgxSpinnerService } from "ngx-spinner";
import * as XLSX from 'xlsx';
import { pointService } from "../services/poinst.service";
import { FileSaverService } from 'ngx-filesaver';
import { MatSnackBar } from "@angular/material/snack-bar";
import { SelectionModel } from "@angular/cdk/collections";
import { ThemePalette } from "@angular/material/core";


import { descubrimientoManual } from '../model/descubrimientoManual';
import { poleoManual } from '../model/poleoManual';
import * as FileSaver from "file-saver";
import { OntComponentDialog } from "./components/ont/ont.component";
import { DialogInventarioComponent, GenericResponse } from "./components/dialog-inventario/dialog-inventario.component";
import { Observable, Subject, Subscription } from "rxjs";
const EXCEL_TYPE = 'application/vnd.openxmlformats- officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';
interface Olts {
  id_olt: number;
  nombre: string;
  ip: string;
  descricion: string;
  tecnologia: string;
  id_region: number;
  id_configuracion: number;
  estatus: number;
  total_onts: number;
  id: string;
  descubrio: boolean;
  nce: boolean;
  descubrimiento: boolean;
}
export interface Bloques {
  bloque: number;
  nombre: string;
}

@Component({
  selector: 'app-consultaOlts',
  templateUrl: './consultaOlts.component.html',
  styleUrls: ['./consultaOlts.component.css']

})

export class ConsultaOltsComponent implements OnInit {
  public listAceptadas!: [];
  public usuario: any;
  blo: Bloques[] = [{ bloque: 1, nombre: "Bloque 1" }, { bloque: 2, nombre: "Bloque 2" }, { bloque: 3, nombre: "Bloque 3" }, { bloque: 4, nombre: "Bloque 4" }]
  public dat: any;
  @ViewChild('table') table: ElementRef | undefined;
  public rol: any;
  public manualDto: descubrimientoManual | undefined;
  public poleoManualDto: poleoManual | undefined;
  acceso = false;
  color: ThemePalette = 'primary';
  public intentos: any;
  checked = false;
  disabled = false;
  @ViewChild('paginator') paginator: MatPaginator | undefined;
  ELEMENT_DATA: Olts[] = [];
  descubrimiento: any = []
  dataSource = new MatTableDataSource<Olts>;
  columnsToDisplay = ['select', 'ip', 'nombre', 'tecnologia', 'id_region', 'totalOnts',
    'descripcion', 'slide', 'opciones', 'eventos'];
  headers = ['Ip OLT', 'Nombre', 'Tecnología', 'Región', 'eventos'];

  private currentProcess$!: Observable<GenericResponse>;

  selection = new SelectionModel<Olts>(true, []);
  constructor(public dialog: MatDialog,
    private spinner: NgxSpinnerService,
    private service: pointService, private _snackBar: MatSnackBar) {
    setInterval(() => this.valmaximo(), 100000);
    this.usuario = localStorage.getItem('usuario');

  }
  /** Whether the number of selected elements matches the total number of rows. */
  ngOnInit() {

    this.spinner.show();
    this.getDataTable();
    this.valmaximo();
    //this.usuario = localStorage.getItem('usuario');
    console.log(this.usuario);
    this.rol = localStorage.getItem('rol');
    this.intentos = 0;
    switch (this.rol) {
      case 'Administrador':
        this.acceso = true
        break;
      case 'Operador':
        this.acceso = true
        break;
      case 'Usuario':
        this.acceso = false
        break;
    }
    // this.poleoOlt({
    //   id_olt: 1,
    //   nombre: "ARROYO_VERDE_GT",
    //   ip: "10.71.6.147 ",
    //   descricion: "",
    //   tecnologia: "",
    //   id_region: 0,
    //   id_configuracion: 0,
    //   estatus: 0,
    //   total_onts: 0,
    //   id: "",
    //   descubrio: false,
    //   nce: false
    // });
  }

  ExportTOExcel() {
    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.ELEMENT_DATA);
    const workbook: XLSX.WorkBook = {
      Sheets: { 'homologacion': worksheet },
      SheetNames: ['homologacion']
    };
    const excelBuffer: any = XLSX.write(workbook, {
      bookType: 'xlsx', type: 'array'
    });
    this.saveAsExcelFile(excelBuffer, "listado_OLTs");

  }
  valmaximo() {
    this.service.validaMaximo().subscribe(
      res => {

        this.intentos = res;

      })
  }

  saveAsExcelFile(buffer: any, fileName: string) {
    const data: Blob = new Blob([buffer], { type: EXCEL_TYPE });
    FileSaver.saveAs(data, fileName + '_export_' + new Date().getTime() +
      EXCEL_EXTENSION);
  }

  selec(evento: any, ip: any) {
    if (!evento) {
      this.descubrimiento.push(ip);
    } else {
      this.descubrimiento = this.descubrimiento.filter((item: string) => item !== ip)
    }
    /*this.service.validaMaximo().subscribe(
      res => {

        this.intentos = res;  
        
        if (this.intentos > 0) {
          if (!evento) {
            if (this.intentos > this.descubrimiento.length) {
              this.descubrimiento.push(idOlt);
            } else {
              this._snackBar.open("Solo puedes puedes realizar " + 5 + " descubrimientos por hoy", "Cerrar", {
                duration: 4000
              });
            }
          } else {
            this.descubrimiento = this.descubrimiento.filter((item: string) => item !== idOlt)
          }
        } else {
          this._snackBar.open("Ya no puedes realizar mas descubrimientos  por hoy ", "Cerrar", {
            duration: 4000
          });
          setTimeout(() => {
            //window.location.reload();
          },
            600);

        }
      })*/


  }
  activar(idOlt: any, estatus: any, tecnologia: any) {
    // if (tecnologia != "FIBER HOME") {
    let esta = estatus == 1 ? 0 : 1;
    this.service.updateEstatus(idOlt, esta, this.usuario).subscribe(
      res => {
        this.getDataTable()
      })
    /*  } else {
        this._snackBar.open("No se puede activar la olt porque aun no esta configurada para descubrimiento", "Cerrar", {
          duration: 4000
        });
      }*/

  }
  getDataTable() {
    this.service.getOlts(this.usuario).subscribe(
      res => {
        this.ELEMENT_DATA = res;
        this.dataSource = new MatTableDataSource<Olts>(this.ELEMENT_DATA);
        this.dataSource!.paginator = this.paginator!;
        this.spinner.hide();
      })
  }


  actaulizaOlts() {
    const dialogRef = this.dialog.open(ActualizaOntsDialog, { disableClose: true });

    dialogRef.afterClosed().subscribe(result => {

    });
  }
  descubrimientoManual() {
    if (this.descubrimiento.length > 0) {
      /* if (this.descubrimiento.length > 5) {
         this._snackBar.open("Solo puedes realizar 5 descubrimientos  por día", "Cerrar", {
           duration: 4000
         });
       } else {*/
      this.openDetalle()
      this.manualDto = new descubrimientoManual(this.descubrimiento, this.usuario);
      this.service.descubrimiento(this.manualDto).subscribe(
        res => {
          this._snackBar.open(res.sms, "Cerrar", {
            duration: 4000
          });

        })
      /*}*/
    } else {
      this._snackBar.open("Debes seleccionar al menos una olt", "Cerrar", {
        duration: 4000
      });
    }
  }

  poleoMetrica(idBloque: any) {
    if (this.descubrimiento.length > 0) {
      this.poleoManualDto = new poleoManual(this.descubrimiento, this.usuario, idBloque);
      //this.openDetalle()
      this.service.poleoMetrica(this.poleoManualDto).subscribe(
        res => {
          this._snackBar.open(res.sms, "Cerrar", {
            duration: 4000
          });
        })

    } else {
      this._snackBar.open("Debes seleccionar al menos una olt", "Cerrar", {
        duration: 4000
      });
    }
  }

  //Aqui mete la funcion
  //Redireccion al detalle olt
  openDetalleHomologacion(olt: Olts) {
    const { id_olt } = olt;
    const dialogConfig = new MatDialogConfig<Olts>();
    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = true;
    dialogConfig.height = '65vh';
    dialogConfig.width = '130vw';
    dialogConfig.data = olt;

    this.dialog.open(OntComponentDialog, dialogConfig);
  }

  openDetalle() {
    this.dialog.open(detalleEjecucionDialog, {
      height: '80vh',
      width: '130vw',
    });
  }

  openDialog() {
    this.dialog.open(DialogElementsExampleDialog, {
      height: '80vh',
      width: '130vw',
    });

  }

  //Abre la diaogo
  openDetalleAceptadas(olt: Olts) {

    let state: { sync: boolean, olt: Olts } = {
      sync: false,
      olt: olt
    };

    const dialogConfig = new MatDialogConfig<any>();
    dialogConfig.autoFocus = true;
    dialogConfig.height = '80vh';
    dialogConfig.width = '50vw';
    dialogConfig.data = state;

    this.dialog.open(DialogInventarioComponent, dialogConfig);

    const dialogRef = this.dialog.open(DialogInventarioComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      () => this.getDataTable()
    )
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
    this.dataSource!.paginator = this.paginator!;
  }

  poleoOlt(idOlt: Olts) {
    this.openDetalleAceptadas(idOlt);
    console.log('servicio 1');
    this.spinner.show();
    this.service.poleoOlt(idOlt.id_olt, this.usuario).subscribe((data) => {
      console.log(data);

      if (data.cod == 0) {
        this.service.getAceptadosInventario(idOlt.id_olt, idOlt.ip, new Date().toISOString(), new Date().toISOString(), this.usuario).subscribe((resp) => {
          console.log('servicio 2');
          this.spinner.hide();
          this.listAceptadas = resp;
          this.openDetalleAceptadas(idOlt);
        });
      }
    });
  }

  detalleSin(idOlt: Olts) {
    this.service.getAceptadosInventario(idOlt.id_olt, idOlt.ip, new Date().toISOString(), new Date().toISOString(), this.usuario).subscribe((resp) => {
      console.log('servicio 2');
      this.spinner.hide();
      this.listAceptadas = resp;
      this.openDetalleAceptadas(idOlt);
    });
  }

}

@Component({
  selector: 'actualiza-onts',
  templateUrl: './actualiza-onts.html',
  styleUrls: ['./actualiza-onts.css']
})
export class ActualizaOntsDialog {
  public usuario: any;
  public rol: any;
  acceso = false;
  @ViewChild('fileInput')
  fileInput!: ElementRef;
  fileAttr = 'No se ha elegido archivo (.xlsx)';
  dataString = "";
  traza = "";
  solicitud: any;
  loading = false;
  loadingExcel = true;
  response: boolean = false;

  actualizados: number | undefined;
  noActualizados: number | undefined;
  recibidos: number | undefined;
  dataNoActualizados: any[] = [];




  excelHeaders1: string[] = [
    'serie',
    'ip',
    'frame',
    'slot',
    'port',
    'estatus',
    'tipo',
    'oid',
    'uid'
  ];
  templateToExcel1: string[][] = [this.excelHeaders1, []];
  //usuario!: UsuarioModel | null;
  nombre!: string;
  modulo = 'Alta masiva de Onts Excel - Carga de Excel';



  constructor(
    private _dialogRef: MatDialogRef<ActualizaOntsDialog>,
    //@Inject(MAT_DIALOG_DATA) public data: DialogData,
    private _formBuilder: FormBuilder,
    private _dialog: MatDialog,
    private service: pointService,
    private spinner: NgxSpinnerService,
    private fileSaver: FileSaverService,
    private fb: FormBuilder,
    private _snackbar: MatSnackBar
  ) {


  }
  ngOnInit() {
    this.usuario = localStorage.getItem('usuario');
    this.rol = localStorage.getItem('rol');
    switch (this.rol) {
      case 'Administrador':
        this.acceso = true
        break;
      case 'Operador':
        this.acceso = true
        break;
      case 'Usuario':
        this.acceso = false
        break;
    }
  }
  onFileChange(ev: any) {
    let workBook: any;
    let jsonData;
    const reader = new FileReader();
    const file = ev.target.files[0];
    this.fileAttr = file.name;
    reader.onload = (event) => {
      const data = reader.result;
      workBook = XLSX.read(data, { type: 'binary' });
      jsonData = workBook.SheetNames.reduce((initial: any, name: any) => {
        const sheet = workBook.Sheets[name];
        initial[name] = XLSX.utils.sheet_to_json(sheet);
        return initial;
      }, {});
      if (JSON.stringify(jsonData['Hoja1']) != undefined) {
        this.solicitud = jsonData.Hoja1;
        const dataString = JSON.stringify(jsonData['Hoja1']);
        this.traza = dataString;
        if (this.traza != "")
          this.loadingExcel = false;

      }
      if (JSON.stringify(jsonData['Sheet1']) != undefined) {
        this.solicitud = jsonData.Sheet1;
        const dataString = JSON.stringify(jsonData['Sheet1']);
        this.traza = dataString;
        if (this.traza != "")
          this.loadingExcel = false;

      }
    };
    reader.readAsBinaryString(file);


  }

  exportExcel() {

    this.spinner.show();
    this.service.updateStatus(JSON.parse(`{"lista":${this.traza}, "usuario":"${this.usuario}" }`)).subscribe(
      res => {

        if (res.cod == 0) {
          this.dataNoActualizados = res.data;
          this.recibidos = res.totalRecibidas;
          this.actualizados = res.totalActualizadas;
          this.noActualizados = res.noActualizadas?.length;
          this.response = true;
        }
        this.spinner.hide();
      },
      error => { this.spinner.hide(); }
    )

  }



  exportTemplateAsExcel() {
    const wb: XLSX.WorkBook = XLSX.utils.book_new();
    const ws1: XLSX.WorkSheet = XLSX.utils.aoa_to_sheet(this.templateToExcel1);
    XLSX.utils.book_append_sheet(wb, ws1, 'Hoja1');
    XLSX.writeFile(wb, 'plantilla_solicitud' + '.xlsx');


  }



  cancelar(): void {
    this.traza = "";
    this._dialogRef.close();
  }

  descargarNoAct() {
    const EXCEL_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset = 'UTF-8";
    const EXCEL_EXTENSION = ".xlsx";

    const workSheet = XLSX.utils.json_to_sheet(this.dataNoActualizados);
    const woorBook = {
      Sheets: {
        'noActualizados': workSheet
      },
      SheetNames: ['noActualizados']
    }

    const excelBuffer = XLSX.write(woorBook, { bookType: "xlsx", type: "array" });

    const bookData = new Blob([excelBuffer], { type: EXCEL_TYPE });
    this.fileSaver.save(bookData, "noActualizadas");


  }



  error(mensaje: string) {
    this._snackbar.open(mensaje, 'Cerrar', {
      //duration:5000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    })
  }


}

export interface data {
  _id: string;
  idOlt: number;
  ip: string;
  nombre: string;
  fecha: string;
  estatus: string;
  descripcion: string;
  onts: number;
  usuario: string

}

@Component({
  selector: 'detalleEjecucion',
  templateUrl: './detalleEjecucion.html',
  styleUrls: ['./actualiza-onts.css']
})

export class detalleEjecucionDialog implements OnInit {
  archivo: any;
  dataSource = new MatTableDataSource<data>;
  displayedColumns: string[] = ['ip', 'nombre', 'fecha', 'descripcion', 'usuario', 'totalOnts', 'estatus'];
  ELEMENT_DATA: data[] = [];
  constructor(

    private service: pointService,
    private spinner: NgxSpinnerService,
    private _snackbar: MatSnackBar
  ) {
    setInterval(() => this.getDetalle(), 1000);
    setInterval(() => this.getaArchivo(), 1000);

  }
  ngOnInit() {
    this.getaArchivo();
    this.getDetalle();
  }
  getDetalle() {
    this.service.getDetalleDescubrimiento().subscribe(
      res => {
        this.ELEMENT_DATA = res;
        this.dataSource = new MatTableDataSource<data>(this.ELEMENT_DATA);
      })

  }
  getaArchivo() {
    this.service.getArchivo(1).subscribe(
      res => {
        this.archivo = res;
      })

  }
}

@Component({
  selector: 'actualiza-manual',
  templateUrl: 'actualiza-manual.html',
  styleUrls: ['./actualiza-manual.css']
})
export class DialogElementsExampleDialog implements OnInit {
  panelOpenState = false;
  selection = new SelectionModel<Imetricas>(true, []);
  displayedColumns: string[] = ['nombre', 'huawei', 'zte', 'fh', 'acciones'];
  ELEMENT_DATA: lisMetrics[] = [];
  bloqueAnt: any;
  dataSourceMe: lisMetrics[] = [];
  //dataSourceMe= new  MatTableDataSource<lisMetrics>(this.ELEMENT_DATA);
  arrMetricas: any[] = []
  selectedValue: string | undefined;
  blo1: Imetricas[] = [];
  blo2: Imetricas[] = [];
  blo3: Imetricas[] = [];
  blo4: Imetricas[] = [];
  sb: Imetricas[] = [];
  blo: Bloques[] = [{ bloque: 1, nombre: "Identificación ONTs" }, { bloque: 2, nombre: "Estatus ONTs" }, { bloque: 3, nombre: "Performance ONTs" }, { bloque: 4, nombre: "Otro" }, { bloque: 5, nombre: "Sin bloque asignado" }]

  constructor(
    private service: pointService,
    private spinner: NgxSpinnerService,
    private _snackbar: MatSnackBar
  ) {
  }
  ngOnInit() {
    this.getMetricas();
  }



  getMetricas() {

    this.service.detalleMetricas().subscribe(
      res => {

        this.ELEMENT_DATA = []
        this.dataSourceMe = []
        this.blo1 = [];
        this.blo2 = [];
        this.blo3 = [];
        this.blo4 = [];
        this.sb = [];
        let val
        for (let d in res.entity) {
          val = {
            nombre: res.entity[d].nombre,
            oidHuawei: res.entity[d].zte.oid == '' ? null : res.entity[d].zte.oid,
            oidZte: res.entity[d].huawei.oid == '' ? null : res.entity[d].huawei.oid,
            oidFh: res.entity[d].fh.oid == '' ? null : res.entity[d].fh.oid,
            id_metrica: res.entity[d].id_metrica,
          }

          this.asignaBloque(val, res.entity[d].bloque)

        }
        let valB;
        const bl = this.blo.filter(nombre => nombre.nombre != "Sin bloque asignado")
        for (let b in this.blo) {
          valB = {
            grupo: this.blo[b].nombre,
            items: this.blo[b].bloque == 1 ? this.blo1 :
              this.blo[b].bloque == 2 ? this.blo2 :
                this.blo[b].bloque == 3 ? this.blo3 :
                  this.blo[b].bloque == 4 ? this.blo4 : this.sb,
            bloq: bl.filter(nombre => nombre.nombre != this.blo[b].nombre),
            bloque: this.blo[b].bloque

          }

          this.ELEMENT_DATA.push(valB);
        }

        this.dataSourceMe = this.ELEMENT_DATA

      }
    )
  }
  asignaBloque(data: any, bloque: any) {
    let val

    if (bloque.length > 0) {
      for (let d in bloque) {
        val = {
          nombre: data.nombre,
          oidHuawei: data.oidHuawei,
          oidZte: data.oidZte,
          oidFh: data.oidFh,
          id_metrica: data.id_metrica,

        }
        switch (bloque[d]) {
          case 1:
            this.blo1.push(val)
            break;
          case 2:
            this.blo2.push(val)
            break;
          case 3:
            this.blo3.push(val)
            break;
          case 4:
            this.blo4.push(val)
            break;
        }

      }

    } else {
      val = {
        nombre: data.nombre,
        oidHuawei: data.oidHuawei,
        oidZte: data.oidZte,
        oidFh: data.oidFh,
        id_metrica: data.id_metrica,
      }
      this.sb.push(val)

    }
    this.sb = this.sb.filter(nombre => nombre.nombre != 'SERIAL NUMBER')
  }

  trackByFn(index: number, group: any): number {
    return group.id;
  }

  hasDetails(_: number, group: any): boolean {
    return group.items && group.items.length > 0;
  }

  selec(idMetrica: any, idBloque: any, tipo: any) {

    if (tipo == 'A') {
      this.service.changeMetricas(idMetrica, idBloque).subscribe(
        res => {
          this.ELEMENT_DATA = []
          this.dataSourceMe = []
          this._snackbar.open(res.sms, "Cerrar", {
            duration: 4000
          });
          this.getMetricas();
        }
      )

    } else {
      this.service.removeMetricas(idMetrica, idBloque).subscribe(
        res => {
          this.ELEMENT_DATA = []
          this.dataSourceMe = []

          this._snackbar.open(res.sms, "Cerrar", {
            duration: 4000
          });
          this.getMetricas();
        }

      )

    }
  }
}
export interface Imetricas {
  nombre: string;
  oidHuawei: string;
  oidZte: string;
  oidFh: string;
  id_metrica: number;

}

export interface Bloques {
  bloque: number;
  nombre: string;
}
export interface lisMetrics {
  grupo: string;
  items: Imetricas[]
  bloq: Bloques[]
  bloque: number
}


