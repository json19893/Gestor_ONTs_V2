import { Component, Inject, OnInit, QueryList, ViewChild, ViewChildren,  } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import { pointService } from '../services/poinst.service';


export interface olts {
    id:               string;
    id_olt:           number;
    nombre:           string;
    ip:               string;
    descripcion:      string;
    tecnologia:       string;
    id_region:        number;
    id_configuracion: number;
    estatus:          number;
    pin:              number;
    descubrio:        boolean;
    total_onts:       number;
    onts:             Ont[];
}

export interface Ont {
    _id:                  string;
    numero_serie:         string;
    oid:                  string;
    fecha_descubrimiento: Date;
    fecha_modificacion:   Date;
    id_olt:               number;
    estatus:              number;
    id_ejecucion:         string;
    alias:                string;
    id_region:            number;
    slot:                 number;
    frame:                number;
    port:                 number;
    tipo:                 string;
    uid:                  string;
    descripcionAlarma:    string;
    lastDownTime:         Date;
    actualizacion:        string;
    id_puerto:            number;
    tecnologia:           string;
    index:                string;
    indexFSP:             string;
    error:                boolean;
    sa:                   boolean;
}
@Component({
selector:'app-inventarioNce',
templateUrl: './inventarioNce.component.html',
styleUrls: ['./inventarioNce.component.css']

})

export class InventarioNceComponent implements OnInit {
    pageSize = 10; // Número de elementos por página
    currentPageIndex = 0; // Página actual
    total=0;
    displayedColumns=['tipo','oid','frame','slot','puerto','uid','numeroSerie', 'alias', 'estatus','fecha','fechaUltimaCaida','desEstatus','acciones'];
    dataSource:olts[]=[];
    @ViewChild(MatPaginator)
    paginator!: MatPaginator;
    startIndex = 0;
    endIndex = 10;
  
    constructor(
        private router:Router,
        private spinner:NgxSpinnerService,
        private service: pointService,
        ){
  // Valor inicial de los índices
        }
    ngOnInit() {
        this.spinner.show();
     
    }
    ngAfterViewInit() {
        this.getRechazadasNce()
        this.paginator.page.subscribe(() => {
          this.startIndex = this.paginator.pageIndex * this.paginator.pageSize;
          this.endIndex = this.startIndex + this.paginator.pageSize;
        });
      }
    getRechazadasNce(){
        this.service.getRechazadasNce().subscribe(
            data => {
                this.dataSource = data;
            },
            err => console.error(err)
          );
     
    }

     onPageChange(event: PageEvent): void {
    this.pageSize = event.pageSize;
    this.currentPageIndex = event.pageIndex;
  }
    
  
}

