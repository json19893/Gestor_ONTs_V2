import { Component, ElementRef, Inject, OnInit, ViewChild } from "@angular/core";
import { OltsService } from "./service/asignar-onts.service";
import { olts } from "./interfaces";
import { DialogElementsExampleDialogComponent } from "./components/dialog-elements-example-dialog/dialog-elements-example-dialog.component";


@Component({
  selector: 'app-asignacionOlts',
  templateUrl: './consultaOlts.component.html',
  styleUrls: ['./consultaOlts.component.css']

})

export class ConsultaOltsAsignacionComponent implements OnInit {

  /*public usuario: any;
  public dat:any;
  @ViewChild('table') table: ElementRef | undefined;
  @ViewChild('paginator') paginator: MatPaginator | undefined;
  public rol: any;
  color: ThemePalette = 'primary';*/
  

  public collection: olts[] = [];
  //columnsToDisplay = ['select', 'ip', 'nombre', 'tecnologia', 'id_region', 'totalOnts', 'descripcion', 'slide','opciones'];
  columnsToDisplay = ['idolt', 'nombre' ,'ip'];
  headers = ['Id', 'Nombre' ,'Ip'];
  dialog: any;

  constructor(private oltsService: OltsService) {}
  /** Whether the number of selected elements matches the total number of rows. */
  ngOnInit() {
    this.oltsService.getOlts().subscribe(olt=>{
        console.log(olt);
        this.collection = olt;
    });
  }

  
  getDataTable() {

  }

  applyFilter($event: KeyboardEvent) {
    
  }

  openDialog() {
    this.dialog.open(DialogElementsExampleDialogComponent,{
      height: '80vh',
      width: '130vw',
    });  
  }

  ExportTOExcel() {
    
  }
  
  openDetalle() {
    
  }
}