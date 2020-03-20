package com.myproject.spacegame.user.spaceship.flight;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {

	List<Flight> findAllBySpaceshipId(Long id);
	Flight findBySpaceshipIdAndIsFlyingTrue(Long id);
	List<Flight> findAllByIsFlyingTrue();

}
