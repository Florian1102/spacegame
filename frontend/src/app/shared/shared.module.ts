import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from './components/button/button.component';
import { FormGroup } from '@angular/forms';



@NgModule({
  declarations: [ButtonComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [
    CommonModule,
    FormsModule,
    ButtonComponent,
    ReactiveFormsModule

  ]
})
export class SharedModule { }
