import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Planet } from '../models/planet.model';

const baseUrl = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class PlanetService {

  constructor(private http: HttpClient) { }

  public findAllSpaceships(): Observable<Planet[]>{
    return this.http.get<Planet[]>(baseUrl + "/planets");
  }

  public findSpaceshipById(id: number): Observable<Planet>{
    return this.http.get<Planet>(baseUrl + "/planets/" + id);
  }

  public addPlanet(userId: number, galaxy: number, system: number, position: number): Observable<Planet>{
    return this.http.post<Planet>(baseUrl + "/" + userId + "/planets/add?galaxy=" + galaxy + "&system=" + system + "&position=" + position, null);
  }

  public levelUpPlanetBuilding(id: number, nameOfBuilding: string): Observable<Planet>{
    return this.http.put<Planet>(baseUrl + "/planets/" + id + "/" + nameOfBuilding + "/build", null);
  }

  public deletePlanet(id: number): Observable<void>{
    return this.http.delete<void>(baseUrl + "/planets/" + id);
  }
}
