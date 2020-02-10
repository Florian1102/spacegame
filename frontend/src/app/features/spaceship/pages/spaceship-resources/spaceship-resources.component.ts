import { Component, OnInit, Input } from '@angular/core';
import { User } from 'src/app/core/models/user.model';

@Component({
  selector: 'app-spaceship-resources',
  templateUrl: './spaceship-resources.component.html',
  styleUrls: ['./spaceship-resources.component.css']
})
export class SpaceshipResourcesComponent implements OnInit {

  @Input() user: User;
  
  constructor() { }

  ngOnInit() {
  }

}
