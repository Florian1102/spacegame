package com.myproject.spacegame.user.spaceship;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myproject.spacegame.coordinateSystem.CoordinateSystem;
import com.myproject.spacegame.user.planet.Planet;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpaceshipHandler {

	private final SpaceshipRepository spaceshipRepository;
	
	public void flyToPlanet(Spaceship spaceship, Planet planet) {
		//Zeit berechnen
	}
	// statsSpaceshipBerechnen()
	
	public Long calculateFlightDuration(Spaceship spaceship, CoordinateSystem coordinateSystem) {
		
		Long flightDuration = 10L;
		//Speed and entfernung
		return flightDuration;
	}
	
	public void flyBack(Long spaceshipId, Long flyDuration) {
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

		@SuppressWarnings("unused")
		ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
			public Object call() throws Exception {
				Spaceship spaceshipFound = spaceshipRepository.findById(spaceshipId).get();
				spaceshipFound.setFlightDuration(0L);
				spaceshipRepository.save(spaceshipFound);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		}, flyDuration, TimeUnit.SECONDS); 
		executorService.shutdown();
	}
	
}
