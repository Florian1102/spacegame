import { Component, OnInit } from '@angular/core';
import { Spaceship } from 'src/app/core/models/spaceship.model';
import { Buildingstats } from 'src/app/core/models/buildingstats.model';
import { Technology } from 'src/app/core/models/technology';
import { TechnologyService } from 'src/app/core/services/technology.service';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { BuildingstatsService } from 'src/app/core/services/buildingstats.service';
import { SpaceshipService } from 'src/app/core/services/spaceship.service';

@Component({
  selector: 'app-technology',
  templateUrl: './technology.component.html',
  styleUrls: ['./technology.component.css']
})
export class TechnologyComponent implements OnInit {

  userId: number;
  technologyId: number;
  technology: Technology;
  spaceshipId: number;
  spaceship: Spaceship;
  buildingStatsOfEnergyResearch: Buildingstats;
  buildingStatsOfResourceResearch: Buildingstats;
  
  constructor(private route: ActivatedRoute, 
              private authService: AuthService, 
              private technologyService: TechnologyService,
              private spaceshipService: SpaceshipService,
              private buildingstatsService: BuildingstatsService) { }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => {
      this.userId = +paramMap.get('userId')
      this.spaceshipId = +paramMap.get('spaceshipId')
      this.technologyId = +paramMap.get('technologyId')
    });
    this.findTechnology();
    this.findSpaceship();
  }

  findTechnology(){
    this.technologyService.findTechnologyById(this.technologyId).subscribe(foundTechnology => {
      this.technology = foundTechnology;
      this.getBuildingStats();
    });
  }

  findSpaceship(){
    this.spaceshipService.findSpaceshipById(this.spaceshipId).subscribe(foundSpaceship => {
      this.spaceship = foundSpaceship;
    });
  }

  getBuildingStats(){
    this.buildingstatsService.findBuildingstats(this.technology.energyResearchLvl + 1, "energyresearch").subscribe(foundBuildingStats => this.buildingStatsOfEnergyResearch = foundBuildingStats);
    this.buildingstatsService.findBuildingstats(this.technology.resourceResearchLvl + 1, "resourceresearch").subscribe(foundBuildingStats => this.buildingStatsOfResourceResearch = foundBuildingStats);
  }

  increaseLvl(nameOfResearch: string){
    if (this.spaceship.remainingResearchDuration > 0) {
      alert("Es wird bereits etwas erforscht");
    } else {
      this.technologyService.researchTechnology(this.technologyId, nameOfResearch).subscribe(() => {
        this.findTechnology();
        this.findSpaceship(); 
        this.authService.updateUser(this.userId)})
    }
  }

}
