import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TradeOffer } from '../models/trade-offer';

const baseUrl = 'http://localhost:8080/tradeoffers';

@Injectable({
  providedIn: 'root'
})
export class TradeOfferService {

  constructor(private http: HttpClient) { }

  public showActiveTradeOffers(): Observable<TradeOffer[]>{
    return this.http.get<TradeOffer[]>(baseUrl + "/active");
  }

  public showAcceptedTradeOffers(): Observable<TradeOffer[]>{
    return this.http.get<TradeOffer[]>(baseUrl + "/accepted");
  }

  public showTradeOffer(id: number): Observable<TradeOffer>{
    return this.http.get<TradeOffer>(baseUrl + "/" + id);
  }

  public showTradeOffersOfUser(userId: number): Observable<TradeOffer[]>{
    return this.http.get<TradeOffer[]>(baseUrl + "/user/" + userId);
  }

  public createTradeOffer(tradeOffer: TradeOffer): Observable<TradeOffer>{
    return this.http.post<TradeOffer>(baseUrl, tradeOffer);
  }

  public editTradeOffer(id: number, tradeOffer: TradeOffer): Observable<TradeOffer>{
    return this.http.put<TradeOffer>(baseUrl + "/" + id, tradeOffer);
  }
  
  public deleteTradeOffer(id: number, userId: number): Observable<void>{
    return this.http.delete<void>(baseUrl + "/" + id + "/" + userId);
  }

  public acceptTradeOffer(id: number, userId: number): Observable<TradeOffer>{
    return this.http.put<TradeOffer>(baseUrl + "/" + id + "/accept?userid=" + userId, null);
  }

  public declineTradeOffer(id: number, userId: number): Observable<TradeOffer>{
    return this.http.put<TradeOffer>(baseUrl + "/" + id + "/decline?userid=" + userId, null);
  }
  
}
