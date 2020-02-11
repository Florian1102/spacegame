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


const routes: Routes = [
  { path: '', redirectTo: "start", pathMatch: "full"},
  { path: 'start', component: StartComponent},
  { path: 'aboutus', component: AboutusComponent},
  { path: 'imprint', component: ImprintComponent},
  { path: 'login', component: LoginComponent, canActivate: [NoAuthGuard]},
  { path: 'registration', component: RegistrationComponent, canActivate: [NoAuthGuard]},
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
  { path: 'overview', component: OverviewComponent, canActivate: [AuthGuard]},
  { path: 'spaceship-buildings', component: SpaceshipBuildingsComponent, canActivate: [AuthGuard]},
  // { path: 'users/:id', component: ActivityDetailPageComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
