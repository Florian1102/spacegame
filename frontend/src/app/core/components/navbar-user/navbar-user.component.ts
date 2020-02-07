import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-navbar-user',
  templateUrl: './navbar-user.component.html',
  styleUrls: ['./navbar-user.component.css']
})
export class NavbarUserComponent implements OnInit {

  @Input() userIsLoggedIn: boolean;
  @Output() changeUserStatus = new EventEmitter();
  
  constructor() { }

  ngOnInit() {
  }

  loginUser(){
    this.userIsLoggedIn = true;
    this.changeUserStatus.emit(this.userIsLoggedIn);
  }

  logoutUser(){
    this.userIsLoggedIn = false;
    this.changeUserStatus.emit(this.userIsLoggedIn);
  }
}
