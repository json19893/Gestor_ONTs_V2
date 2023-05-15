import { Component, OnInit,  } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import { pointService } from '../services/poinst.service';
import { Subject } from 'rxjs'; 
import {login} from '../model/login';
import {MatSnackBar} from '@angular/material/snack-bar';
import * as CryptoJS from 'crypto-js';
@Component({
selector:'app-login',
templateUrl: './login.component.html',
styleUrls: ['./login.component.css']

})
export class LoginComponent implements OnInit {
  public parLogin:login | undefined;
  public sesion:any;
  public ses:any;
  public cli:any
  cod_sesion:any;
    hide = true;
    loginForm: FormGroup | any;
    constructor(
      private _snackBar: MatSnackBar,
      private router:Router,
      private service: pointService,
      private spinner:NgxSpinnerService){
        this.loginForm = new FormGroup({
            u: new FormControl('', [Validators.required]),
            p: new FormControl('', [Validators.required])
          });
    }
    ngOnInit() {
     
      this.service.getIp().subscribe(
        res => {
      res

          localStorage.setItem("cl",res.sms)
        },
        err => console.error(err)
        
      );
       this.ses=localStorage.getItem('cod_sesion');
      if(this.ses==1){
        this.router.navigate(['/home'])
      }else{
        this.router.navigate(['/login'])
      }
      this.spinner.show();
        setTimeout(() => { this.spinner.hide(); }, 1000,);

    }

  

  login(){
    this.spinner.show();
    if(!this.loginForm.valid){
        return;
      }else{
       this.cli= localStorage.getItem("cl")
       //const clave = CryptoJS.enc.Utf8.parse('GESTORONTSV2T');
       var key = CryptoJS.enc.Base64.parse("o9szYIOq1rRMiouNhNvaq96lqUvCekxR");
       var srcs = CryptoJS.enc.Utf8.parse(this.loginForm.value.u)
       var srcs1 = CryptoJS.enc.Utf8.parse(this.loginForm.value.p)
       const us =CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7}); //CryptoJS.AES.encrypt(this.loginForm.value.u, clave).toString();
       const ps =CryptoJS.AES.encrypt(srcs1, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});  //CryptoJS.AES.encrypt(this.loginForm.value.p, clave).toString();
      let u1=us.toString();
      let p1=ps.toString();
     
        this.parLogin=new login(u1,p1,this.cli.toString());
        this.service.login(this.parLogin).subscribe(
          res => {
            this.sesion=res;
          if( this.sesion.cod==0){
            localStorage.setItem('cod_sesion','1');
            localStorage.setItem('usuario',this.sesion.usuario);
            localStorage.setItem('nombreCompleto',this.sesion.nombreCompleto);
            localStorage.setItem('rol',this.sesion.rol);
            localStorage.setItem( "detalleClasificacion","false"); 
            localStorage.removeItem('tipo');
            localStorage.setItem('detalleOnts', 'false');
            localStorage.setItem('muestraHome', 'false');
            localStorage.setItem('muestraDetalle', 'false');
            localStorage.removeItem('IdRegion');
            localStorage.removeItem('nombreRegion');
            localStorage.removeItem('dataRegion');
            localStorage.removeItem('totalHuaweiRe');
            localStorage.removeItem('totalZteRe');
            localStorage.removeItem('totalFhRe');
            localStorage.removeItem('totalArribaZteRe');
            localStorage.removeItem('totalArribaHuaweiRe');
            localStorage.removeItem('totalArribaFhRe');
            localStorage.removeItem('totalAbajoHuaweiRe');
            localStorage.removeItem('totalAbajoZteRe');
            localStorage.removeItem('totalAbajoFhRe');
            localStorage.removeItem('totalHuaweiEmpRe');
            localStorage.removeItem('totalZteEmpRe');
            localStorage.removeItem('totalFhEmpRe');
            localStorage.removeItem('totalArribaZteEmpRe');
            localStorage.removeItem('totalArribaHuaweiEmpRe');
            localStorage.removeItem('totalArribaFhEmpRe');
            localStorage.removeItem('totalAbajoHuaweiEmpRe');
            localStorage.removeItem('totalAbajoZteEmpRe');
            localStorage.removeItem('totalAbajoFhEmpRe');
            localStorage.removeItem('busqueda');
            localStorage.removeItem('nom');
            localStorage.removeItem('ip');
            localStorage.removeItem('page');
            localStorage.removeItem('alias');
            localStorage.removeItem('ns');
            localStorage.removeItem('dataDetalle');
            localStorage.removeItem("upDetalle");
            localStorage.removeItem("downDetalle");
            localStorage.removeItem("totalDetalle");
            localStorage.removeItem("cambiosDetalle");
            window.location.reload();
          }else{
            this._snackBar.open(this.sesion.sms, "Cerrar",{
              duration: 3000
            });
          }
          this.spinner.hide();
          },
          err => console.error(err)
          
        );
      }
  }

}