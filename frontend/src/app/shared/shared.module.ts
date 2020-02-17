import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from './components/button/button.component';
import { AdvertisementComponent } from './components/advertisement/advertisement.component';
import { RouterModule } from '@angular/router';
import { RounddownPipe } from './pipes/rounddown.pipe';


@NgModule({
  declarations: [ButtonComponent, AdvertisementComponent, RounddownPipe],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule
  ],
  exports: [
    CommonModule,
    FormsModule,
    ButtonComponent,
    ReactiveFormsModule,
    AdvertisementComponent,
    RouterModule,
    RounddownPipe
  ]
})
export class SharedModule { }
