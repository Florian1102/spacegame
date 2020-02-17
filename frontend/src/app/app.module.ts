import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoreModule } from './core/core.module';
import { ContentModule } from './features/content/content.module';
import { SpaceshipModule } from './features/spaceship/spaceship.module';
import { SharedModule } from './shared/shared.module';
import { PlanetModule } from './features/planet/planet.module';
import { GalaxyModule } from './features/galaxy/galaxy.module';
import { registerLocaleData } from '@angular/common';
import localeDe from '@angular/common/locales/de';
registerLocaleData(localeDe);


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    ContentModule,
    SpaceshipModule,
    PlanetModule,
    GalaxyModule,
    SharedModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
