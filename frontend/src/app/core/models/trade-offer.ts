import { User } from './user.model';

export interface TradeOffer {

    id: number;
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
}