import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { NavbarInternalComponent } from './components/navbar/navbar-internal/navbar-internal.component';
import { NavbarExternalComponent } from './components/navbar/navbar-external/navbar-external.component';
import { RouterModule } from '@angular/router';
import { AccountModule } from '../features/account/account.module';
import { NavbarUserComponent } from './components/navbar-user/navbar-user.component';

@NgModule({
  declarations: [HeaderComponent, FooterComponent, NavbarComponent, NavbarInternalComponent, NavbarExternalComponent, NavbarUserComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    RouterModule,
    AccountModule
  ],
  exports: [
    HeaderComponent,
    FooterComponent,
    NavbarComponent,
    NavbarUserComponent
  ]
})
export class CoreModule { }
