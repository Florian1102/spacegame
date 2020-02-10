import { NgModule } from '@angular/core';
import { PlanetResourcesComponent } from './planet-resources/planet-resources.component';
import { SharedModule } from 'src/app/shared/shared.module';



@NgModule({
  declarations: [PlanetResourcesComponent],
  imports: [
    SharedModule
  ],
  exports: [
    PlanetResourcesComponent
  ]
})
export class PlanetModule { }
