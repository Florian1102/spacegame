import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { User } from '../../models/user.model';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar-user',
  templateUrl: './navbar-user.component.html',
  styleUrls: ['./navbar-user.component.css']
})
export class NavbarUserComponent implements OnInit {

  @Input() user: User;
  
  constructor(private authService: AuthService,
              private router: Router) { }

  ngOnInit() {
  }

  logoutUser(){
    this.authService.logoutUser();
    this.router.navigate(['start']);
  }
}
