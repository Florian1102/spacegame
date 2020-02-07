import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user.model';
import { Observable } from 'rxjs';

const baseUrl = 'http://localhost:8080/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  public findUserByName(name: string): Observable<User>{
    return this.http.get<User>(baseUrl + "?username=" + name);
  }

  public findUserById(id: number): Observable<User>{
    return this.http.get<User>(baseUrl + "/" + id);
  }

  public createUser(user: User): Observable<User>{
    return this.http.post<User>(baseUrl, user);
  }

  public editUserById(id: number, user: User): Observable<User>{
    return this.http.put<User>(baseUrl + "/" + id, user);
  }

  public deleteUserById(id: number): Observable<User>{
    return this.http.delete<User>(baseUrl + "/" + id);
  }

  public getHighscore(): Observable<User>{
    return this.http.get<User>(baseUrl + "/highscore");
  }

  
  
}
