import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateFormat'
})
export class DateFormatPipe implements PipeTransform {
  /*transform(date: Date): string {
   
   
    if(isNaN(date.getTime())){
      return "---"
    }else{
      const day = date.getDate();
    const month = date.getMonth() + 1;
    const year = date.getFullYear();
    const ampm = date.getHours() >= 12 ? 'PM' : 'AM';
    const hours = date.getHours() % 12;
    const minutes = date.getMinutes();

    return `${day}/${month}/${year} ${hours}:${minutes < 10 ? '0' + minutes : minutes} ${ampm}`;
  }
   
   
  }*/

  transform(value: any, format: string = 'dd/MM/yyyy'): any {
    if (!value) return '----';
    const date = new Date(value);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear().toString();
    const ampm = date.getHours() >= 12 ? 'PM' : 'AM';
    const hours = date.getHours() % 12;
    const minutes = date.getMinutes();
    return `${day}/${month}/${year} ${hours}:${minutes < 10 ? '0' + minutes : minutes} ${ampm}`;
}
}