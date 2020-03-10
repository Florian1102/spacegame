import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from './components/button/button.component';
import { AdvertisementComponent } from './components/advertisement/advertisement.component';
import { RouterModule } from '@angular/router';
import { RounddownPipe } from './pipes/rounddown.pipe';
import { TimerComponent } from './components/timer/timer.component';


@NgModule({
  declarations: [ButtonComponent, AdvertisementComponent, RounddownPipe, TimerComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    
  ],
  exports: [
    CommonModule,
    FormsModule,
    ButtonComponent,
    ReactiveFormsModule,
    AdvertisementComponent,
    RouterModule,
    RounddownPipe,
    TimerComponent
  ]
})
export class SharedModule { }
