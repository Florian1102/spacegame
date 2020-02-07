import { Spaceship } from './spaceship.model';
import { Planet } from './planet.model';

export interface User {
    id?: number;
    name: string;
    spaceship?: Spaceship;
    planets?: Planet[];
}