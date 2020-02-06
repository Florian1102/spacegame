import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from './components/button/button.component';



@NgModule({
  declarations: [ButtonComponent],
  imports: [
    CommonModule,
    FormsModule
  ],
  exports: [
    FormsModule,
    ButtonComponent,
    
  ]
})
export class SharedModule { }
