package com.myproject.spacegame.coordinateSystem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinateSystemRepository extends JpaRepository<CoordinateSystem, Long> {

	boolean existsByGalaxyAndSystemAndPosition(int galaxy, int system, int position);
	boolean existsPlanetByGalaxyAndSystemAndPosition(int galaxy, int system, int position);
	CoordinateSystem findByGalaxyAndSystemAndPosition(int galaxy, int system, int position);
	List<CoordinateSystem> findAllByGalaxyAndSystem(int galaxy, int system);
}
