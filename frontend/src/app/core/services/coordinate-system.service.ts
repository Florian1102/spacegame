import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CoordinateSystem } from '../models/coordinate.model';

const baseUrl = 'http://localhost:8080/coordinatesystem';

@Injectable({
  providedIn: 'root'
})
export class CoordinateSystemService {

  constructor(private http: HttpClient) { }

  public showCoordinateSystem(galaxy: number, system: number): Observable<CoordinateSystem[]>{
    return this.http.get<CoordinateSystem[]>(baseUrl + "?galaxy=" + galaxy + "&system=" + system);
  }
}
