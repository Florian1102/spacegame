package com.myproject.spacegame.user.planet;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.user.planet.buildings.IronMineStatsRepository;
import com.myproject.spacegame.user.planet.buildings.PlanetBuildingStats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetRessourceHandler {

	private final IronMineStatsRepository ironMineStatsRepository;
	private final PlanetRepository planetRepository;
	
	public Planet calculateNewPlanetRessources(Planet planet) {
		long ironMineNewLvl = planet.getIronMineLvl() + 1;
	
		PlanetBuildingStats ironMineStatsOfLvl = ironMineStatsRepository.findById(ironMineNewLvl).get();
		planet.setIron(planet.getIron() - ironMineStatsOfLvl.getNecessaryIron());
		planet.setRemainingBuildingDuration(ironMineStatsOfLvl.getBuildingDuration());
		planetRepository.save(planet);

		System.out.println("Bau gestartet");
		return planet;
	}
}
