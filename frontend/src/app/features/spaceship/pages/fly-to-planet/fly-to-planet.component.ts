import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Planet } from 'src/app/core/models/planet.model';
import { PlanetService } from 'src/app/core/services/planet.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SpaceshipService } from 'src/app/core/services/spaceship.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { Spaceship } from 'src/app/core/models/spaceship.model';
import { FlightService } from 'src/app/core/services/flight.service';

@Component({
  selector: 'app-fly-to-planet',
  templateUrl: './fly-to-planet.component.html',
  styleUrls: ['./fly-to-planet.component.css']
})
export class FlyToPlanetComponent implements OnInit {

  userId: number;
  spaceshipId: number;
  spaceship: Spaceship;
  planet: Planet;
  planetId: number;

  metal: number = 0;
  crystal: number = 0;
  hydrogen: number = 0;
  galaxy: number;
  system: number;
  position: number;
  action: string;

  message: string;

  form: FormGroup;
  pickupOrDeliverState: boolean = true;

  constructor(private planetService: PlanetService, 
              private spaceshipService: SpaceshipService,
              private flightService: FlightService, 
              private authService: AuthService, 
              private route: ActivatedRoute, 
              private router: Router, 
              private fb: FormBuilder) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
   }

  ngOnInit() {
    this.form = this.fb.group({
      action: ['', [Validators.required]],
      metal: ['', [Validators.required, Validators.min(0)]],
      crystal: ['', [Validators.required, Validators.min(0)]],
      hydrogen: ['', [Validators.required, Validators.min(0)]],
      galaxy: ['', [Validators.required]],
      system: ['', [Validators.required]],
      position: ['', [Validators.required]]
    });
    
    this.route.paramMap.subscribe(paramMap => {
      this.userId = +paramMap.get('userId')
      this.spaceshipId = +paramMap.get('spaceshipId')
    });
    this.route.queryParamMap.subscribe(queryParamMap => {
      this.planetId = +queryParamMap.get('planetid');
      if (this.planetId){
        this.findPlanet();
        
      }
    });
    this.findSpaceship();
    
  }
  
  findPlanet(){
    this.planetService.findPlanetById(this.planetId).subscribe(foundPlanet => {
      this.planet = foundPlanet;
        this.form = this.fb.group({
          action: ['pickup', [Validators.required]],
          metal: [this.planet.metal, [Validators.required, Validators.min(0)]],
          crystal: [this.planet.crystal, [Validators.required, Validators.min(0)]],
          hydrogen: [this.planet.hydrogen, [Validators.required, Validators.min(0)]],
          galaxy: [this.planet.coordinates.galaxy, [Validators.required]],
          system: [this.planet.coordinates.system, [Validators.required]],
          position: [this.planet.coordinates.position, [Validators.required]]
        });
    });
  }

  findSpaceship(){
    this.spaceshipService.findSpaceshipById(this.spaceshipId).subscribe(foundSpaceship => {
      this.spaceship = foundSpaceship;
    });
  }

  changeState(){
    this.pickupOrDeliverState = !this.pickupOrDeliverState;
  }

  // maxResourcesOfPlanet(){ 
  //   this.form = this.fb.group({
  //     metal: [Math.floor(this.planet.metal)],
  //     crystal: [Math.floor(this.planet.crystal)],
  //     hydrogen: [Math.floor(this.planet.hydrogen)]
  //   });
  // }

  // maxResourcesOfSpaceship(){ 
  //   this.form = this.fb.group({
  //     metal: [Math.floor(this.spaceship.metal)],
  //     crystal: [Math.floor(this.spaceship.crystal)],
  //     hydrogen: [Math.floor(this.spaceship.hydrogen)],
  //     galaxy: [''],
  //     system: [''],
  //     position: [''],
  //   });
  // }

  flyToCoordinate(){
    this.metal = this.form.value.metal;
    this.crystal = this.form.value.crystal;
    this.hydrogen = this.form.value.hydrogen;
    this.galaxy = this.form.value.galaxy;
    this.system = this.form.value.system;
    this.position = this.form.value.position;
    this.action = this.form.value.action;
    if (this.form.valid) {
      this.flightService.flyToCoordinateWithAction(this.spaceshipId, this.action, this.galaxy, this.system, this.position, this.metal, this.crystal, this.hydrogen).subscribe(()=> {
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
     },
     error => { alert(error.error) 
     })
    } else {
         alert("Eingabe ungültig");
     }
  }

}
