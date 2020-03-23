import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Flight } from '../models/flight.model';
import { Observable } from 'rxjs';

const baseUrl = 'http://localhost:8080/flights';

@Injectable({
  providedIn: 'root'
})
export class FlightService {

  constructor(private http: HttpClient) { }

  public findActiveFlightOfUser(spaceshipId: number): Observable<Flight>{
    return this.http.get<Flight>(baseUrl + "/" + spaceshipId + "/activeflight");
  }

  public flyToCoordinateWithAction(spaceshipId: number, action: string, galaxy: number, system: number, position: number, metal: number, crystal: number, hydrogen: number): Observable<Flight>{
    return this.http.post<Flight>(baseUrl + "/" + spaceshipId + "/" + action + "?galaxy=" + galaxy + "&system=" + system + "&position=" + position + "&metal=" + metal + "&crystal=" + crystal + "&hydrogen=" + hydrogen, null);
  }

}
