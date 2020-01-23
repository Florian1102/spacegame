package com.myproject.spacegame.user.planet.buildings;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetBuildingStatsRepository extends JpaRepository<PlanetBuildingStats, Long>{

	PlanetBuildingStats findByLevelAndNameOfBuilding(int level, String nameOfBuilding);
	boolean existsByLevelAndNameOfBuilding(int level, String nameOfBuilding);
}
