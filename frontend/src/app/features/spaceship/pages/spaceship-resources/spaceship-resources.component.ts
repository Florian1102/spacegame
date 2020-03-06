import { Component, OnInit, Input } from '@angular/core';
import { User } from 'src/app/core/models/user.model';
import { Spaceship } from 'src/app/core/models/spaceship.model';
import { SpaceshipService } from 'src/app/core/services/spaceship.service';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-spaceship-resources',
  templateUrl: './spaceship-resources.component.html',
  styleUrls: ['./spaceship-resources.component.css']
})
export class SpaceshipResourcesComponent implements OnInit {

  @Input() user: User;
  spaceship: Spaceship;
  
  constructor(private spaceshipService: SpaceshipService, private authService: AuthService) {
    authService.changeUser$.subscribe((value) => { 
      this.user = value; 
      if (this.user){
        this.findSpaceship();
      }
    });
   }

  ngOnInit() {
    this.findSpaceship();
  }

  findSpaceship(){
    this.spaceshipService.findSpaceshipById(this.user.spaceship.id).subscribe(spaceship => this.spaceship = spaceship);
  }
}
