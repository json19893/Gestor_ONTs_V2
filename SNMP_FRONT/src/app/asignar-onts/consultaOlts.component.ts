import { Component, ElementRef, Inject, OnInit, ViewChild } from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { ThemePalette } from "@angular/material/core";


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
  
  columnsToDisplay = ['select', 'ip', 'nombre', 'tecnologia', 'id_region', 'totalOnts', 'descripcion', 'slide','opciones'];
  headers = ['Ip OLT', 'Nombre', 'Tecnología', 'Región'];

  constructor() {

  }
  /** Whether the number of selected elements matches the total number of rows. */
  ngOnInit() {
    
  }

  
  getDataTable() {

  }

  applyFilter($event: KeyboardEvent) {
    
  }
}


