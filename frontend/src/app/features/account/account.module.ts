import { NgModule } from '@angular/core';
import { ProfileComponent } from './pages/profile/profile.component';
import { LoginComponent } from './pages/login/login.component';
import { RegistrationComponent } from './pages/registration/registration.component';
import { SharedModule } from 'src/app/shared/shared.module';

@NgModule({
  declarations: [ProfileComponent, LoginComponent, RegistrationComponent],
  imports: [
    SharedModule,
   
  ],
 
})
export class AccountModule { }
