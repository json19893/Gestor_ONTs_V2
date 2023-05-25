import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'convertStatusOnt'
})
export class ConvertStatusOntPipe implements PipeTransform {

  transform(state: number): String {
    if(state === 0){
      return 'DISCONECT';
    }

    if(state === 1){
      return 'UP';
    }


    if(state === 2){
      return 'DOWN';
    }

    return '---';
  }

}

