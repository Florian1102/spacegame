package com.myproject.spacegame.user.planet;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
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
			int currentLvlOfSpecificBuilding = planetBuildingHandler.getCurrentLvlOfSpecificBuilding(planet, nameOfBuilding);
			
			PlanetBuildingStats specificBuildingStatsOfNextLvl = getStatsOfBuildingsAndTechnologies.getBuildingStatsOfNextLvl(currentLvlOfSpecificBuilding, nameOfBuilding);
			planet.setMetal(planet.getMetal() - specificBuildingStatsOfNextLvl.getNecessaryMetal());
			planet.setCrystal(planet.getCrystal() - specificBuildingStatsOfNextLvl.getNecessaryCrystal());
			planet.setHydrogen(planet.getHydrogen() - specificBuildingStatsOfNextLvl.getNecessaryHydrogen());
			
			planet.setRemainingBuildingDuration((long) (specificBuildingStatsOfNextLvl.getBuildingDuration()*(Math.pow(0.5, planet.getCommandCentralLvl()-1))));
			planetRepository.save(planet);
			
			planetBuildingHandler.prepareBuilding(planet, specificBuildingStatsOfNextLvl);
			return planet;
			
		} catch (Exception e) {
			e.getMessage();
		}
		return planet;
		
	}
	
}
