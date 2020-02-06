import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfileComponent } from './pages/profile/profile.component';
import { LoginComponent } from './pages/login/login.component';
import { RegistrationComponent } from './pages/registration/registration.component';



@NgModule({
  declarations: [ProfileComponent, LoginComponent, RegistrationComponent],
  imports: [
    CommonModule
  ]
})
export class AccountModule { }
