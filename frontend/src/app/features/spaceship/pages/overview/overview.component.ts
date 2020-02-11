import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/core/models/user.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { SpaceshipService } from 'src/app/core/services/spaceship.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent implements OnInit {

  user: User;

  constructor(private authService: AuthService) {
    this.user = this.authService.user;
    authService.changeUser$.subscribe((value) => { 
      this.user = value; 
    });
   }

  ngOnInit() {
  }

}
