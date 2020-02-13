import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CoordinateSystemService } from 'src/app/core/services/coordinate-system.service';
import { CoordinateSystem } from 'src/app/core/models/coordinate.model';
import { FormGroup, FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-coordinate-system-view',
  templateUrl: './coordinate-system-view.component.html',
  styleUrls: ['./coordinate-system-view.component.css']
})
export class CoordinateSystemViewComponent implements OnInit {

  coordinateSystem: CoordinateSystem[];
  galaxy: number;
  system: number;

  form: FormGroup;

  constructor(private route: ActivatedRoute, private coordinateSystemService: CoordinateSystemService, private fb: FormBuilder) { }

  ngOnInit() {
    this.route.queryParamMap.subscribe(paramMap => {
      this.galaxy = +paramMap.get('galaxy');
      this.system = +paramMap.get('system');
    });
    this.findCoordinateSystem(this.galaxy, this.system);

    this.form = this.fb.group({
      galaxy: ['galaxy'],
      system: ['system']
    });
  }

  findCoordinateSystem(galaxy: number, system: number){
    this.coordinateSystemService.showCoordinateSystem(galaxy, system).subscribe(foundCoordinateSystem => this.coordinateSystem = foundCoordinateSystem);
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
