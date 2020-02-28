import { Spaceship } from './spaceship.model';
import { Planet } from './planet.model';
import { Technology } from './technology';
import { TradeOffer } from './trade-offer';

export interface User {
    id?: number;
    name?: string;
    points?: number;
    spaceship?: Spaceship;
    planets?: Planet[];
    technology?: Technology;
    daysLoggedIn?: number;
    tradeOffersOfUser?: TradeOffer[];
    tradeOfferAcceptedByUser?: TradeOffer[];

}