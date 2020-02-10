import { CoordinateSystem } from './coordinate.model';
import { User } from './user.model';

export interface Spaceship {

    id: number;
    user: User;
    coordinateSystem: CoordinateSystem
    spaceshipLvl: number;
    attackPower: number;
    defense: number;
    speed: number;
    metal: number;
    crystal: number;
    hydrogen: number;
    energy: number;
    metalStoreLvl: number;
    crystalStoreLvl: number;
    hydrogenTankLvl: number;
    metalStore: number;
    crystalStore: number;
    hydrogenTank: number;
    researchLaboratoryLvl: number;
    remainingBuildingDuration: number;
    remainingResearchDuration: number;
    hydrogenConsumption: number;
    reduceBuildingDuration: number;
    reduceResearchDuration: number;
    merchantSpaceship: boolean;
    fighterSpaceship: boolean;
    counterOfChangedSpecialization: number;
    flightDuration: number;

}