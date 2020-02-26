import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { CreateOfferComponent } from './create-offer/create-offer.component';
import { OffersOverviewComponent } from './offers-overview/offers-overview.component';


@NgModule({
  declarations: [CreateOfferComponent, OffersOverviewComponent],
  imports: [
    SharedModule
  ]
})
export class TradeOffersModule { }
