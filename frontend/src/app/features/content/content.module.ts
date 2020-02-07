import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StartComponent } from './start/start.component';
import { ImprintComponent } from './imprint/imprint.component';
import { AboutusComponent } from './aboutus/aboutus.component';



@NgModule({
  declarations: [StartComponent, ImprintComponent, AboutusComponent],
  imports: [
    CommonModule
  ],
  exports: [
   
  ]
})
export class ContentModule { }
