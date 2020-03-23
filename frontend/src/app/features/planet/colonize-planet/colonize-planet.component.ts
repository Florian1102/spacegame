import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/core/services/user.service';
import { User } from 'src/app/core/models/user.model';
import { PlanetService } from 'src/app/core/services/planet.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { FlightService } from 'src/app/core/services/flight.service';

@Component({
  selector: 'app-colonize-planet',
  templateUrl: './colonize-planet.component.html',
  styleUrls: ['./colonize-planet.component.css']
})
export class ColonizePlanetComponent implements OnInit {

  form: FormGroup;
  userId: number;
  spaceshipId: number;
  message: string;

  constructor(private route: ActivatedRoute, 
              private flightService: FlightService, 
              private authService: AuthService, 
              private fb: FormBuilder) { }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => this.userId = +paramMap.get('userId'));
    this.route.paramMap.subscribe(paramMap => this.spaceshipId = +paramMap.get('spaceshipId'));

    this.form = this.fb.group({
      galaxy: ['galaxy'],
      system: ['system'],
      position: ['position']
    });
  }

  colonize(){
    this.flightService.flyToCoordinateWithAction(this.spaceshipId, "colonize", this.form.value.galaxy, this.form.value.system, this.form.value.position, 0, 0, 0).subscribe(() => 
    this.authService.updateUser(this.userId),
     error => { alert(error.error) 
     })
  }

}
