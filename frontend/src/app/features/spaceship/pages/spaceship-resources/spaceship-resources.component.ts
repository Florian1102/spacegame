import { Component, OnInit, Input } from '@angular/core';
import { User } from 'src/app/core/models/user.model';
import { Spaceship } from 'src/app/core/models/spaceship.model';
import { SpaceshipService } from 'src/app/core/services/spaceship.service';

@Component({
  selector: 'app-spaceship-resources',
  templateUrl: './spaceship-resources.component.html',
  styleUrls: ['./spaceship-resources.component.css']
})
export class SpaceshipResourcesComponent implements OnInit {

  @Input() user: User;
  spaceship: Spaceship;
  
  constructor(private spaceshipService: SpaceshipService) { }

  ngOnInit() {
    this.findSpaceship();
  }

  findSpaceship(){
    this.spaceshipService.findSpaceshipById(this.user.spaceship.id).subscribe(spaceship => this.spaceship = spaceship);
  }
}
