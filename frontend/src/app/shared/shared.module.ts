import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from './components/button/button.component';
import { AdvertisementComponent } from './components/advertisement/advertisement.component';


@NgModule({
  declarations: [ButtonComponent, AdvertisementComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  exports: [
    CommonModule,
    FormsModule,
    ButtonComponent,
    ReactiveFormsModule,
    AdvertisementComponent
  ]
})
export class SharedModule { }
