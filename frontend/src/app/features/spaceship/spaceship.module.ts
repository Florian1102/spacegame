import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { SpaceshipResourcesComponent } from './pages/spaceship-resources/spaceship-resources.component';
import { SpaceshipBuildingsComponent } from './pages/spaceship-buildings/spaceship-buildings.component';
import { OverviewComponent } from './pages/overview/overview.component';



@NgModule({
  declarations: [SpaceshipResourcesComponent, SpaceshipBuildingsComponent, OverviewComponent],
  imports: [
    SharedModule,
  ],
  exports: [
    SpaceshipResourcesComponent,
    SpaceshipBuildingsComponent,
    OverviewComponent
  ]
})
export class SpaceshipModule { }
