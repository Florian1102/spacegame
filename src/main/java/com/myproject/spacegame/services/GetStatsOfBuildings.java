package com.myproject.spacegame.services;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.user.planet.buildings.NamesOfPlanetBuildings;
import com.myproject.spacegame.user.planet.buildings.PlanetBuildingStats;
import com.myproject.spacegame.user.planet.buildings.PlanetBuildingStatsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetStatsOfBuildings {

	private final PlanetBuildingStatsRepository planetBuildingStatsRepository;

	public PlanetBuildingStats getIronMineStatsOfNextLvl(int currentIronMineLvl, NamesOfPlanetBuildings nameOfBuilding) {
		int nextLvl = currentIronMineLvl + 1;
		PlanetBuildingStats ironMineStatsOfNextLvl = planetBuildingStatsRepository.findByLevelAndNameOfBuilding(nextLvl, nameOfBuilding.toString().toLowerCase());
		return ironMineStatsOfNextLvl;
	}
	
	public boolean existsNextBuildingLvl(int currentBuildingLvl, NamesOfPlanetBuildings nameOfBuilding) {
		int nextLvl = currentBuildingLvl + 1;
		if (!planetBuildingStatsRepository.existsByLevelAndNameOfBuilding(nextLvl, nameOfBuilding.toString().toLowerCase())) {
			return false;
		} else {
			return true;
		}
	}
	
}
