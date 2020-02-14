import { User } from './user.model';
import { CoordinateSystem } from './coordinate-system.model';

export interface Planet {

    id: number;
    user: User;
    coordinates: CoordinateSystem;
    fields: number;
    remainingFields: number;
    metal: number;
    crystal: number;
    hydrogen: number;
    energy: number;
    metalProductionEveryHour: number;
    crystalProductionEveryHour: number;
    hydrogenProductionEveryHour: number;
    metalMineLvl: number;
    crystalMineLvl: number;
    hydrogenPlantLvl: number;
    solarPowerPlantLvl: number;
    metalStorehouseLvl: number;
    crystalStorehouseLvl: number;
    hydrogenTankLvl: number;
    metalStorehouse: number;
    crystalStorehouse: number;
    hydrogenTank: number;
    commandCentralLvl: number;
    solarSatellite: number;
    defenseTower: number;
    remainingBuildingDuration: number;
    reduceBuildingDuration: number;
    
}