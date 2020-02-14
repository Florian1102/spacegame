import { NgModule } from '@angular/core';
import { PlanetResourcesComponent } from './planet-resources/planet-resources.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { PlanetOverviewComponent } from './planet-overview/planet-overview.component';
import { ColonizePlanetComponent } from './colonize-planet/colonize-planet.component';



@NgModule({
  declarations: [PlanetResourcesComponent, PlanetOverviewComponent, ColonizePlanetComponent],
  imports: [
    SharedModule
  ],
  exports: [
    PlanetResourcesComponent,
    PlanetOverviewComponent,
    ColonizePlanetComponent
  ]
})
export class PlanetModule { }
