import { Component } from '@angular/core';
import { User } from './core/models/user.model';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})


export class AppComponent {
  title = 'Spacegame';

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
