import { Spaceship } from './spaceship.model';

export interface Flight {

    id: number;
    spaceship: Spaceship;
    action: string;
    isFlying: boolean;
    startGalaxy: number;
    startSystem: number;
    startPosition: number;
    startTime: Date;
    destinationGalaxy: number;
    destinationSystem: number;
    destinationPosition: number;
    arrivalTime: Date;
    metal: number;
    crystal: number;
    hydrogen: number;
    hydrogenConsumption: number;
}