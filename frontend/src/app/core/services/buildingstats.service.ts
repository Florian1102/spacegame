import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Buildingstats } from '../models/buildingstats.model';

const baseUrl = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class BuildingstatsService {

  constructor(private http: HttpClient) { }

  public findBuildingstats(level: number, nameOfBuilding: string): Observable<Buildingstats>{
    return this.http.get<Buildingstats>(baseUrl + "/buildingstats?nameOfBuilding=" + nameOfBuilding + "&level=" + level);
  }
}
