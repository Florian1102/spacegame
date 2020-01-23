package com.myproject.spacegame.user.spaceship;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceshipStatsRepository extends JpaRepository<SpaceshipStats, Long>{

	boolean existsByLevel(int level);
	SpaceshipStats findByLevel(int level);
}
