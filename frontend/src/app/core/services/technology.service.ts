import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Technology } from '../models/technology';

const baseUrl = 'http://localhost:8080/technology';

@Injectable({
  providedIn: 'root'
})
export class TechnologyService {

  constructor(private http: HttpClient) { }

  public findTechnologyById(id: number): Observable<Technology>{
    return this.http.get<Technology>(baseUrl + "/" + id);
  }

  public researchTechnology(id: number, technologyName: string): Observable<Technology>{
    return this.http.put<Technology>(baseUrl + "/" + id + "/" + technologyName + "/research", null);
  }
}
