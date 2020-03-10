import { User } from './user.model';

export interface Technology {
    id?: number;
    user: User;
    energyResearchLvl?: number;
    energyResearchFactor?: number;
    resourceResearchLvl?: number;
    resourceResearchFactor?: number;
    endOfResearch: Date;
	nameOfResearch: string;
	currentLvlOfReseearch: number;
	
}