import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PlanetService } from 'src/app/core/services/planet.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { Planet } from 'src/app/core/models/planet.model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-rename-planet',
  templateUrl: './rename-planet.component.html',
  styleUrls: ['./rename-planet.component.css']
})
export class RenamePlanetComponent implements OnInit {

  planet: Planet;
  planetId: number;
  userId: number;
  name: string;

  form: FormGroup;

  constructor(private route: ActivatedRoute, private planetService: PlanetService, private authService: AuthService, private fb: FormBuilder) { }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => {
      this.userId = +paramMap.get('userId')
      this.planetId = +paramMap.get('planetId')
      
    });
    this.findPlanet();

    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
    });
  }

  findPlanet(){
    this.planetService.findPlanetById(this.planetId).subscribe(foundPlanet => this.planet = foundPlanet);
  }

  renamePlanet(){
    this.name = this.form.value.name;
    if (this.form.valid) {
      this.planetService.renamePlanet(this.planetId, this.name).subscribe(() => {
      this.authService.updateUser(this.userId);
      this.planet.name = this.name;
      this.form = this.fb.group({
        name: [''],
      });
      });
    } else {
        alert("Eingabe ungültig");
    }
  }

}
