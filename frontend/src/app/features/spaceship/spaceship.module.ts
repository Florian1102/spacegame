import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { SpaceshipResourcesComponent } from './pages/spaceship-resources/spaceship-resources.component';
import { SpaceshipBuildingsComponent } from './pages/spaceship-buildings/spaceship-buildings.component';



@NgModule({
  declarations: [SpaceshipResourcesComponent, SpaceshipBuildingsComponent],
  imports: [
    SharedModule,
  ],
  exports: [
    SpaceshipResourcesComponent,
    SpaceshipBuildingsComponent
  ]
})
export class SpaceshipModule { }
