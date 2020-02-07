import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Spaceship } from '../models/spaceship.model';
import { Observable } from 'rxjs';

const baseUrl = 'http://localhost:8080/users';

@Injectable({
  providedIn: 'root'
})
export class SpaceshipService {

  constructor(private http: HttpClient) { }

  public findSpaceshipById(id: number): Observable<Spaceship>{
    return this.http.get<Spaceship>(baseUrl + "/" + id);
  }

  public editSpaceshipById(id: number, spaceship: Spaceship): Observable<Spaceship>{
    return this.http.put<Spaceship>(baseUrl + "/" + id, spaceship);
  }

 
}
