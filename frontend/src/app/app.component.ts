import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})


export class AppComponent {
  title = 'Spacegame';

  userIsLoggedIn: boolean = false;

  handleChangeUserStatus(userStatus: boolean){
    this.userIsLoggedIn = userStatus;
  }
}
