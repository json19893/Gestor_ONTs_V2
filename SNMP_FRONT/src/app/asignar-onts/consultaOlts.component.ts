import { Component, ElementRef, Inject, OnInit, ViewChild } from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { ThemePalette } from "@angular/material/core";
import { OltsService } from "./service/asignar-onts.service";
import { olts } from "./interfaces";


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
  columnsToDisplay = ['id_olt', 'nombre' ,'ip'];
  headers = ['Id', 'Nombre' ,'Ip'];

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
}


