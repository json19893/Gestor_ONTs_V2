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
import { getElement } from '@amcharts/amcharts4/core';

@Component({
  selector: 'app-ont',
  templateUrl: './ont.component.html',
  styleUrls: ['./ont.component.css']
})
export class OntComponentDialog implements OnInit, AfterViewInit {
  dateFin: any;
  fechaFin: any;
  responseData!: OntResponse[];


  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
    this.dataSource.paginator = this.paginator;
  }

  private currentOlt!: Olts;

  displayedColumns: string[] = [
    'id', 'numero_serie', 'oid', 'tecnologia',
    'frame', 'slot', 'port',
    'fecha_descubrimiento',
  ];

  dataSource!: MatTableDataSource<OntResponse>;
  resultsLength = 0;

  isLoadingResults: boolean = true;
  isRateLimitReached: boolean = true;

  myGroup = new FormGroup({
    fechaIni: new FormControl<Date>(new Date()),
    fechaFin: new FormControl<Date>(new Date()),
  });

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  private obs$!: Observable<string>;
  tipo: string = "";

  constructor(public dialogRef: MatDialogRef<String>,
    private service: OltsService,
    @Inject(MAT_DIALOG_DATA) public olt: Olts) { }

  ngOnInit() {
    this.currentOlt = this.olt;
  }

  ngAfterViewInit() {
    //Solicita las onts
    const { id_olt, ip } = this.currentOlt;

    const ontsObs = this.service.obtenerOnts({
      idOlt: id_olt,
      ipOlt: ip,
      fechaIni: '2023-05-18T19:40:58.354+00:00',
      fechaFin: '2023-05-30T19:40:58.354+00:00'
    });

    this.isRateLimitReached = true;

    ontsObs.subscribe(data => {
      console.log(data)
      this.isLoadingResults = false;

      if (!(data.length === 0)) {
        this.isRateLimitReached = data === null;
      }
      this.responseData = data;
      this.resultsLength = data.length;
      this.dataSource = new MatTableDataSource<OntResponse>(data);
    });
  }


  marcarOnt() {
    console.log(this.tipo);
  }

  filtrarPorRangoFechas(event: Event) {
    let dt = document.getElementById('dates');

    const fechaIni = this.myGroup.value.fechaIni;
    const fechaFin = this.myGroup.value.fechaFin;

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
    this.dataSource.paginator = this.paginator;

    //Aqui filtra y despues 
    let ontResponse: OntResponse[] = [];
    let pureArray = this.responseData.map(t => t);

    let list: OntResponse[] = [];

    for (const iterator of pureArray.values()) {

      if (fechaFin != null
        && fechaIni != null
        && iterator.fecha_descubrimiento >= fechaIni
        && iterator.fecha_descubrimiento <= fechaFin) {
        console.log(iterator)
        list.push(iterator);
      }
    }

    this.dataSource = new MatTableDataSource<OntResponse>(list);
  }

  polearOLT(olt: Olts) {
    this.isLoadingResults = true;
    this.isRateLimitReached = false;
    //Manda a llamar un spinner
    this.service.polearOLT(olt).subscribe(() => {

    });
  }
}
