import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TradeOffer } from 'src/app/core/models/trade-offer';
import { TradeOfferService } from 'src/app/core/services/trade-offer.service';

@Component({
  selector: 'app-offers-overview',
  templateUrl: './offers-overview.component.html',
  styleUrls: ['./offers-overview.component.css']
})
export class OffersOverviewComponent implements OnInit {

  userId: number;
  tradeOffers: TradeOffer[];

  constructor(private route: ActivatedRoute, private tradeOfferService: TradeOfferService) { }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => {
      this.userId = +paramMap.get('userId');
    });
    this.findActiveTradeOffers();
  }

  findActiveTradeOffers(){
    this.tradeOfferService.showActiveTradeOffers().subscribe(foundTradeOffers => this.tradeOffers = foundTradeOffers);
  }

  findAcceptedTradeOffers(){
    this.tradeOfferService.showAcceptedTradeOffers().subscribe(foundTradeOffers => this.tradeOffers = foundTradeOffers);
  }

  findTradeOffersOfUser(){
    this.tradeOfferService.showTradeOffersOfUser(this.userId).subscribe(foundTradeOffers => this.tradeOffers = foundTradeOffers);
  }

  acceptTradeOfffer(id: number){
    this.tradeOfferService.acceptTradeOffer(id, this.userId).subscribe(()=> this.findActiveTradeOffers());
  }

  declineTradeOfffer(id: number){
    this.tradeOfferService.declineTradeOffer(id, this.userId).subscribe(()=> this.findTradeOffersOfUser());
  }

  deleteTradeOffer(id: number){
    this.tradeOfferService.deleteTradeOffer(id, this.userId).subscribe(()=> this.findTradeOffersOfUser());
  }

}
