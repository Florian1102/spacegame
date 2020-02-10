import { Spaceship } from './spaceship.model';
import { Planet } from './planet.model';
import { Technology } from './technology';

export interface User {
    id?: number;
    name: string;
    points?: number;
    spaceship?: Spaceship;
    planets?: Planet[];
    technology?: Technology;
    daysLoggedIn?: number;
}