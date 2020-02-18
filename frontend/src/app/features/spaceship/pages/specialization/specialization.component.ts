import { Component, OnInit } from '@angular/core';
import { SpaceshipService } from 'src/app/core/services/spaceship.service';
import { ActivatedRoute } from '@angular/router';
import { Spaceship } from 'src/app/core/models/spaceship.model';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-specialization',
  templateUrl: './specialization.component.html',
  styleUrls: ['./specialization.component.css']
})
export class SpecializationComponent implements OnInit {

  userId: number;
  spaceshipId: number;
  spaceship: Spaceship;

  constructor(private route: ActivatedRoute, private spaceshipService: SpaceshipService, private authService: AuthService) { }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => {
      this.userId = +paramMap.get('userId');
      this.spaceshipId = +paramMap.get('spaceshipId');
    })
    this.findSpaceship();
  }

  findSpaceship(){
    this.spaceshipService.findSpaceshipById(this.spaceshipId).subscribe(foundSpaceship => this.spaceship = foundSpaceship);
  }

  chooseSpecialization(merchantOrFighter: string){
    this.spaceshipService.changeToFighterOrMerchant(this.spaceshipId, merchantOrFighter).subscribe(() => {
      this.findSpaceship();
      this.authService.updateUser(this.userId);
    })
  }

}
