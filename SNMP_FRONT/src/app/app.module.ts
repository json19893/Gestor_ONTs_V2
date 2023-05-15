import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { routing ,AppRoutinProviders} from './app.routing';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {ErrorComponent} from './404/404.component';
import {HomeComponent} from './home/home.component';
import {MenuComponent} from './menu/menu.component';
import { EstatusComponent } from './estatus/estatus.component';
import { MaterialModule } from './material.module';
import { NgxSpinnerModule } from "ngx-spinner";
import { GoogleMapsModule } from '@angular/google-maps'
import { HttpClientModule ,HttpClient} from '@angular/common/http';
import { MatPaginatorIntl } from '@angular/material/paginator';
import { getDutchPaginatorIntl } from './PaginatorI18n';
import { LoginComponent } from './login/login.component';
import { pointService } from './services/poinst.service';
import { DetalleOntComponent } from './detalleOnt/detalleOnt.component';
import {detalleActualizacionDialog} from './actualizacion/actualizacion.component'
import {detalleEjecucionMetricaDialog} from './detalleOnt/detalleOnt.component'
import {ClasificacionComponent} from './clasificacion/clasificacion.component';
import { BottomSheetOverviewExampleSheet} from'./menu/menu.component';
import { DialogContentExampleDialog } from './menu/menu.component';
import { ActualizacionComponent } from './actualizacion/actualizacion.component'
import {detalleActualizacionOnt} from './detalleOnt/detalleOnt.component'
import { ActualizaOntsDialog, ConsultaOltsComponent ,detalleEjecucionDialog, DialogElementsExampleDialog} from './consultaOlts/consultaOlts.component';
import { FileSaverModule } from 'ngx-filesaver';
import {DateFormatPipe} from './pipes/date-format.pipe'
import {numberPipe} from './pipes/number-format.pipe'
import {textPipe} from './pipes/text-format.pipe'

@NgModule({
  declarations: [
    AppComponent,
    ErrorComponent,
    HomeComponent,
    MenuComponent,
    EstatusComponent,
    LoginComponent,
    DetalleOntComponent,
    ClasificacionComponent,
    BottomSheetOverviewExampleSheet,
    DialogContentExampleDialog,
    ActualizacionComponent,
    ConsultaOltsComponent,
    ActualizaOntsDialog,
    detalleEjecucionDialog,
    DialogElementsExampleDialog,
    detalleActualizacionDialog,
    detalleEjecucionMetricaDialog,
    DateFormatPipe,
    numberPipe,
    textPipe,
    detalleActualizacionOnt

   

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    routing,
    FormsModule,
    MaterialModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    NgxSpinnerModule,
    GoogleMapsModule,
    HttpClientModule,
    FileSaverModule
  
    ],
    
  providers: [AppRoutinProviders,BrowserModule,pointService,{ provide: MatPaginatorIntl, useValue: getDutchPaginatorIntl() }],
  bootstrap: [AppComponent]
})
export class AppModule { }
