import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { Spaceship } from 'src/app/core/models/spaceship.model';
import { SpaceshipService } from 'src/app/core/services/spaceship.service';
import { ActivatedRoute } from '@angular/router';
import { Buildingstats } from 'src/app/core/models/buildingstats.model';
import { BuildingstatsService } from 'src/app/core/services/buildingstats.service';

@Component({
  selector: 'app-spaceship-buildings',
  templateUrl: './spaceship-buildings.component.html',
  styleUrls: ['./spaceship-buildings.component.css']
})
export class SpaceshipBuildingsComponent implements OnInit {

  userId: number;
  spaceshipId: number;
  spaceship: Spaceship;
  buildingStatsOfSpaceship: Buildingstats;
  buildingStatsOfResearchLaboratory: Buildingstats;

  constructor(private route: ActivatedRoute, 
              private authService: AuthService, 
              private spaceshipService: SpaceshipService,
              private buildingstatsService: BuildingstatsService) {
    
   }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => {
      this.userId = +paramMap.get('userId')
      this.spaceshipId = +paramMap.get('spaceshipId')
    });
    this.findSpaceship();
  }

  findSpaceship(){
    this.spaceshipService.findSpaceshipById(this.spaceshipId).subscribe(foundSpaceship => {
      this.spaceship = foundSpaceship;
      this.getBuildingStats();
    });
  }

  getBuildingStats(){
    this.buildingstatsService.findBuildingstats(this.spaceship.spaceshipLvl + 1, "spaceship").subscribe(foundBuildingStats => this.buildingStatsOfSpaceship = foundBuildingStats);
    this.buildingstatsService.findBuildingstats(this.spaceship.researchLaboratoryLvl + 1, "researchlaboratory").subscribe(foundBuildingStats => this.buildingStatsOfResearchLaboratory = foundBuildingStats);
  }

  increaseLvl(nameOfBuilding: string){
    if (this.spaceship.remainingBuildingDuration > 0) {
      alert("Es befindet sich noch etwas im Bau");
    } else {
      this.spaceshipService.levelUpSpaceshipBuilding(this.spaceship.id, nameOfBuilding).subscribe(() => {
        this.findSpaceship();
        this.authService.updateUser(this.userId)
      },
      error => { alert(error.error) 
      })
    }
  }

}
