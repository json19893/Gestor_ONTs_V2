import { Component, OnInit, ViewChild } from '@angular/core';
import { DetalleActualizacionOltServiceService } from './service/detalle-actualizacion-olt-service.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-detalle-actualizacion-olt',
  templateUrl: './detalle-actualizacion-olt.component.html',
  styleUrls: ['./detalle-actualizacion-olt.component.css']
})
export class DetalleActualizacionOltComponent implements OnInit {

  displayedColumns: string[] = ['ip', 'causa', 'descripcion', 'nombre', 'fechaRecibida', 'fechaRegistro', 'status'];
  listDetalleActualizacionOlts!:any[];
  dataSource = new MatTableDataSource<any>;
  @ViewChild('paginator') paginator: MatPaginator | undefined;

  constructor(private _serviceDetalleActualizacion:DetalleActualizacionOltServiceService,
    private spinner: NgxSpinnerService,){

  }
  ngOnInit(): void {
   
    this._serviceDetalleActualizacion.getDetallesActualizacion().subscribe(data=>{
      console.log("------------ ",data);

      this.listDetalleActualizacionOlts = data;

      this.dataSource = new MatTableDataSource<any>(data);
      this.dataSource!.paginator = this.paginator!;
        this.spinner.hide();
     
    })
    
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
    this.dataSource!.paginator = this.paginator!;
  }

}
