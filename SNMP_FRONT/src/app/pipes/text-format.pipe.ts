import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'textPipe'
})
export class textPipe implements PipeTransform {

  transform(value: any): any {
    if (value!=null||value!=undefined){ 
      return value;
    } else{
      return "---";
    }
}
}