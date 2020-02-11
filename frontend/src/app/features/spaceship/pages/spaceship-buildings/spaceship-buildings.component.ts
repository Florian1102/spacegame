import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/services/auth.service';
import { Spaceship } from 'src/app/core/models/spaceship.model';
import { SpaceshipService } from 'src/app/core/services/spaceship.service';
import { User } from 'src/app/core/models/user.model';

@Component({
  selector: 'app-spaceship-buildings',
  templateUrl: './spaceship-buildings.component.html',
  styleUrls: ['./spaceship-buildings.component.css']
})
export class SpaceshipBuildingsComponent implements OnInit {

  user: User;

  constructor(private authService: AuthService, private spaceshipService: SpaceshipService) {
    this.user = this.authService.user;
    authService.changeUser$.subscribe((value) => { 
      this.user = value; 
    });
   }

  ngOnInit() {
  }

  increaseLvl(nameOfBuilding: string){
    if (this.user.spaceship.remainingBuildingDuration > 0) {
      alert("Es befindet sich noch etwas im Bau");
    } else {
      this.spaceshipService.levelUpSpaceshipBuilding(this.user.spaceship.id, nameOfBuilding).subscribe(() => {
        this.authService.updateUser(this.user.id)})
    }
  }

}
