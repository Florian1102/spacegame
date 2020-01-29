package com.myproject.spacegame.services;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.user.planet.buildings.BuildingStats;
import com.myproject.spacegame.user.planet.buildings.PlanetBuildingStatsRepository;
import com.myproject.spacegame.user.technology.technologyStats.TechnologyStats;
import com.myproject.spacegame.user.technology.technologyStats.TechnologyStatsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetStatsOfBuildingsAndTechnologies {

	private final PlanetBuildingStatsRepository planetBuildingStatsRepository;
//	private final TechnologyStatsRepository technologyStatsRepository;

	public BuildingStats getBuildingStatsOfNextLvl(int currentBuildingLvl, String nameOfBuilding) throws Exception {

		int nextLvl = currentBuildingLvl + 1;
		if (!planetBuildingStatsRepository.existsByNameOfBuilding(nameOfBuilding)) {
			throw new Exception("Das Gebäude existiert nicht.");
		} else if (!planetBuildingStatsRepository.existsByLevelAndNameOfBuilding(nextLvl, nameOfBuilding)) {
			throw new Exception("Du hast das Maximallevel bereits erreicht");
		} else {
			BuildingStats buildingStatsOfNextLvl = planetBuildingStatsRepository.findByLevelAndNameOfBuilding(nextLvl,
					nameOfBuilding);
			return buildingStatsOfNextLvl;
		}
	}

//	public boolean existsBuilding(String nameOfBuilding) {
//
//		if (!planetBuildingStatsRepository.existsByNameOfBuilding(nameOfBuilding)) {
//			return true;
//		} else {
//			return true;
//		}
//	}
//
//	public boolean existsNextBuildingLvl(int currentBuildingLvl, String nameOfBuilding) {
//		int nextLvl = currentBuildingLvl + 1;
//
//		if (!planetBuildingStatsRepository.existsByLevelAndNameOfBuilding(nextLvl, nameOfBuilding)) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//
//	public TechnologyStats getTechnologyStatsOfNextLvl(int currentTechnologyLvl, String nameOfTechnology) {
//
//		int nextLvl = currentTechnologyLvl + 1;
//		TechnologyStats technologyStatsOfNextLvl = technologyStatsRepository.findByLevelAndNameOfTechnology(nextLvl,
//				nameOfTechnology);
//		return technologyStatsOfNextLvl;
//	}
//
//	public boolean existsTechnology(String nameOTechnology) {
//
//		if (technologyStatsRepository.existsByNameOfTechnology(nameOTechnology)) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public boolean existsNextTechnologyLvl(int currentTechnologyLvl, String nameOfTechnology) {
//		int nextLvl = currentTechnologyLvl + 1;
//		if (!technologyStatsRepository.existsByLevelAndNameOfTechnology(nextLvl, nameOfTechnology)) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//	
	

}
