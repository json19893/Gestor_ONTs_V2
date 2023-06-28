import { AfterViewChecked, Component, OnInit, ViewChild,Inject } from '@angular/core';
import { DetalleActualizacionOltServiceService } from './service/detalle-actualizacion-olt-service.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { NgxSpinnerService } from 'ngx-spinner';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-detalle-actualizacion-olt',
  templateUrl: './detalle-actualizacion-olt.component.html',
  styleUrls: ['./detalle-actualizacion-olt.component.css']
})
export class DetalleActualizacionOltComponent implements OnInit , AfterViewChecked {

  displayedColumns: string[] = ['ip', 'causa', 'descripcion', 'nombre', 'fechaRecibida', 'fechaRegistro', 'status'];
  listDetalleActualizacionOlts!:any[];
  dataSource = new MatTableDataSource<any>;
  @ViewChild('paginator') paginator: MatPaginator | undefined;

  constructor(
    public dialogRef: MatDialogRef<String>,
    private _serviceDetalleActualizacion:DetalleActualizacionOltServiceService,
    private spinner: NgxSpinnerService,
    @Inject(MAT_DIALOG_DATA) public obj: { ipOlt: string}
    
    ){
      dialogRef.disableClose = false;
  }
  ngAfterViewChecked(): void {
    throw new Error('Method not implemented.');
  }
  ngOnInit(): void {
   
    this._serviceDetalleActualizacion.getDetallesActualizacion(this.obj.ipOlt).subscribe(data=>{
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
