import { NgModule } from '@angular/core';
import { ProfileComponent } from './pages/profile/profile.component';
import { LoginComponent } from './pages/login/login.component';
import { RegistrationComponent } from './pages/registration/registration.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { HighscoreComponent } from './pages/highscore/highscore.component';

@NgModule({
  declarations: [ProfileComponent, LoginComponent, RegistrationComponent, HighscoreComponent],
  imports: [
    SharedModule,
   
  ],
 
})
export class AccountModule { }
