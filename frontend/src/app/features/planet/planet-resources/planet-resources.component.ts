import { Component, OnInit, Input } from '@angular/core';
import { User } from 'src/app/core/models/user.model';
import { Planet } from 'src/app/core/models/planet.model';
import { PlanetService } from 'src/app/core/services/planet.service';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-planet-resources',
  templateUrl: './planet-resources.component.html',
  styleUrls: ['./planet-resources.component.css']
})
export class PlanetResourcesComponent implements OnInit {

  @Input() user: User;
  planets: Planet[];
  
  constructor(private planetService: PlanetService, private authService: AuthService) {
    authService.changeUser$.subscribe((value) => { 
      this.user = value; 
      this.getPlanetData(this.user.id);
    });
  }
  
  ngOnInit() {
    this.getPlanetData(this.user.id);
  }
    
  getPlanetData(userId: number){
    this.planetService.findAllPlanetsOfUser(userId).subscribe(foundPlanets => {
      this.planets = foundPlanets;
      this.sortByPlanetsById();
    });
  }

  sortByPlanetsById() {
    return this.planets.sort((a, b) => a['id'] > b['id'] ? 1 : a['id'] === b['id'] ? 0 : -1);
  }

  deletePlanet(planetId: number){
    this.planetService.deletePlanet(planetId).subscribe(() => this.getPlanetData(this.user.id));
  }

}
