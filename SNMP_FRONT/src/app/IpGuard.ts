import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { pointService } from './services/poinst.service';

@Injectable({
  providedIn: 'root'
})
export class IpGuard implements CanActivate {
    public ip:any
    constructor(
        private _snackBar: MatSnackBar,
        private router:Router,private service: pointService,){
       
      }
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    this.service.getIp().subscribe(
        res => {
            this.ip=  res.sms

      localStorage.setItem("cl",res.sms)
        },
        err => console.error(err)
        
      );
      this.ip= localStorage.getItem("cl")

    const allowedIP = '192.168.132.145'; 

   // obtiene la dirección IP del cliente de la URL
//alert(document.location.href)
    if (this.ip === allowedIP ) {
      return true;
    } else {
        this._snackBar.open("No tienes permitido acceder a esta pagina", "cerrar",{
            duration: 3000
          });
        this.router.navigate(['/error'])
      return false;
    }
  }
}