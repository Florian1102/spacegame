import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { SpaceshipResourcesComponent } from './pages/spaceship-resources/spaceship-resources.component';
import { SpaceshipBuildingsComponent } from './pages/spaceship-buildings/spaceship-buildings.component';
import { OverviewComponent } from './pages/overview/overview.component';
import { SpecializationComponent } from './pages/specialization/specialization.component';
import { FlyToPlanetComponent } from './pages/fly-to-planet/fly-to-planet.component';
import { TechnologyComponent } from './pages/technology/technology.component';



@NgModule({
  declarations: [SpaceshipResourcesComponent, SpaceshipBuildingsComponent, OverviewComponent, SpecializationComponent, FlyToPlanetComponent, TechnologyComponent],
  imports: [
    SharedModule,
  ],
  exports: [
    SpaceshipResourcesComponent,
    SpaceshipBuildingsComponent,
    OverviewComponent,
    SpecializationComponent,
    FlyToPlanetComponent,
    TechnologyComponent
  ]
})
export class SpaceshipModule { }
