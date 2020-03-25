import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CoordinateSystemService } from 'src/app/core/services/coordinate-system.service';
import { CoordinateSystem } from 'src/app/core/models/coordinate-system.model';
import { FormGroup, FormBuilder } from '@angular/forms';
import { SpaceshipService } from 'src/app/core/services/spaceship.service';
import { Spaceship } from 'src/app/core/models/spaceship.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { User } from 'src/app/core/models/user.model';
import { Planet } from 'src/app/core/models/planet.model';

@Component({
  selector: 'app-coordinate-system-view',
  templateUrl: './coordinate-system-view.component.html',
  styleUrls: ['./coordinate-system-view.component.css']
})
export class CoordinateSystemViewComponent implements OnInit {

  coordinateSystem: CoordinateSystem[];
  galaxy: number;
  system: number;
  userId: number;
  user: User;
  spaceshipId: number;

  form: FormGroup;

  constructor(private route: ActivatedRoute, private coordinateSystemService: CoordinateSystemService, private fb: FormBuilder, private authService: AuthService) { }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => {
      this.userId = +paramMap.get('userId')
      this.spaceshipId = +paramMap.get('spaceshipId')
    });
    this.route.queryParamMap.subscribe(queryParamMap => {
      this.galaxy = +queryParamMap.get('galaxy');
      this.system = +queryParamMap.get('system');
    });
    this.findCoordinateSystem(this.galaxy, this.system);
    this.user = this.authService.user;

    this.form = this.fb.group({
      galaxy: [this.galaxy],
      system: [this.system]
    });
  }

  isUsersPlanet(planet: Planet){
    this.user.planets.some(x => x === planet);
  }

  findCoordinateSystem(galaxy: number, system: number){
    this.coordinateSystemService.showCoordinateSystem(galaxy, system).subscribe(foundCoordinateSystem => {
      this.coordinateSystem = foundCoordinateSystem;
      this.sortByPositionAsc();
    },
    error => { alert(error.error) 
    });
  }

  sortByPositionAsc() {
    return this.coordinateSystem.sort((a, b) => a['position'] > b['position'] ? 1 : a['position'] === b['position'] ? 0 : -1);
  }

  search() {
    this.galaxy = this.form.value.galaxy;
    this.system = this.form.value.system;
    if (this.form.valid) {
      this.findCoordinateSystem(this.galaxy, this.system);
    } else {
        alert("Eingabe ungültig");
    }
  }

}
