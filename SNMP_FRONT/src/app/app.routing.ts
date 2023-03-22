import { ModuleWithProviders} from "@angular/core";
import {Routes, RouterModule} from "@angular/router";
import {ErrorComponent} from './404/404.component';
import {HomeComponent} from './home/home.component';
import { LoginComponent } from "./login/login.component";
import {MenuComponent} from './menu/menu.component';



const routes: Routes = [
    {path:'', component:MenuComponent},
    {path:'login',component:LoginComponent},
    {path:'home',component:MenuComponent},
    {path:'**',component:ErrorComponent}
   ];
   
   export const AppRoutinProviders: any[]=[];
   export const routing: ModuleWithProviders <any> = RouterModule.forRoot(routes);
