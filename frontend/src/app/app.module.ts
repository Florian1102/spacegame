import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { ContentModule } from './features/content/content.module';
import { SpaceshipModule } from './features/spaceship/spaceship.module';
import { SharedModule } from './shared/shared.module';
import { PlanetModule } from './features/planet/planet.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    ContentModule,
    SpaceshipModule,
    PlanetModule,
    SharedModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
