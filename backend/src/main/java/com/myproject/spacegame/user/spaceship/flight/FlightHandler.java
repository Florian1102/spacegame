package com.myproject.spacegame.user.spaceship.flight;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.myproject.spacegame.coordinateSystem.CoordinateSystemRepository;
import com.myproject.spacegame.user.planet.ColonizePlanet;
import com.myproject.spacegame.user.planet.ResourceHandler;
import com.myproject.spacegame.user.spaceship.Spaceship;
import com.myproject.spacegame.user.spaceship.SpaceshipRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightHandler {

	private final FlightRepository flightRepository;
	private final SpaceshipRepository spaceshipRepository;
	private final CoordinateSystemRepository coordinateSystemRepository;
	private final ColonizePlanet colonizePlanet;
	private final ResourceHandler resourceHandler;

	ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

	ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(new Runnable() {

		@Override
		public void run() {
			proofFlightsEveryTimeUnit();
		}
	}, 1, 5, TimeUnit.SECONDS);

	private void proofFlightsEveryTimeUnit() {
		List<Flight> flights = flightRepository.findAllByIsFlyingTrue();
		LocalDateTime dateNow = LocalDateTime.now();
		flights.stream().forEach(flight -> {
			if (flight.isFlying() == true && dateNow.isAfter(flight.getArrivalTime())) {
				Spaceship spaceship = flight.getSpaceship();
				try {
					flight.setFlying(false);
					Long planetId;
					switch (flight.getAction()) {
					case "colonize":
						colonizePlanet.colonizePlanet(spaceship.getUser().getId(), flight.getDestinationGalaxy(),
								flight.getDestinationSystem(), flight.getDestinationPosition());
						spaceship.setCurrentPosition(coordinateSystemRepository.findByGalaxyAndSystemAndPosition(
								flight.getDestinationGalaxy(), flight.getDestinationSystem(), flight.getDestinationPosition()));
						break;
					case "deliver":
						planetId = getPlanetId(flight.getDestinationGalaxy(), flight.getDestinationSystem(), flight.getDestinationPosition());
						spaceship = resourceHandler.pickUpOrDeliverResources(spaceship, planetId, flight.getMetal(),
								flight.getCrystal(), flight.getHydrogen(), false);
						spaceship.setCurrentPosition(coordinateSystemRepository.findByGalaxyAndSystemAndPosition(
								flight.getDestinationGalaxy(), flight.getDestinationSystem(), flight.getDestinationPosition()));
						break;
					case "pickup":
						planetId = getPlanetId(flight.getDestinationGalaxy(), flight.getDestinationSystem(), flight.getDestinationPosition());
						spaceship = resourceHandler.pickUpOrDeliverResources(spaceship, planetId, flight.getMetal(),
								flight.getCrystal(), flight.getHydrogen(), true);
						spaceship.setCurrentPosition(coordinateSystemRepository.findByGalaxyAndSystemAndPosition(
								flight.getDestinationGalaxy(), flight.getDestinationSystem(), flight.getDestinationPosition()));
						break;
					case "attack":
						planetId = getPlanetId(flight.getDestinationGalaxy(), flight.getDestinationSystem(), flight.getDestinationPosition());
						// implement function attack()
						spaceship.setCurrentPosition(coordinateSystemRepository.findByGalaxyAndSystemAndPosition(
								flight.getDestinationGalaxy(), flight.getDestinationSystem(), flight.getDestinationPosition()));
						//flyback()
						break;
					case "station":
						planetId = getPlanetId(flight.getDestinationGalaxy(), flight.getDestinationSystem(), flight.getDestinationPosition());
						spaceship.setCurrentPosition(coordinateSystemRepository.findByGalaxyAndSystemAndPosition(
								flight.getDestinationGalaxy(), flight.getDestinationSystem(), flight.getDestinationPosition()));
						break;
					default:
						throw new Exception("Keine gültige Aktion");
					}
				} catch (Exception e) {
					e.getMessage();
					spaceship.setCurrentPosition(coordinateSystemRepository.findByGalaxyAndSystemAndPosition(
							flight.getDestinationGalaxy(), flight.getDestinationSystem(), flight.getDestinationPosition()));
				} finally {
					// Es muss sichergestellt werden dass das Raumschiff wieder eine Position hat
					flightRepository.save(flight);
					spaceshipRepository.save(spaceship);
				}
			}
		});
	}

	private Long getPlanetId(int galaxy, int system, int position) throws Exception {
		if (!coordinateSystemRepository.existsPlanetByGalaxyAndSystemAndPosition(galaxy, system, position)) {
			throw new Exception("Der Planet existiert nicht");
		}
		Long planetId = coordinateSystemRepository.findByGalaxyAndSystemAndPosition(galaxy, system, position)
				.getPlanet().getId();
		return planetId;
	}

}
