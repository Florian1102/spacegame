import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { CoordinateSystemViewComponent } from './coordinate-system-view/coordinate-system-view.component';



@NgModule({
  declarations: [CoordinateSystemViewComponent],
  imports: [
    SharedModule
  ]

})
export class GalaxyModule { }
