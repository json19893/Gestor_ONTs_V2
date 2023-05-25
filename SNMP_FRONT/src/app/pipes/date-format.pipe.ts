import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateFormat'
})
export class DateFormatPipe implements PipeTransform {


  transform(value: any): any {

    if (!value){
      return "--"
    } 
    else if (value=="NULL"){
      console.log("valueee NULL "+value)
      return "--"
    } 
    else if (value==null){
      return "--"
    } 
    else if (value==undefined){
      return "--"
    } 
   else if (value=="0-0-0,0:0:0.0,.0:0"){
    
      return "--"
    }else if (value=="No se cuenta con Oid para polear"){
      return "--"
    }else if (value==null){
      return "--"
    }
  else if (value=='---'){
    return "--"
  }
    else{
   
    const date = new Date(value);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear().toString();
    const ampm = date.getHours()+6 >= 12 ? 'PM' : 'AM';
    const hours = date.getHours() % 12;
    const minutes = date.getMinutes();
    return `${day}/${month}/${year} ${hours+6}:${minutes < 10 ? '0' + minutes : minutes} ${ampm}`;
  }
}
}