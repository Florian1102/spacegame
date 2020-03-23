import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Spaceship } from '../models/spaceship.model';
import { Observable } from 'rxjs';

const baseUrl = 'http://localhost:8080/spaceships';

@Injectable({
  providedIn: 'root'
})
export class SpaceshipService {

  constructor(private http: HttpClient) { }

  public findSpaceshipById(id: number): Observable<Spaceship>{
    return this.http.get<Spaceship>(baseUrl + "/" + id);
  }

  public showFlightDurationToCoordinate(id: number, galaxy: number, system: number, position: number): Observable<Spaceship>{
    return this.http.get<Spaceship>(baseUrl + "/" + id + "/calculateDurationForFlightTo?galaxy=" + galaxy + "&system=" + system + "&position=" + position);
  }
  
  public showHydrogenConsumptionToCoordinate(id: number, galaxy: number, system: number, position: number): Observable<Spaceship>{
    return this.http.get<Spaceship>(baseUrl + "/" + id + "/calculateHydrogenConsumptionForFlightTo?galaxy=" + galaxy + "&system=" + system + "&position=" + position);
  }
  
  public levelUpSpaceshipBuilding(id: number, nameOfBuilding: string): Observable<Spaceship>{
    return this.http.put<Spaceship>(baseUrl + "/" + id + "/" + nameOfBuilding + "/build", null);
  }
  
  public changeToFighterOrMerchant(id: number, fighterOrMerchant: string): Observable<Spaceship>{
    return this.http.put<Spaceship>(baseUrl + "/" + id + "/choosefighterormerchant/" + fighterOrMerchant, null);
  }
  
}
