package com.myproject.spacegame.user.planet;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.services.GetStatsOfBuildings;
import com.myproject.spacegame.user.planet.buildings.NamesOfPlanetBuildings;
import com.myproject.spacegame.user.planet.buildings.PlanetBuildingStats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetRessourceHandler {

	private final PlanetRepository planetRepository;
	private final GetStatsOfBuildings getStatsOfBuildings;
	
	public Planet calculateNewPlanetRessources(Planet planet) {
		
		PlanetBuildingStats ironMineStatsOfNextLvl = getStatsOfBuildings.getIronMineStatsOfNextLvl(planet.getMetalMineLvl(), NamesOfPlanetBuildings.METALMINE);
		planet.setMetal(planet.getMetal() - ironMineStatsOfNextLvl.getNecessaryMetal());
		planet.setCrystal(planet.getCrystal() - ironMineStatsOfNextLvl.getNecessaryCrystal());
		planet.setHydrogen(planet.getHydrogen() - ironMineStatsOfNextLvl.getNecessaryHydrogen());
		
		planet.setRemainingBuildingDuration(ironMineStatsOfNextLvl.getBuildingDuration());
		planetRepository.save(planet);

		System.out.println("Bau gestartet");
		return planet;
	}
}
