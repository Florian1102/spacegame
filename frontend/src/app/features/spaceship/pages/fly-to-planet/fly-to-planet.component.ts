import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Planet } from 'src/app/core/models/planet.model';
import { PlanetService } from 'src/app/core/services/planet.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SpaceshipService } from 'src/app/core/services/spaceship.service';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-fly-to-planet',
  templateUrl: './fly-to-planet.component.html',
  styleUrls: ['./fly-to-planet.component.css']
})
export class FlyToPlanetComponent implements OnInit {

  userId: number;
  planetId: number;
  spaceshipId: number;
  planet: Planet;

  metal: number = 0;
  crystal: number = 0;
  hydrogen: number = 0;
  galaxy: number;
  system: number;
  position: number;

  message: string;

  form: FormGroup;
  pickupOrDeliverState: boolean = true;

  constructor(private planetService: PlanetService, 
              private spaceshipService: SpaceshipService, 
              private authService: AuthService, 
              private route: ActivatedRoute, 
              private router: Router, 
              private fb: FormBuilder) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
   }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => {
      this.userId = +paramMap.get('userId')
      this.spaceshipId = +paramMap.get('spaceshipId')
      this.planetId = +paramMap.get('planetId')
    });
    
    this.findPlanet();
    this.form = this.fb.group({
      metal: ['', [Validators.required, Validators.min(0)]],
      crystal: ['', [Validators.required, Validators.min(0)]],
      hydrogen: ['', [Validators.required, Validators.min(0)]],
      galaxy: [''],
      system: [''],
      position: ['']
    });
  }

  findPlanet(){
    this.planetService.findPlanetById(this.planetId).subscribe(foundPlanet => {
      this.planet = foundPlanet;
    });
  }

  changeState(){
    this.pickupOrDeliverState = !this.pickupOrDeliverState;
  }

  pickupResources(){
    this.metal = this.form.value.metal;
    this.crystal = this.form.value.crystal;
    this.hydrogen = this.form.value.hydrogen;
    if (this.form.valid) {
      this.spaceshipService.pickUpResources(this.spaceshipId, this.planetId, this.metal, this.crystal, this.hydrogen).subscribe(()=> {
        this.message = "Das Raumschiff ist losgeflogen";
        this.authService.updateUser(this.userId);
        this.form = this.fb.group({
          metal: [],
          crystal: [],
          hydrogen: []
        });
      })
    } else {
        alert("Eingabe ungültig");
    }
  }

  // maxResources(){ Funktioniert noch nicht, da er die Felder als String betrachtet
  //   this.form = this.fb.group({
  //     metal: [this.planet.metal],
  //     crystal: [this.planet.crystal],
  //     hydrogen: [this.planet.hydrogen]
  //   });
  // }

  deliverResources(){
    this.metal = this.form.value.metal;
    this.crystal = this.form.value.crystal;
    this.hydrogen = this.form.value.hydrogen;
    this.galaxy = this.form.value.galaxy;
    this.system = this.form.value.system;
    this.position = this.form.value.position;
    if (this.form.valid) {
      this.spaceshipService.deliverResources(this.spaceshipId, this.metal, this.crystal, this.hydrogen, this.galaxy, this.system, this.position).subscribe(()=> {
        this.message = "Das Raumschiff ist losgeflogen";
        this.authService.updateUser(this.userId);
        this.form = this.fb.group({
          metal: [''],
          crystal: [''],
          hydrogen: [''],
          galaxy: [''],
          system: [''],
          position: [''],
        });
     })
    } else {
         alert("Eingabe ungültig");
     }
  }

}
