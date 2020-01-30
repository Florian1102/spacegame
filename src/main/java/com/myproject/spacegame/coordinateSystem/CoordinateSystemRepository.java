package com.myproject.spacegame.coordinateSystem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinateSystemRepository extends JpaRepository<CoordinateSystem, Long> {

	boolean existsByGalaxyAndSystemAndPosition(int galaxy, int system, int position);
	CoordinateSystem findByGalaxyAndSystemAndPosition(int galaxy, int system, int position);
	//TODO: Hier muss eine FUnktion eingefügt werden, die nach einem System sucht
}
