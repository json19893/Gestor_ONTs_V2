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
    fecha_descubrimiento: string;
    fecha_modificacion:   string;
    id_olt:               number;
    estatus:              string;
    id_ejecucion:         string;
    alias:                string;
    id_region:            number;
    slot:                 number;
    frame:                number;
    port:                 number;
    tipo:                 string;
    uid:                  string;
    descripcionAlarma:    string;
    lastDownTime:         string;
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
                let olts;
                let onts;
                let olts1=[]
                        for( let ol in data){
                            let onts1=[]
                        
                               
                            for(let on in data[ol].onts){
                                onts={
                                    _id: data[ol].onts[on]._id,        
                                    numero_serie: data[ol].onts[on].numero_serie,  
                                    oid:  data[ol].onts[on].oid,        
                                    fecha_descubrimiento: data[ol].onts[on].fecha_descubrimiento, 
                                    fecha_modificacion: data[ol].onts[on].fecha_modificacion, 
                                    id_olt:data[ol].onts[on].id_olt,    
                                    estatus: data[ol].onts[on].estatus ==1? "UP":data[ol].onts[on].estatus==0?"DISCONNECT":"DOWN",            
                                    id_ejecucion:data[ol].onts[on].id_ejecucion,    
                                    alias: data[ol].onts[on].alias,               
                                    id_region:  data[ol].onts[on].id_region,           
                                    slot:   data[ol].onts[on].slot,              
                                    frame:  data[ol].onts[on].frame,                
                                    port:  data[ol].onts[on].port,                 
                                    tipo:   data[ol].onts[on].tipo,                
                                    uid:  data[ol].onts[on].uid,                  
                                    descripcionAlarma:    data[ol].onts[on].descripcionAlarma,  
                                    lastDownTime:  data[ol].onts[on].lastDownTime,         
                                    actualizacion:   data[ol].onts[on].actualizacion,       
                                    id_puerto:   data[ol].onts[on].id_puerto,          
                                    tecnologia: data[ol].onts[on].tecnologia,           
                                    index:      data[ol].onts[on].index,           
                                    indexFSP:  data[ol].onts[on].indexFSP,            
                                    error:    data[ol].onts[on].error,             
                                    sa:    data[ol].onts[on].sa,                 
                                  }  
                                  onts1.push(onts);
                            }
                            olts={  
                            id: data[ol].id,              
                            id_olt:data[ol].id_olt,         
                            nombre:data[ol].nombre,             
                            ip:data[ol].ip,                 
                            descripcion:data[ol].descripcion,        
                            tecnologia:data[ol].tecnologia,          
                            id_region:data[ol].id_region,           
                            id_configuracion:data[ol].id_configuracion,     
                            estatus:data[ol].estatus,             
                            pin:data[ol].pin,                  
                            descubrio:data[ol].descubrio,           
                            total_onts:data[ol].total_onts,         
                            onts: onts1
                            }
                            olts1.push(olts)
                        }
                this.dataSource = olts1;
            },
            err => console.error(err)
          );
     
    }


    
  
}

