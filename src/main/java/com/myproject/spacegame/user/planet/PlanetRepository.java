package com.myproject.spacegame.user.planet;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanetRepository extends JpaRepository<Planet, Long> {

	Long countByUserId(Long id);
	
	List<Planet> findByUserId(Long id);
}
