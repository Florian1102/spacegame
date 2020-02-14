package com.myproject.spacegame.services;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.buildingStats.BuildingStats;
import com.myproject.spacegame.buildingStats.BuildingStatsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetStatsOfBuildingsAndTechnologies {

	private final BuildingStatsRepository buildingStatsRepository;

	public BuildingStats getBuildingOrTechnologyStatsOfNextLvl(int currentBuildingOrTechnologyLvl,
			String nameOfBuildingOrTechnology) throws Exception {
		int nextLvl = currentBuildingOrTechnologyLvl + 1;
		if (!buildingStatsRepository.existsByNameOfBuildingOTechnology(nameOfBuildingOrTechnology)) {
			throw new Exception("Das Gebäude existiert nicht.");
		} else if (!buildingStatsRepository.existsByLevelAndNameOfBuildingOTechnology(nextLvl,
				nameOfBuildingOrTechnology)) {
			throw new Exception("Du hast das Maximallevel bereits erreicht");
		} else {
			BuildingStats buildingStatsOfNextLvl = buildingStatsRepository
					.findByLevelAndNameOfBuildingOTechnology(nextLvl, nameOfBuildingOrTechnology);
			return buildingStatsOfNextLvl;
		}
	}

	public BuildingStats getBuildingOrTechnologyStats(int currentBuildingOrTechnologyLvl,
			String nameOfBuildingOrTechnology) throws Exception {

		if (!buildingStatsRepository.existsByLevelAndNameOfBuildingOTechnology(currentBuildingOrTechnologyLvl, nameOfBuildingOrTechnology)) {
			throw new Exception("Das Gebäude existiert nicht");
		} else {
			BuildingStats buildingStats = buildingStatsRepository
					.findByLevelAndNameOfBuildingOTechnology(currentBuildingOrTechnologyLvl, nameOfBuildingOrTechnology);
			return buildingStats;
		}
	}
}
