import { Planet } from './planet.model';

export interface CoordinateSystem {
    id: number;
    galaxy: number;
    system: number;
    position: number;
    planet: Planet;
}