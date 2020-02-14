import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PlanetService } from 'src/app/core/services/planet.service';
import { Planet } from 'src/app/core/models/planet.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { BuildingstatsService } from 'src/app/core/services/buildingstats.service';
import { Buildingstats } from 'src/app/core/models/buildingstats.model';

@Component({
  selector: 'app-planet-overview',
  templateUrl: './planet-overview.component.html',
  styleUrls: ['./planet-overview.component.css']
})
export class PlanetOverviewComponent implements OnInit {

  userId: number;
  planetId: number;
  planet: Planet;
  buildingStatsOfCommandCentral: Buildingstats;
  buildingStatsOfMetalMine: Buildingstats;
  buildingStatsOfCrystalMine: Buildingstats;
  buildingStatsOfHydrogenPlant: Buildingstats;
  buildingStatsOfSolarPowerPlant: Buildingstats;
  buildingStatsOfMetalStorehouse: Buildingstats;
  buildingStatsOfCrystalStorehouse: Buildingstats;
  buildingStatsOfHydrogenTank: Buildingstats;
  buildingStatsOfSolarsatellite: Buildingstats;

  constructor(private route: ActivatedRoute, 
              private router: Router, 
              private planetService: PlanetService, 
              private authService: AuthService,
              private buildingstatsService: BuildingstatsService) { 
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
    this.planetService.findPlanetById(this.planetId).subscribe(foundPlanet => {
      this.planet = foundPlanet;
      this.getBuildingStats();
    });
  }

  levelUpPlanetBuilding(nameOfBuilding: string){
    this.planetService.levelUpPlanetBuilding(this.planetId, nameOfBuilding).subscribe(() => {
      this.findPlanet();
      this.authService.updateUser(this.userId);

    })
  }
  // @coach: Hast du hier einen Tipp, sodass man nicht immer so viele Abfragen auf einmal machen muss?

  getBuildingStats(){
    this.buildingstatsService.findBuildingstats(this.planet.commandCentralLvl + 1, "commandcentral").subscribe(foundBuildingStats => this.buildingStatsOfCommandCentral = foundBuildingStats);
    this.buildingstatsService.findBuildingstats(this.planet.metalMineLvl + 1, "metalmine").subscribe(foundBuildingStats => this.buildingStatsOfMetalMine = foundBuildingStats);
    this.buildingstatsService.findBuildingstats(this.planet.crystalMineLvl + 1, "crystalmine").subscribe(foundBuildingStats => this.buildingStatsOfCrystalMine = foundBuildingStats);
    this.buildingstatsService.findBuildingstats(this.planet.hydrogenPlantLvl + 1, "hydrogenplant").subscribe(foundBuildingStats => this.buildingStatsOfHydrogenPlant = foundBuildingStats);
    this.buildingstatsService.findBuildingstats(this.planet.solarPowerPlantLvl + 1, "solarpowerplant").subscribe(foundBuildingStats => this.buildingStatsOfSolarPowerPlant = foundBuildingStats);
    this.buildingstatsService.findBuildingstats(this.planet.metalStorehouseLvl + 1, "metalstorehouse").subscribe(foundBuildingStats => this.buildingStatsOfMetalStorehouse = foundBuildingStats);
    this.buildingstatsService.findBuildingstats(this.planet.crystalStorehouseLvl + 1, "crystalstorehouse").subscribe(foundBuildingStats => this.buildingStatsOfCrystalStorehouse = foundBuildingStats);
    this.buildingstatsService.findBuildingstats(this.planet.hydrogenTankLvl + 1, "hydrogentank").subscribe(foundBuildingStats => this.buildingStatsOfHydrogenTank = foundBuildingStats);
    this.buildingstatsService.findBuildingstats(1, "solarsatellite").subscribe(foundBuildingStats => this.buildingStatsOfSolarsatellite = foundBuildingStats);
  }
}
