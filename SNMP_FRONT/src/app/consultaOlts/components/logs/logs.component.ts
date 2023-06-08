import { AfterContentInit, AfterViewInit, Component, EventEmitter, Input, OnDestroy, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observer, Subscription, interval, mergeMap } from 'rxjs';
import { pointService } from 'src/app/services/poinst.service';

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.css']
})
export class LogsComponent implements OnInit, OnDestroy {
  observable: any;

  @Input() estaSincronizando = new EventEmitter<boolean>();
  procesar: boolean = false;

  obtenerLogs!: string;
  splitLogs: any;

  private listener$!: Subscription;
  constructor(private service: pointService,
    private _snackBar: MatSnackBar) { }

  ngOnDestroy(): void {
    this.listener$.unsubscribe();
  }

  ngOnInit(): void {

    this.listener$ = this.estaSincronizando
      .asObservable()
      .subscribe((sincronizacion) => {
        console.log('Iniziando sincronizacion');
        console.log('Conectandose con el servidor');
        let id = setTimeout(()=> console.clear(), 5000);


        let subcription = interval(2000)
          .pipe(
            mergeMap(() => {
              
              return this.preguntarPorLogs()
            })
          ).subscribe(subject);

        if (!sincronizacion) {
          subcription.unsubscribe();
        }
      });

    const subject = {
      next: (archivo: any) => {
        //Setteo de datos para la renderiza los datos
        this.splitLogs = archivo;
      },
      error: (err: Error) => {
        console.log(`Hubo un error en el consumo de la api: ${err.message}`);
      },
      complete: () => {
        console.log('Se completó la sincronización');
      }
    };

    this.preguntarPorLogs().subscribe(subject).unsubscribe();
  }

  //Si esta sincronizando sigo preguntando por el estado del log
  preguntarPorLogs() {
    return this.service.getArchivo(2);
  }
}
