import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/core/models/user.model';
import { AuthService } from 'src/app/core/services/auth.service';
import { SpaceshipService } from 'src/app/core/services/spaceship.service';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent implements OnInit {

  userId: number;
  user: User;

  constructor(private route: ActivatedRoute, private userService: UserService) {
    
   }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => this.userId = +paramMap.get('userId'));
    this.findUser();
  }

  findUser(){
    this.userService.findUserById(this.userId).subscribe(foundUser => this.user = foundUser);
  }

}
