package com.myproject.spacegame.user.spaceship;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myproject.spacegame.buildingStats.BuildingStats;
import com.myproject.spacegame.coordinateSystem.CoordinateSystem;
import com.myproject.spacegame.services.GetStatsOfBuildingsAndTechnologies;
import com.myproject.spacegame.user.planet.Planet;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpaceshipHandler {

	private final SpaceshipRepository spaceshipRepository;
	private final GetStatsOfBuildingsAndTechnologies getStatsOfBuildingsAndTechnologies;

	public void flyToPlanet(Spaceship spaceship, Planet planet) {
		// Zeit berechnen
	}

	public Spaceship calculateAndSaveSpaceshipStats(Spaceship spaceshipFound) throws Exception {

		Long attackPower = 0L;
		Long defense = 0L;
		Long speed = 0L;
		double hydrogenConsumption = 0.0;

		try {
			BuildingStats spaceshipStats = getStatsOfBuildingsAndTechnologies
					.getBuildingOrTechnologyStats(spaceshipFound.getSpaceshipLvl(), "spaceship");
			// TODO: alle Buildings und Forschungen vom Rauschiff berücksichtigen
			attackPower = attackPower + spaceshipStats.getAttackPower(); // und hier addieren
			defense = defense + spaceshipStats.getDefense();
			speed = speed + spaceshipStats.getSpeed();
			hydrogenConsumption = hydrogenConsumption + spaceshipStats.getHydrogenConsumption();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}

		if (spaceshipFound.isFighterSpaceship()) {
			attackPower = (long) (attackPower * 1.3); // TODO: Faktoren anpassen, welchen Bonus das Spaceship erhält
			speed = (long) (speed * 1.2);
		} else if (spaceshipFound.isMerchantSpaceship()) {
			defense = (long) (defense * 1.2);
			speed = (long) (speed * 1.4);
			hydrogenConsumption = (long) (hydrogenConsumption * 0.8);
		}

		spaceshipFound.setAttackPower(attackPower);
		spaceshipFound.setDefense(defense);
		spaceshipFound.setSpeed(speed);
		spaceshipFound.setHydrogenConsumption(hydrogenConsumption);
		spaceshipRepository.save(spaceshipFound);

		return spaceshipFound;
	}

	public Long calculateFlightDuration(Spaceship spaceship, CoordinateSystem coordinates) {

		Long speed = spaceship.getSpeed();
		Long galaxyDistance = (long) Math.abs((spaceship.getCurrentPosition().getGalaxy() - coordinates.getGalaxy()));
		Long systemDistance = (long) Math.abs((spaceship.getCurrentPosition().getSystem() - coordinates.getSystem()));
		Long positionDistance = (long) Math
				.abs((spaceship.getCurrentPosition().getPosition() - coordinates.getPosition()));

		Long timeForDistance = (galaxyDistance * 60) + (systemDistance * 20) + (positionDistance * 10); // TODO: ändern
																										// in 5 STunden,
																										// halbe Stunde,
																										// 15 Minuten
		Long flightDuration = timeForDistance / speed;
		return flightDuration;
	}

	public double calculateHydrogenConsumption(Spaceship spaceship, CoordinateSystem coordinates) {

		double hydrogenConsumption = spaceship.getHydrogenConsumption();
		Long galaxyDistance = (long) Math.abs((spaceship.getCurrentPosition().getGalaxy() - coordinates.getGalaxy()));
		Long systemDistance = (long) Math.abs((spaceship.getCurrentPosition().getSystem() - coordinates.getSystem()));
		Long positionDistance = (long) Math
				.abs((spaceship.getCurrentPosition().getPosition() - coordinates.getPosition()));

		Long timeForDistance = (galaxyDistance * 300) + (systemDistance * 15) + (positionDistance * 10); // TODO: evtl.
																											// Werte
																											// anpassen
		double flightDuration = timeForDistance * hydrogenConsumption;
		return flightDuration;
	}

	public void flyBack(Long spaceshipId, Long flightDuration) {
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		@SuppressWarnings("unused")
		ScheduledFuture<?> scheduledFuture = executorService.schedule(new Callable<Object>() {
			public Object call() throws Exception {
				Spaceship spaceshipFound = spaceshipRepository.findById(spaceshipId).get();
				spaceshipFound.setFlightDuration(0L);
				spaceshipRepository.save(spaceshipFound);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		}, flightDuration, TimeUnit.SECONDS);
		executorService.shutdown();
	}

}
