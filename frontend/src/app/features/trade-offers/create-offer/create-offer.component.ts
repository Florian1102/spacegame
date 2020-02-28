import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TradeOfferService } from 'src/app/core/services/trade-offer.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/core/models/user.model';
import { TradeOffer } from 'src/app/core/models/trade-offer';
import { Location } from '@angular/common';


@Component({
  selector: 'app-create-offer',
  templateUrl: './create-offer.component.html',
  styleUrls: ['./create-offer.component.css']
})
export class CreateOfferComponent implements OnInit {

  userId: number;
  form: FormGroup;
  message: string;

  metal: number;
  crystal: number;
  hydrogen: number;
  comment: string;
  tradeOfferOfUser: User;
  acceptedByUser: User;
  offerActive: boolean;
  search: boolean;
  offer: boolean;
  course: string;

  constructor(private route: ActivatedRoute, 
              private tradeOfferService: TradeOfferService, 
              private fb: FormBuilder,
              private location: Location) { }

  ngOnInit() {
    this.route.paramMap.subscribe(paramMap => {
      this.userId = +paramMap.get('userId');
    });

    this.form = this.fb.group({
      searchOrOffer: ['', [Validators.required]],
      metal: ['', [Validators.required, Validators.min(0), Validators.max(99999999)]],
      crystal: ['', [Validators.required, Validators.min(0), Validators.max(99999999)]],
      hydrogen: ['', [Validators.required, Validators.min(0), Validators.max(99999999)]],
      course: ['', [Validators.required]],
      comment: [''],
    });
  }

  createTradeOffer(){
    if (this.form.value.searchOrOffer === "search"){
      this.search = true;
      this.offer = false;
    } else {
      this.search = false;
      this.offer = true;
    }
    if (this.form.valid) {
      let tradeOffer: TradeOffer = {
        tradeOfferOfUser: {
          id: this.userId
        },
        metal: this.form.value.metal,
        crystal: this.form.value.crystal,
        hydrogen: this.form.value.hydrogen,
        comment: this.form.value.comment,
        course: this.form.value.course,
        search: this.search,
        offer: this.offer,
        offerActive: true
      };
      this.tradeOfferService.createTradeOffer(tradeOffer).subscribe(() => this.location.back())
    } else {
       alert("Fehlerhafte Eingabe");
    }
  }
  

}
