import { AfterContentInit, Component, Input, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { pointService } from 'src/app/services/poinst.service';

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.css']
})
export class LogsComponent implements OnInit, AfterContentInit {

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action);
  }

  @Input('logs') logs!: string[];

  estadoSyncronizacion!: string;
  splitLogs: string[] = [];

  constructor(private service: pointService,
    private _snackBar: MatSnackBar) { }

  ngAfterContentInit(): void {
    this.splitLogs = this.estadoSyncronizacion.split('\n');
  }

  ngOnInit(): void {
    //Consulta el logs de la sincronizacion
    this.estadoSyncronizacion = '[ 2023-06-05T13:17:24.727 ] INFO [Ejecutando Poleo Manual]: PoleoMetricasImpl.getPoleoOntMetrica'
      + '\n[ 2023-06-05T13:17:24.731 ] INFO [Ejecutando el comando snmp]: snpmget procesando peticion...'
      + '\n[ 2023-06-05T13:17:24.818 ] INFO [Termino la Ejecuccion del Comando snmp]: ifName'
      + '\n[ 2023-06-05T13:17:24.820 ] INFO [Datos Generales del Poleo]'
      + '\n[ 2023-06-05T13:17:24.731 ] INFO [Ejecutando el comando snmp]: snpmget procesando peticion...'
      + '\nfinal del log'
    //Lee el archivo
    this.service.getArchivo(2)
      .subscribe((archivo) => {
        this.splitLogs = this.estadoSyncronizacion.split('\n');
      });
  }
}
