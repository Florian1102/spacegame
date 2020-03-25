import { Component, OnInit, Input } from '@angular/core';
import { User } from 'src/app/core/models/user.model';
import { Spaceship } from 'src/app/core/models/spaceship.model';
import { SpaceshipService } from 'src/app/core/services/spaceship.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { FlightService } from 'src/app/core/services/flight.service';
import { Flight } from 'src/app/core/models/flight.model';

@Component({
  selector: 'app-spaceship-resources',
  templateUrl: './spaceship-resources.component.html',
  styleUrls: ['./spaceship-resources.component.css']
})
export class SpaceshipResourcesComponent implements OnInit {

  @Input() user: User;
  spaceship: Spaceship;
  flight: Flight;
  
  constructor(private spaceshipService: SpaceshipService, 
              private authService: AuthService,
              private flightService: FlightService) {
    authService.changeUser$.subscribe((value) => { 
      this.user = value; 
      if (this.user){
        this.findSpaceship();
        if (this.user.spaceship.currentPosition===null){
          this.findActiveFlight();
        }
      }
    });
   }

  ngOnInit() {
    this.findSpaceship();
    this.findActiveFlight();
  }

  findSpaceship(){
    this.spaceshipService.findSpaceshipById(this.user.spaceship.id).subscribe(spaceship => this.spaceship = spaceship);
  }

  findActiveFlight(){
    this.flightService.findActiveFlightOfUser(this.user.spaceship.id).subscribe(flight => this.flight = flight);
  }
}
