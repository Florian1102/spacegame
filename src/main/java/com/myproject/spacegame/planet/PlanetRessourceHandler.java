package com.myproject.spacegame.planet;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.planet.buildings.IronMineStatsRepository;
import com.myproject.spacegame.planet.buildings.PlanetBuildingStats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetRessourceHandler {

	private final IronMineStatsRepository ironMineStatsRepository;
	
	public Planet calculateNewPlanetRessources(Planet planet) {
		long ironMineNewLvl = planet.getIronMineLvl() + 1;
	
		PlanetBuildingStats ironMineStatsOfLvl = ironMineStatsRepository.findById(ironMineNewLvl).get();
		planet.setIron(planet.getIron() - ironMineStatsOfLvl.getNecessaryIron());
		planet.setRemainingBuildingDuration(ironMineStatsOfLvl.getBuildingDuration());
		System.out.println("Bau gestartet");
		return planet;
	}
}
