import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StartComponent } from './features/content/start/start.component';
import { AboutusComponent } from './features/content/aboutus/aboutus.component';
import { ImprintComponent } from './features/content/imprint/imprint.component';
import { LoginComponent } from './features/account/pages/login/login.component';
import { RegistrationComponent } from './features/account/pages/registration/registration.component';
import { ProfileComponent } from './features/account/pages/profile/profile.component';
import { OverviewComponent } from './features/spaceship/pages/overview/overview.component';
import { SpaceshipBuildingsComponent } from './features/spaceship/pages/spaceship-buildings/spaceship-buildings.component';
import { AuthGuard } from './core/guards/auth.guard';
import { NoAuthGuard } from './core/guards/no-auth.guard';
import { PlanetOverviewComponent } from './features/planet/planet-overview/planet-overview.component';
import { CoordinateSystemViewComponent } from './features/galaxy/coordinate-system-view/coordinate-system-view.component';
import { ColonizePlanetComponent } from './features/planet/colonize-planet/colonize-planet.component';
import { SpecializationComponent } from './features/spaceship/pages/specialization/specialization.component';
import { RenamePlanetComponent } from './features/planet/rename-planet/rename-planet.component';


const routes: Routes = [
  { path: '', redirectTo: "start", pathMatch: "full"},
  { path: 'start', component: StartComponent},
  { path: 'aboutus', component: AboutusComponent},
  { path: 'imprint', component: ImprintComponent},
  { path: 'login', component: LoginComponent, canActivate: [NoAuthGuard]},
  { path: 'registration', component: RegistrationComponent, canActivate: [NoAuthGuard]},
  { path: 'user/:userId/profile', component: ProfileComponent, canActivate: [AuthGuard]},
  { path: 'user/:userId/overview', component: OverviewComponent, canActivate: [AuthGuard]},
  { path: 'user/:userId/spaceship/:spaceshipId/buildings', component: SpaceshipBuildingsComponent, canActivate: [AuthGuard]},
  { path: 'user/:userId/spaceship/:spaceshipId/specialization', component: SpecializationComponent, canActivate: [AuthGuard]},
  { path: 'user/:userId/planet/colonize', component: ColonizePlanetComponent, canActivate: [AuthGuard]},
  { path: 'user/:userId/planet/:planetId/rename', component: RenamePlanetComponent, canActivate: [AuthGuard]},
  { path: 'user/:userId/planet/:planetId', component: PlanetOverviewComponent, canActivate: [AuthGuard]},
  { path: 'user/:userId/coordinatesystem', component: CoordinateSystemViewComponent, canActivate: [AuthGuard]},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
