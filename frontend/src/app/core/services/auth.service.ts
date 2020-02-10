import { Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  user: User;
  readonly changeUser$ = new Subject<User>();
  
  constructor() { }

  public loginUser(user: User): void{
    this.user = user;
    this.changeUser$.next(this.user);
  }

  public logoutUser(): void{
    this.changeUser$.next(null);
  }
}
