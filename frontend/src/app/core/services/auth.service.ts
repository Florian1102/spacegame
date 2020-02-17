import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { Subject } from 'rxjs';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  user: User;
  readonly changeUser$ = new Subject<User>();
  
  constructor(private userService: UserService) { 
    this.user = JSON.parse(localStorage.getItem('user'));
  }

  ngOnInit() {
    
  }

  public loginUser(user: User): void{
    this.user = user;
    this.changeUser$.next(this.user);
    localStorage.setItem("user", JSON.stringify(this.user))
  }

  public logoutUser(): void{
    this.user = null;
    this.changeUser$.next(this.user);
    localStorage.removeItem("user");
  }

  public updateUser(userId: number){
    this.userService.findUserById(userId).subscribe(
      foundUser => {
      this.user = foundUser;
      this.changeUser$.next(this.user);
      localStorage.setItem("user", JSON.stringify(this.user));
      })
  }
}
