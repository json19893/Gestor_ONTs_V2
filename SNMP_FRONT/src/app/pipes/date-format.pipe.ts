import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateFormat'
})
export class DateFormatPipe implements PipeTransform {
  transform(value: any): any {
    if(value!=null){
    const date = new Date(value);
    const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();
    const ampm = date.getHours() >= 12 ? 'PM' : 'AM';
    const hours = date.getHours() % 12;
    const minutes = date.getMinutes();

    return `${day}/${month}/${year} ${hours}:${minutes < 10 ? '0' + minutes : minutes} ${ampm}`;
    }else{
        return "";
    }
  }
}