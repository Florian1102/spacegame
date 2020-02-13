import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { Spaceship } from 'src/app/core/models/spaceship.model';
import { SpaceshipService } from 'src/app/core/services/spaceship.service';
import { User } from 'src/app/core/models/user.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-spaceship-buildings',
  templateUrl: './spaceship-buildings.component.html',
  styleUrls: ['./spaceship-buildings.component.css']
})
export class SpaceshipBuildingsComponent implements OnInit {

  userId: number;
  spaceshipId: number;
  spaceship: Spaceship;

  constructor(private route: ActivatedRoute, private authService: AuthService, private spaceshipService: SpaceshipService) {
    
   }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => {
      this.userId = +paramMap.get('userId')
      this.spaceshipId = +paramMap.get('spaceshipId')
    });
    this.findSpaceship();
  }

  findSpaceship(){
    this.spaceshipService.findSpaceshipById(this.spaceshipId).subscribe(foundSpaceship => this.spaceship = foundSpaceship);
  }

  increaseLvl(nameOfBuilding: string){
    if (this.spaceship.remainingBuildingDuration > 0) {
      alert("Es befindet sich noch etwas im Bau");
    } else {
      this.spaceshipService.levelUpSpaceshipBuilding(this.spaceship.id, nameOfBuilding).subscribe(() => {
        this.findSpaceship();
        this.authService.updateUser(this.userId)})
    }
  }

}
