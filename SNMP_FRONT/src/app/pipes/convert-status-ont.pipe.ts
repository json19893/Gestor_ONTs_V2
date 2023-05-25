import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'convertStatusOnt'
})
export class ConvertStatusOntPipe implements PipeTransform {

  transform(state: number): String {
    if(state === 0){
      return 'disconnect';
    }

    if(state === 1){
      return 'up';
    }


    if(state === 2){
      return 'cannot pull';
    }

    return 'unknow state';
  }

}

