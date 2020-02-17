import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'rounddown'
})
export class RounddownPipe implements PipeTransform {

  transform(value: number): number {
    return Math.floor(value);
  }

}
