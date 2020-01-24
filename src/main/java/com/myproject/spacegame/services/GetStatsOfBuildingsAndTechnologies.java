package com.myproject.spacegame.services;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.user.planet.buildings.NamesOfPlanetBuildings;
import com.myproject.spacegame.user.planet.buildings.PlanetBuildingStats;
import com.myproject.spacegame.user.planet.buildings.PlanetBuildingStatsRepository;
import com.myproject.spacegame.user.technology.technologyStats.NamesOfTechnologies;
import com.myproject.spacegame.user.technology.technologyStats.TechnologyStats;
import com.myproject.spacegame.user.technology.technologyStats.TechnologyStatsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetStatsOfBuildingsAndTechnologies {

	private final PlanetBuildingStatsRepository planetBuildingStatsRepository;
	private final TechnologyStatsRepository technologyStatsRepository;

	public PlanetBuildingStats getBuildingStatsOfNextLvl(int currentBuildingLvl, NamesOfPlanetBuildings nameOfBuilding) {
		
		int nextLvl = currentBuildingLvl + 1;
		PlanetBuildingStats buildingStatsOfNextLvl = planetBuildingStatsRepository.findByLevelAndNameOfBuilding(nextLvl, nameOfBuilding.toString().toLowerCase());
		return buildingStatsOfNextLvl;
	}
	
	public boolean existsNextBuildingLvl(int currentBuildingLvl, NamesOfPlanetBuildings nameOfBuilding) {
		int nextLvl = currentBuildingLvl + 1;
		if (!planetBuildingStatsRepository.existsByLevelAndNameOfBuilding(nextLvl, nameOfBuilding.toString().toLowerCase())) {
			return false;
		} else {
			return true;
		}
	}
	
	public TechnologyStats getTechnologyStatsOfNextLvl(int currentTechnologyLvl, NamesOfTechnologies nameOfTechnology) {
		
		int nextLvl = currentTechnologyLvl + 1;
		TechnologyStats technologyStatsOfNextLvl = technologyStatsRepository.findByLevelAndNameOfTechnology(nextLvl, nameOfTechnology.toString().toLowerCase());
		return technologyStatsOfNextLvl;
	}
	
	public boolean existsNextTechnologyLvl(int currentTechnologyLvl, NamesOfTechnologies nameOfTechnology) {
		int nextLvl = currentTechnologyLvl + 1;
		if (!technologyStatsRepository.existsByLevelAndNameOfTechnology(nextLvl, nameOfTechnology.toString().toLowerCase())) {
			return false;
		} else {
			return true;
		}
	}
	
}
