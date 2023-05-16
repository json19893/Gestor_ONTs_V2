import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateFormat'
})
export class DateFormatPipe implements PipeTransform {


  transform(value: any): any {
    console.log("val::: "+value)
    if (!value){
      return "--"
    } 
    if (value=="0-0-0,0:0:0.0,.0:0"){
      return "--"
    }
    const date = new Date(value);
    console.log("date::: "+date)
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear().toString();
    const ampm = date.getHours() >= 12 ? 'PM' : 'AM';
    const hours = date.getHours() % 12;
    const minutes = date.getMinutes();
    return `${day}/${month}/${year} ${hours}:${minutes < 10 ? '0' + minutes : minutes} ${ampm}`;
}
}