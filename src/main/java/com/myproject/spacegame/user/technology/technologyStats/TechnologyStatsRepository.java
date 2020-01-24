package com.myproject.spacegame.user.technology.technologyStats;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnologyStatsRepository extends JpaRepository<TechnologyStats, Long> {

	TechnologyStats findByLevelAndNameOfTechnology(int level, String nameOfTechnology);
	boolean existsByLevelAndNameOfTechnology(int currentLevel, String nameOfTechnology);
}
