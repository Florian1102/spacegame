package com.myproject.spacegame.user.planet;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
import com.myproject.spacegame.user.planet.buildings.NamesOfPlanetBuildings;
import com.myproject.spacegame.user.planet.buildings.PlanetBuildingHandler;
import com.myproject.spacegame.user.planet.buildings.PlanetBuildingStats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetRessourceHandler {

	private final PlanetRepository planetRepository;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;
	private final PlanetBuildingHandler planetBuildingHandler;
	
	public Planet calculateNewPlanetRessources(Planet planet, String nameOfBuilding) {
		
		try {
			int lvlOfSpecificBuilding = getCurrentLvlOfSpecificBuilding(planet, nameOfBuilding);
			
			PlanetBuildingStats specificBuildingStatsOfNextLvl = getStatsOfBuildingsAndTechnologies.getBuildingStatsOfNextLvl(lvlOfSpecificBuilding, nameOfBuilding);
			planet.setMetal(planet.getMetal() - specificBuildingStatsOfNextLvl.getNecessaryMetal());
			planet.setCrystal(planet.getCrystal() - specificBuildingStatsOfNextLvl.getNecessaryCrystal());
			planet.setHydrogen(planet.getHydrogen() - specificBuildingStatsOfNextLvl.getNecessaryHydrogen());
			
			planet.setRemainingBuildingDuration(specificBuildingStatsOfNextLvl.getBuildingDuration());
			planetRepository.save(planet);
			
			planetBuildingHandler.prepareBuilding(planet, specificBuildingStatsOfNextLvl);
			System.out.println("Bau gestartet");
			return planet;
			
		} catch (Exception e) {
			e.getMessage();
		}
		return planet;
		
	}
	
	private int getCurrentLvlOfSpecificBuilding(Planet planet, String nameOfBuilding) throws Exception {
		// TODO: Hier müssen alle Gebäude eingefügt werden
		if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.METALMINE.toString())) {
			return planet.getMetalMineLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.CRYSTALMINE.toString())) {
			return planet.getCrystalMineLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.CRYSTALMINE.toString())) {
			return planet.getCrystalMineLvl();
		} else if (nameOfBuilding.equalsIgnoreCase(NamesOfPlanetBuildings.CRYSTALMINE.toString())) {
			return planet.getCrystalMineLvl();
		} else {
			throw new Exception("Es liegen keine Informationen über das aktuelle Level des Gebäudes vor");
		}
	}
}
