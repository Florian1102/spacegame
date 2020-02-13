import { Component, OnInit, Input } from '@angular/core';
import { User } from 'src/app/core/models/user.model';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  userId: number;
  user: User;

  constructor(private route: ActivatedRoute, private userService: UserService) { }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => this.userId = +paramMap.get('userId')); 
    this.findUser();
  }

  findUser() {
    this.userService.findUserById(this.userId).subscribe(foundUser => this.user = foundUser);
  }

}
