package com.myproject.spacegame.buildingStats;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingStatsRepository extends JpaRepository<BuildingStats, Long>{

	BuildingStats findByLevelAndNameOfBuildingOTechnology(int level, String nameOfBuildingOrTechnology);
	boolean existsByNameOfBuildingOTechnology(String nameOfBuildingOrTechnology);
	boolean existsByLevelAndNameOfBuildingOTechnology(int level, String nameOfBuildingOrTechnology);
}
