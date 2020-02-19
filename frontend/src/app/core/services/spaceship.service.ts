import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
    return this.http.get<Spaceship>(baseUrl + "/" + id + "/calculatedurationtocoordinates?galaxy=" + galaxy + "&system=" + system + "&position=" + position);
  }
  
  public showHydrogenConsumptionToCoordinate(id: number, galaxy: number, system: number, position: number): Observable<Spaceship>{
    return this.http.get<Spaceship>(baseUrl + "/" + id + "/calculatehydrogenconsumption?galaxy=" + galaxy + "&system=" + system + "&position=" + position);
  }
  
  public levelUpSpaceshipBuilding(id: number, nameOfBuilding: string): Observable<Spaceship>{
    return this.http.put<Spaceship>(baseUrl + "/" + id + "/" + nameOfBuilding + "/build", null);
  }
  
  public changeToFighterOrMerchant(id: number, fighterOrMerchant: string): Observable<Spaceship>{
    return this.http.put<Spaceship>(baseUrl + "/" + id + "/choosefighterormerchant/" + fighterOrMerchant, null);
  }
  
  public pickUpResources(id: number, planetId: number, metal: number, crystal: number, hydrogen: number): Observable<Spaceship>{
    return this.http.put<Spaceship>(baseUrl + "/" + id + "/pickup/" + planetId + "/resources?metal=" + metal + "&crystal=" + crystal + "&hydrogen=" + hydrogen, null);
  }

}
