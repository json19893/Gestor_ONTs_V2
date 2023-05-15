import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'totalPipe'
})
export class numberPipe implements PipeTransform {

  transform(value: any): any {
    console.log("pipe value "+value)
    if (value>0){ 
      return value;
    } else{
      return 0;
    }

  
}
}