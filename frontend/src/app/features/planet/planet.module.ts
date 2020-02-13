import { NgModule } from '@angular/core';
import { PlanetResourcesComponent } from './planet-resources/planet-resources.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { PlanetOverviewComponent } from './planet-overview/planet-overview.component';



@NgModule({
  declarations: [PlanetResourcesComponent, PlanetOverviewComponent],
  imports: [
    SharedModule
  ],
  exports: [
    PlanetResourcesComponent,
    PlanetOverviewComponent
  ]
})
export class PlanetModule { }
