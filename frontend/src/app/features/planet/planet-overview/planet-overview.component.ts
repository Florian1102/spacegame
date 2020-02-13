import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PlanetService } from 'src/app/core/services/planet.service';
import { Planet } from 'src/app/core/models/planet.model';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-planet-overview',
  templateUrl: './planet-overview.component.html',
  styleUrls: ['./planet-overview.component.css']
})
export class PlanetOverviewComponent implements OnInit {

  userId: number;
  planetId: number;
  planet: Planet;

  constructor(private route: ActivatedRoute, private router: Router, private planetService: PlanetService, private authService: AuthService) { 
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
  }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => {
      this.userId = +paramMap.get('userId')
      this.planetId = +paramMap.get('planetId')
      
    });
    this.findPlanet();
  }

  findPlanet(){
    this.planetService.findPlanetById(this.planetId).subscribe(foundPlanet => this.planet = foundPlanet);
  }

  levelUpPlanetBuilding(nameOfBuilding: string){
    this.planetService.levelUpPlanetBuilding(this.planetId, nameOfBuilding).subscribe(() => {
      this.findPlanet();
      this.authService.updateUser(this.planet.user.id);

    })
  }
}
