import { Planet } from './planet.model';
import { Spaceship } from './spaceship.model';

export interface CoordinateSystem {
    
    id: number;
    galaxy: number;
    system: number;
    position: number;
    planet: Planet;
    spaceships: Spaceship[];
}