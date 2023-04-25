import { Component, OnInit,  } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import {NgxSpinnerService} from 'ngx-spinner';
import { pointService } from '../services/poinst.service';
import { Subject } from 'rxjs'; 
import {login} from '../model/login';
import {MatSnackBar} from '@angular/material/snack-bar';
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
      
        this.parLogin=new login(this.loginForm.value.u,this.loginForm.value.p,this.cli.toString());
        this.service.login(this.parLogin).subscribe(
          res => {
            this.sesion=res;
          if( this.sesion.cod==0){
            localStorage.setItem('cod_sesion','1');
            localStorage.setItem('usuario',this.sesion.usuario);
            localStorage.setItem('nombreCompleto',this.sesion.nombreCompleto);
            localStorage.setItem('rol',this.sesion.rol);
            window.location.reload();
          }else{
            this._snackBar.open(this.sesion.sms, "cerrar",{
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