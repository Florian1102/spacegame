package com.myproject.spacegame.user.planet.buildings;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetBuildingStatsRepository extends JpaRepository<BuildingStats, Long>{

	BuildingStats findByLevelAndNameOfBuilding(int level, String nameOfBuilding);
	boolean existsByNameOfBuilding(String nameOfBuilding);
	boolean existsByLevelAndNameOfBuilding(int level, String nameOfBuilding);
}
