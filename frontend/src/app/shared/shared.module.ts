import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from './components/button/button.component';
import { AdvertisementComponent } from './components/advertisement/advertisement.component';
import { RouterModule } from '@angular/router';


@NgModule({
  declarations: [ButtonComponent, AdvertisementComponent],
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
    RouterModule
  ]
})
export class SharedModule { }
