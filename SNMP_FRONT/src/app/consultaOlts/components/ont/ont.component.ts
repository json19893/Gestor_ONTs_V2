import { AfterContentInit, AfterViewInit, Component, Inject, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
import { OltsService } from '../../services/olts.service';
import { MatTableDataSource } from '@angular/material/table';
import { OntResponse } from '../../interfaces/ResponseOnt';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { FormControl, FormControlName, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { Olts } from 'src/app/model/names.olts';

@Component({
  selector: 'app-ont',
  templateUrl: './ont.component.html',
  styleUrls: ['./ont.component.css']
})
export class OntComponentDialog implements OnInit, AfterContentInit, OnDestroy, AfterViewInit {

  private currentOlt!: Olts;

  nombre = new FormControl('');

  displayedColumns: string[] = [
    'id',
    'numero_serie',
    'tecnologia',
    'frame',
    'slot',
    'port',
    'fecha_descubrimiento',
    'tipo',
    'eventos'
  ];

  dataSource!: MatTableDataSource<OntResponse>;
  resultsLength = 0;
  isLoadingResults: boolean = true;
  isRateLimitReached: boolean = false;

  myGroup = new FormGroup({
    fechaIni: new FormControl<string>(''),
    fechaFin: new FormControl<string>(''),
  });

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  private obs$!: Observable<OntResponse[]>;

  constructor(public dialogRef: MatDialogRef<String>,
    private service: OltsService,
    @Inject(MAT_DIALOG_DATA) public olt: Olts) { }

  ngOnInit() {
    this.currentOlt = this.olt;
  }

  ngAfterViewInit() {
    //Solicita las onts
    const { id_olt, ip } = this.currentOlt;

    this.obs$ = this.service.obtenerOnts({
      idOlt: id_olt,
      ipOlt: ip,
      fechaIni: '2023-05-16T19:40:58.354+00:00',
      fechaFin: '2023-05-18T19:40:58.354+00:00'
    });

    this.obs$.subscribe(data => {
      this.isLoadingResults = false;
      this.isRateLimitReached = data === null;
      this.resultsLength = data.length;
      this.dataSource = new MatTableDataSource<OntResponse>(data);
    });
  }

  ngAfterContentInit(): void {

  }

  AfterViewInit() {

  }

  ngOnDestroy(): void {
    //Desubcribirte del escucha
  }


  marcarOnt() {
    console.log('');
  }

  filtrarPorRangoFechas() {
    const fechaIni = this.myGroup.value.fechaIni;
    const fechaFin = this.myGroup.value.fechaFin;
    console.log(fechaIni);
    console.log(fechaFin);

    this.service.obtenerOnts({
      idOlt: this.currentOlt.id_olt,
      ipOlt: this.currentOlt.ip,
      fechaIni: fechaIni,
      fechaFin: fechaFin
    });
  }
  //Hace la peticion al servidor para registrar la ont
  procesar(ont: OntResponse) {
    //this.service.homologarOnt({ numeroSerie: ont.numero_serie, tipo: "E" });
  }
}
